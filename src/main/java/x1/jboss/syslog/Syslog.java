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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Send a message via syslog.
 *
 * this code is taken from spy.jar and enhanced User: cmott
 */
public class Syslog {
  public static final int EMERG = 0;
  public static final int ALERT = 1;
  public static final int CRIT = 2;
  public static final int ERR = 3;
  public static final int WARNING = 4;
  public static final int NOTICE = 5;
  public static final int INFO = 6;
  public static final int DEBUG = 7;

  public static final int KERN = 0;
  public static final int USER = 8;
  public static final int MAIL = 16;
  public static final int DAEMON = 24;
  public static final int AUTH = 32;
  public static final int SYSLOG = 40;
  public static final int LPR = 48;
  public static final int NEWS = 56;
  public static final int UUCP = 64;
  public static final int CRON = 72;
  public static final int AUTHPRIV = 80;
  public static final int FTP = 88;
  public static final int LOCAL0 = 128;
  public static final int LOCAL1 = 136;
  public static final int LOCAL2 = 144;
  public static final int LOCAL3 = 152;
  public static final int LOCAL4 = 160;
  public static final int LOCAL5 = 168;
  public static final int LOCAL6 = 176;
  public static final int LOCAL7 = 184;

  private static final int PORT = 514;

  private final InetAddress addr;
  private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getParent();

  /**
   * Log to a particular log host.
   */
  public Syslog(String loghost) throws UnknownHostException {
    addr = InetAddress.getByName(loghost);
  }

  /**
   * Send a log message.
   */
  public void log(int facility, int level, String msg) {
    int fl = facility | level;

    String what = "<" + fl + ">" + msg;
    DatagramSocket s = null;
    
    try {
      byte[] data = what.getBytes("UTF-8");
      DatagramPacket dp = new DatagramPacket(data, data.length, addr, PORT);
      s = new DatagramSocket();
      s.send(dp);
    } catch (IOException e) {
      logger.log(Level.WARNING, "Error sending syslog packet", e);
    } finally {
      if (s != null && !s.isClosed()) {
        s.close();
      }      
    }
  }

}