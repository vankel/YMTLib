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

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

@Deprecated
public class Tool {
	private long lastClickTime = 0;

	@Deprecated
	public Block getBlock(int blockId) {
		return Utils.getBlock(blockId);
	}

	@Deprecated
	public int getUnusedEntityID() {
		return Utils.getUnusedEntityID();
	}

	@Deprecated
	public boolean hasString(String text) {
		return Utils.hasString(text);
	}

	@Deprecated
	public boolean isClientSide(World world) {
		return Utils.isClientSide(world);
	}

	@Deprecated
	public boolean isForge() {
		return Utils.isForge();
	}

	@Deprecated
	public boolean isServerSide(World world) {
		return Utils.isServerSide(world);
	}

	@Deprecated
	public boolean isValidTileEntity(TileEntity ent) {
		return Utils.isValidTileEntity(ent);
	}

	@Deprecated
	public void showMessage(EntityPlayer player, String msg) {
		Utils.showMessage(player, msg);
	}

	@Deprecated
	public void showMessage(World world, String msg) {
		Utils.showMessage(world, msg);
	}

	@Deprecated
	public void spawnExplodeParticle(World world, double x, double y, double z) {
		Utils.spawnExplodeParticle(world, x, y, z);
	}

	@Deprecated
	public void spawnPortalParticle(World world, double x, double y, double z) {
		Utils.spawnPortalParticle(world, x, y, z);
	}

	@Deprecated
	public boolean tryInteractServer(World world) {
		if (isServerSide(world)) {
			long time = world.getWorldTime();
			if (12 < Math.abs(lastClickTime - time)) {
				this.lastClickTime = time;
				return true;
			}
		}
		return false;
	}

	@Deprecated
	public boolean tryUseItems(EntityPlayer player, Item item, boolean consumed) {
		return Utils.tryUseItems(player, item, consumed);
	}
}
