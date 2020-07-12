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
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import mod.ymt.cmn.LineFormatter;
import mod.ymt.cmn.PureConsoleHandler;
import mod.ymt.cmn.StdoutInterceptor;
import mod.ymt.cmn.Tool;
import mod.ymt.cmn.Trace;
import mod.ymt.cmn.Utils;
import net.minecraft.client.Minecraft;

public class mod_YMTLib extends BaseMod {
	@MLProp
	public static String logConfig = "/mod/ymt/cmn/logging.properties";
	@MLProp
	public static boolean debug = false;
	@MLProp
	public static boolean stdoutIntercept = true;

	@Override
	public String getPriorities() {
		return null;
	}

	@Override
	public String getVersion() {
		return "152v2";
	}

	@Override
	public void load() {
		if (debug) {
			logInitialize();
		}
	}

	@Override
	public void modsLoaded() {
		if (debug) {
			// システム情報印字
			Trace.logFine("------System Environment");
			for (Entry<String, String> ent: System.getenv().entrySet()) {
				Trace.logFine(ent.getKey() + " = " + ent.getValue());
			}
			Trace.logFine("------End");
			Trace.logFine("------System Properties");
			for (Entry<Object, Object> ent: System.getProperties().entrySet()) {
				Trace.logFine(ent.getKey() + " = " + ent.getValue());
			}
			Trace.logFine("------End");
			// ブロックID印字
			for (int i = 0; i < Block.blocksList.length; i++) {
				Block block = Block.blocksList[i];
				if (block != null) {
					Trace.logConfig("blocksList[%d] = %s", i, block);
				}
			}
			// アイテムID印字
			for (int i = 0; i < Item.itemsList.length; i++) {
				Item item = Item.itemsList[i];
				if (item != null) {
					if (item instanceof ItemBlock)
						Trace.logConfig("itemsList[%d] = %s(%s)", i, item, Block.blocksList[((ItemBlock) item).getBlockID()]);
					else
						Trace.logConfig("itemsList[%d] = %s", i, item);
				}
			}
			// エンティティID印字
			for (int i = Byte.MIN_VALUE; i < Short.MAX_VALUE; i++) {
				Class entClass = EntityList.getClassFromID(i);
				if (entClass != null) {
					Trace.logConfig("entityList[%d] = %s", i, entClass);
				}
				else if (3000 < i) {
					break;
				}
			}
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
		try {
			FileHandler handler = new FileHandler(Minecraft.getMinecraftDir().getAbsolutePath() + "/Minecraft.%g.log", 10 * 1024 * 1024, 8);
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
