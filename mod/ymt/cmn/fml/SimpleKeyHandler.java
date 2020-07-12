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
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;

public abstract class SimpleKeyHandler extends KeyBindingRegistry.KeyHandler {
	public SimpleKeyHandler(KeyBinding kb) {
		this(kb, false);
	}

	public SimpleKeyHandler(KeyBinding kb, boolean repeat) {
		super(new KeyBinding[]{
			kb
		}, new boolean[]{
			repeat
		});
	}

	public abstract void action(EnumSet<TickType> types, KeyBinding kb, boolean isRepeat);

	@Override
	public String getLabel() {
		return toString() + "(" + keyBindings[0].keyCode + ")";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd) {
			action(types, kb, isRepeat);
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		;
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
}