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

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logging handler implementation for syslog
 */
public class SyslogHandler extends Handler {
  private Syslog sysLogger = null;
  // private Thread pump = null;
  // private BooleanLatch done = new BooleanLatch();
  // private BlockingQueue<LogRecord> pendingRecords = new
  // ArrayBlockingQueue<LogRecord>(
  // 5000);
  private String loghost = "localhost";
  private String protocol = "udp";
  private int port = 514;
  private String hostname = "localhost";
  private String application = "java";
  private String pid = null;
  private String facility = "daemon";
  private int facilityInt = Syslog.DAEMON;

  public SyslogHandler() {
    super();
    setFormatter(new SyslogFormatter());
    try {
      InetAddress addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
    } catch (UnknownHostException e) {
    }
  }

  private boolean init() {
    pid = ManagementFactory.getRuntimeMXBean().getName();
    // set up the connection
    try {
      if (protocol.equals("udp")) {
        sysLogger = new UdpSyslog(new InetSocketAddress(loghost, port));
        return true;
      } else if (protocol.equals("tcp")) {
        sysLogger = new TcpSyslog(new InetSocketAddress(loghost, port));
        return true;
      }

    } catch (java.net.UnknownHostException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "unknown host: " + e.getMessage(), e);
    } catch (SocketException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
    } catch (IOException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
    }
    return false;
  }

  private void log(LogRecord record) {
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
    sysLogger.log(this.facilityInt, l, msg);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
   */
  @Override
  public void publish(LogRecord record) {
    if (!init()) {
      throw new IllegalStateException("Can't configure SyslogHandler");
    }
    log(record);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#close()
   */
  @Override
  public void close() {
    if (this.sysLogger != null) {
      this.sysLogger.close();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.util.logging.Handler#flush()
   */
  @Override
  public void flush() {
  }

  public String getLoghost() {
    return loghost;
  }

  public void setLoghost(String loghost) {
    this.loghost = loghost;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public void setPort(String port) {
    this.port = Integer.parseInt(port);
  }

  public String getPort() {
    return "" + this.port;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }
  
  public String getFacility() {
    return this.facility;
  }
  
  public void setFacility(String facility) {
    if (Syslog.FACILITY_MAP.containsKey(facility)) {
      this.facility = facility;
      this.facilityInt = (Integer)Syslog.FACILITY_MAP.get(facility);
    }
    else
      throw new IllegalArgumentException("Illegal syslog facility name: " + facility);
  }
}
