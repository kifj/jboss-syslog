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
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Send a message via syslog.
 * 
 * this code is taken from spy.jar and enhanced User: cmott
 */
public class TcpSyslog extends Syslog {
	private final Socket socket;
	private final PrintStream out;

	/**
	 * Log to a particular log host.
	 * 
	 * @throws SocketException
	 */
	public TcpSyslog(InetSocketAddress destination)
			throws UnknownHostException, IOException {
		super(destination);
		this.socket = new Socket();
		this.socket.connect(destination);
		this.out = new PrintStream(this.socket.getOutputStream());
	}

	/**
	 * Send a log message.
	 */
	public void log(int facility, int level, String msg) {
		int fl = facility | level;

		String what = "<" + fl + ">" + msg;

		out.println(what);
		out.flush();
	}

	public void close() {
		safeClose(out);
		safeClose(socket);
	}
	
	private void safeClose(PrintStream out) {
		try{out.close();}catch(Exception e) {}
	}
	private void safeClose(Socket out) {
		try{out.close();}catch(Exception e) {}
	}
}