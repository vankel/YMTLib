/**
 * Copyright 2013 Yamato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mod.ymt.cmn;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LineFormatter extends Formatter {
	private final String CRLF = System.getProperty("line.separator", "\r\n");
	private final String TAB = "\t";
	
	@Override
	public String format(LogRecord aRecord) {
		final StringBuffer message = new StringBuffer(131);
		
		message.append(String.format("%tY/%<tm/%<td %<tT.%<tL", aRecord.getMillis()));
		message.append(TAB);
		message.append("[");
		message.append(aRecord.getLoggerName());
		message.append("]");
		message.append(TAB);
		message.append(aRecord.getLevel().toString());
		message.append(TAB);
		message.append(formatMessage(aRecord));
		message.append(CRLF);
		
		Throwable th = aRecord.getThrown();
		if (th != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			th.printStackTrace(pw);
			pw.close();
			message.append(sw);
			message.append(CRLF);
		}
		return message.toString();
	}
}
