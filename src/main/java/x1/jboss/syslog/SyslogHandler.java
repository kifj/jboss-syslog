/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms of the GNU
 * General Public License Version 2 only ("GPL").  
 * You may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at http://www.gnu.org/licenses/gpl-2.0.html
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * This particular file is designated as subject to the "Classpath"
 * exception. 
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright (c) 2009-2010 Oracle and/or its affiliates. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package x1.jboss.syslog;

import java.util.logging.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Logging handler implementation for syslog
 */
public class SyslogHandler extends Handler {
  private Syslog sysLogger = null;
  private Thread pump = null;
  private BooleanLatch done = new BooleanLatch();
  private BlockingQueue<LogRecord> pendingRecords = new ArrayBlockingQueue<LogRecord>(5000);
  private String loghost = "localhost";
  private String hostname = "localhost";
  private String application = "java";
  private String pid = null;

  public SyslogHandler() {
    setFormatter(new SyslogFormatter());
  }

  private void init() {
    pid = ManagementFactory.getRuntimeMXBean().getName();
    LogManager manager = LogManager.getLogManager();
    String cname = getClass().getName();
    String systemLogging = manager.getProperty(cname + ".useSystemLogging");
    if (systemLogging != null && systemLogging.equals("false")) {
      return;
    }
    // set up the connection
    try {
      sysLogger = new Syslog(loghost);
    } catch (java.net.UnknownHostException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "unknown host");
      return;
    }
    try {
      InetAddress addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
    } catch (UnknownHostException e) {
    }

    // start the Queue consummer thread.
    pump = new Thread() {
      public void run() {
        try {
          while (!done.isSignalled()) {
            log();
          }
        } catch (RuntimeException e) {
          Logger.getAnonymousLogger().log(Level.WARNING, "Error while logging: " + e.getMessage());
        }
      }
    };
    pump.start();
  }

  private void log() {
    LogRecord record;

    try {
      record = pendingRecords.take();
    } catch (InterruptedException e) {
      return;
    }
    Level level = record.getLevel();
    int l;
    String slLvl;

    if (level.equals(Level.SEVERE)) {
      l = Syslog.CRIT;
      slLvl = "ERR  ";
    } else if (level.equals(Level.WARNING)) {
      l = Syslog.WARNING;
      slLvl = "WARN ";
    } else if (level.equals(Level.INFO)) {
      l = Syslog.INFO;
      slLvl = "INFO ";
    } else {
      l = Syslog.DEBUG;
      slLvl = "DEBUG";
    }

    String msg = hostname + " " + application + "[" + pid + "]: " + slLvl + " " + getFormatter().format(record);
    if (msg.length() > 1024) {
      msg = msg.substring(0, 1024);
    }
    // send message
    sysLogger.log(Syslog.DAEMON, l, msg);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
   */
  @Override
  public void publish(LogRecord record) {
    if (pid == null) {
      init();
    }
    if (pump == null) {
      return;
    }
    try {
      pendingRecords.add(record);
    } catch (IllegalStateException e) {
      // queue is full, start waiting.
      try {
        pendingRecords.put(record);
      } catch (InterruptedException e1) {
        // record is lost...
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#close()
   */
  @Override
  public void close() {
    done.tryReleaseShared(0);
    pump = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#flush()
   */
  @Override
  public void flush() {
    if (!pendingRecords.isEmpty()) {
      log();
    }
  }

  public String getLoghost() {
    return loghost;
  }

  public void setLoghost(String loghost) {
    this.loghost = loghost;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }
}