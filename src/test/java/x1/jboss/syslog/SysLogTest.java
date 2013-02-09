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

import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

public class SysLogTest {

  @Test
  public void testUdpLogger() throws Exception {
    Logger logger = Logger.getLogger(SysLogTest.class.getName());
    SyslogHandler handler = new SyslogHandler();
    assertEquals("localhost", handler.getLoghost());
    assertEquals("udp", handler.getProtocol());
    handler.setLoghost("192.168.122.251");
    assertNotNull(handler.getFormatter());
    logger.addHandler(handler);
    logger.warning("test-1");
    assertNotNull(handler.getFormatter());
    handler.setLevel(Level.WARNING);
    logger.log(Level.SEVERE, "test-2: {0} {1}", new Object[] { "p1", 1 });
    logger.fine("test-3");
    logger.log(Level.WARNING, "test-4: %s %d", new Object[] { "p1", 1 });
    Thread.sleep(500);
    handler.flush();
  }
  
  @Test
  public void testTcpLogger() throws Exception {
    Logger logger = Logger.getLogger(SysLogTest.class.getName());
    SyslogHandler handler = new SyslogHandler();
    handler.setProtocol("tcp");
    assertEquals("localhost", handler.getLoghost());
    assertEquals("tcp", handler.getProtocol());
    handler.setLoghost("192.168.122.251");
    assertNotNull(handler.getFormatter());
    logger.addHandler(handler);
    logger.warning("test-1");
    assertNotNull(handler.getFormatter());
    handler.setLevel(Level.WARNING);
    logger.log(Level.SEVERE, "test-2: {0} {1}", new Object[] { "p1", 1 });
    logger.fine("test-3");
    logger.log(Level.WARNING, "test-4: %s %d", new Object[] { "p1", 1 });
    Thread.sleep(500);
    handler.flush();
  }
}
