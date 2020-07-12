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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StdoutInterceptor {
	private static final PrintStream pureOut = System.out;
	private static final PrintStream pureErr = System.err;
	private static final Logger STDOUT = Logger.getLogger("STDOUT");
	private static final Logger STDERR = Logger.getLogger("STDERR");
	private static final StdoutInterceptor instance = new StdoutInterceptor();

	private final Map<Level, PrintStream> output = new HashMap<Level, PrintStream>();
	{
		putOutput(output, Level.SEVERE, STDERR);
		putOutput(output, Level.WARNING, STDERR);
		putOutput(output, Level.INFO, STDOUT);
		putOutput(output, Level.CONFIG, STDOUT);
		putOutput(output, Level.FINE, STDOUT);
		putOutput(output, Level.FINER, STDOUT);
		putOutput(output, Level.FINEST, STDOUT);
	};
	private boolean running;

	private static final Executor outputExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread th = new Thread(r);
			th.setDaemon(true);
			return th;
		}
	});

	private StdoutInterceptor() {
		;
	}

	public PrintStream getPrintStream(Level level) {
		if (isRunning()) {
			PrintStream result = output.get(level);
			if (result == null)
				return output.get(Level.INFO);
			return result;
		}
		else {
			if (level == Level.SEVERE || level == Level.WARNING)
				return pureErr;
			else
				return pureOut;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void start() {
		synchronized (this) {
			if (running)
				return;
			running = true;
		}
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				for (PrintStream ps: output.values()) {
					ps.close();
				}
				getPureStdOut().println("shutdown Diagnostics.");
			}
		});
		System.setErr(getPrintStream(Level.SEVERE));
		System.setOut(getPrintStream(Level.INFO));
	}

	private void log(PrintStream ps, String format, Object... args) {
		if (ps != null) {
			if (args == null || args.length == 0)
				ps.println(format);
			else
				ps.println(String.format(format, args));
		}
	}

	private Level toLogLevel(String logLevel) {
		try {
			return logLevel == null ? Level.INFO : Level.parse(logLevel.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace(getPureStdErr());
			return Level.INFO;
		}
	}

	public static StdoutInterceptor getInstance() {
		return instance;
	}

	public static PrintStream getPureStdErr() {
		return pureErr;
	}

	public static PrintStream getPureStdOut() {
		return pureOut;
	}

	private static Handler init(Handler handler) {
		handler.setFormatter(new LineFormatter());
		return handler;
	}

	private static void putOutput(Map<Level, PrintStream> output, Level level, Logger logger) {
		output.put(level, new PrintStream(new OutputStreamAdapter(logger, level)));
	}

	public static class OutputStreamAdapter extends OutputStream {
		private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		private final Logger target;
		private final Level level;

		public OutputStreamAdapter(Logger target, Level level) {
			this.target = target;
			this.level = level;
		}

		@Override
		public void close() throws IOException {
			flush();
		}

		@Override
		public void flush() throws IOException {
			final byte[] data = buffer.toByteArray();
			if (0 < data.length) {
				outputExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String text = new String(data, "SJIS");
							for (String line: text.split("\r\n|\r|\n")) {
								target.log(level, line);
							}
						}
						catch (UnsupportedEncodingException e) {
							e.printStackTrace(getPureStdErr());
						}
					}
				});
			}
			buffer.reset();
		}

		@Override
		public void write(byte[] b) throws IOException {
			if (target.isLoggable(level)) {
				buffer.write(b);
				flush();
			}
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			if (target.isLoggable(level)) {
				buffer.write(b, off, len);
				flush();
			}
		}

		@Override
		public void write(int b) throws IOException {
			if (target.isLoggable(level)) {
				buffer.write(b);
				if (b == '\r' || b == '\n') {
					flush();
				}
			}
		}
	}
}
