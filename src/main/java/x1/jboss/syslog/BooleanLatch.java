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

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Acts like a CountDownLatch except that it only requires a single signal to
 * fire. Because a latch is non-exclusive, it uses the shared acquire and
 * release methods.
 * 
 * @author Jerome Dochez
 */
public class BooleanLatch extends AbstractQueuedSynchronizer {
  private static final long serialVersionUID = -2380570517815530977L;

  public boolean isSignalled() {
    return getState() != 0;
  }

  public int tryAcquireShared(int ignore) {
    return isSignalled() ? 1 : -1;
  }

  public boolean tryReleaseShared(int ignore) {
    setState(1);
    return true;
  }
}