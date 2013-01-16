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

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Logging formatter for Syslog
 */
public class SyslogFormatter extends Formatter {
	// private final Date dat = new Date();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord record) {
		// dat.setTime(record.getMillis());
		String source;
		if (record.getSourceClassName() != null) {
			source = record.getSourceClassName();
			if (record.getSourceMethodName() != null) {
				source += " " + record.getSourceMethodName();
			}
		} else {
			source = record.getLoggerName();
		}
		String message = formatMessage(record);
		return " [" + source + "] " + message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.util.logging.Formatter#formatMessage(java.util.logging.LogRecord)
	 */
	@Override
	public synchronized String formatMessage(LogRecord record) {
		String format = record.getMessage();
		java.util.ResourceBundle catalog = record.getResourceBundle();
		if (catalog != null) {
			try {
				format = catalog.getString(record.getMessage());
			} catch (java.util.MissingResourceException ex) {
				// Drop through. Use record message as format
				format = record.getMessage();
			}
		}
		// Do the formatting.
		try {
			Object[] parameters = record.getParameters();
			if (parameters == null || parameters.length == 0) {
				// No parameters. Just return format string.
				return format;
			}
			// Is it a java.text style format?
			// Ideally we could match with
			// Pattern.compile("\\{\\d").matcher(format).find())
			// However the cost is 14% higher, so we cheaply check for
			// 1 of the first 4 parameters
			if (format.indexOf("{0") >= 0 || format.indexOf("{1") >= 0
					|| format.indexOf("{2") >= 0 || format.indexOf("{3") >= 0) {
				return java.text.MessageFormat.format(format, parameters);
			}
			return String.format(format, record.getParameters());
		} catch (Exception ex) {
			// Formatting failed: use localized format string.
			return format;
		}
	}

}
