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
package mod.ymt.cmn.fml;

import java.util.EnumSet;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * @author Yamato
 *
 */
public abstract class SimpleTickHandler implements ITickHandler {
	private final EnumSet<TickType> ticks;
	
	public SimpleTickHandler(TickType first, TickType... rest) {
		this.ticks = EnumSet.of(first, rest);
	}
	
	public abstract void action(EnumSet<TickType> types, Object... tickData);
	
	@Override
	public String getLabel() {
		return getClass().getSimpleName();
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		;
	}
	
	@Override
	public EnumSet<TickType> ticks() {
		return ticks;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		action(type, tickData);
	}
}
