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

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Send a message via syslog.
 *
 * this code is taken from spy.jar and enhanced User: cmott
 */
public abstract class Syslog {
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

	static final Map<String, Integer> FACILITY_MAP = new HashMap<String, Integer>();

	static {
		FACILITY_MAP.put("kern", KERN);
		FACILITY_MAP.put("user", USER);
		FACILITY_MAP.put("mail", MAIL);
		FACILITY_MAP.put("daemon", DAEMON);
		FACILITY_MAP.put("auth", AUTH);
		FACILITY_MAP.put("syslog", SYSLOG);
		FACILITY_MAP.put("lpr", LPR);
		FACILITY_MAP.put("news", NEWS);
		FACILITY_MAP.put("uucp", UUCP);
		FACILITY_MAP.put("cron", CRON);
		FACILITY_MAP.put("authpriv", AUTHPRIV);
		FACILITY_MAP.put("ftp", FTP);
		FACILITY_MAP.put("local0", LOCAL0);
		FACILITY_MAP.put("local1", LOCAL1);
		FACILITY_MAP.put("local2", LOCAL2);
		FACILITY_MAP.put("local3", LOCAL3);
		FACILITY_MAP.put("local4", LOCAL4);
		FACILITY_MAP.put("local5", LOCAL5);
		FACILITY_MAP.put("local6", LOCAL6);
		FACILITY_MAP.put("local7", LOCAL7);
	}

	private final InetSocketAddress destination;

	/**
	 * Log to a particular log host.
	 *
	 * @throws SocketException
	 */
	public Syslog(InetSocketAddress destination) throws UnknownHostException, SocketException {
		this.destination = destination;
	}

	public InetSocketAddress getDestination() {
		return this.destination;
	}

	/**
	 * Send a log message.
	 */
	public abstract void log(int facility, int level, String msg);

	public abstract void close();

}
