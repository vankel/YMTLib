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
package net.minecraft.src;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mod.ymt.cmn.CfgFile;
import mod.ymt.cmn.LineFormatter;
import mod.ymt.cmn.PureConsoleHandler;
import mod.ymt.cmn.StdoutInterceptor;
import mod.ymt.cmn.Utils;

public class mod_YMTLib extends BaseMod {
	private String logConfig = "";
	private boolean debug = false;
	private boolean stdoutIntercept = false;

	@Override
	public String getPriorities() {
		return null;
	}

	@Override
	public String getVersion() {
		return "162v3";
	}

	@Override
	public void load() {
		CfgFile cfg = new CfgFile("mod_YMTLib.txt");
		debug = cfg.getBoolean("debug", false);
		stdoutIntercept = cfg.getBoolean("stdoutIntercept", false);
		logConfig = cfg.getString("logConfig", "/mod/ymt/cmn/logging.properties");
		cfg.save();

		if (debug) {
			logInitialize();
		}
	}

	protected void logInitialize() {
		// まずはログ設定ファイルを読み込む
		if (Utils.hasString(logConfig)) {
			InputStream ins = getClass().getResourceAsStream(logConfig);
			if (ins != null) {
				try {
					try {
						LogManager.getLogManager().readConfiguration(ins);
					}
					finally {
						ins.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// Formatter 等を設定(Minecraft から読み込むと Logger とは ClassLoader が異なるので読み込めない)
		try {
			PureConsoleHandler handler = new PureConsoleHandler();
			handler.setLevel(Level.ALL);
			handler.setFormatter(new LineFormatter());
			Logger.getLogger("").addHandler(handler);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// StdoutInterceptor 起動
		if (stdoutIntercept) {
			StdoutInterceptor.getInstance().start();
		}
	}
}
