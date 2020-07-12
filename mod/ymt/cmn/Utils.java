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

import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author Yamato
 *
 */
public class Utils {
	private static final Logger log = Logger.getLogger(Utils.class.getName());

	private Utils() {
		;
	}

	public static void addLocalization(String key, String en_name, String ja_name) {
		LanguageRegistry.instance().addStringLocalization(key, en_name);
		LanguageRegistry.instance().addStringLocalization(key, "ja_JP", ja_name);
	}

	public static void addName(Object obj, String en_name, String ja_name) {
		LanguageRegistry.instance().addName(obj, en_name);
		LanguageRegistry.instance().addNameForObject(obj, "ja_JP", ja_name);
	}

	public static int compare(int x, int y) {
		if (x < y)
			return -1;
		if (x > y)
			return 1;
		return 0;
	}

	public static Block getBlock(int blockId) {
		if (blockId != 0) {
			if (0 < blockId && blockId < Block.blocksList.length) {
				Block block = Block.blocksList[blockId];
				if (block != null) {
					return block;
				}
			}
			log.fine(String.format("Tool#getBlock - UnknownBlockID[%s]", blockId));
		}
		return null;
	}

	public static int getUnusedEntityID() {
		int result = -1;
		if (isForge()) {
			try {
				Class entityRegistry = Class.forName("cpw.mods.fml.common.registry.EntityRegistry");
				result = (Integer) entityRegistry.getMethod("findGlobalUniqueEntityId").invoke(null);
			}
			catch (Exception e) {
				throw new RuntimeException("EntityID cant generated", e);
			}
		}
		else {
			for (int i = 1; i <= Byte.MAX_VALUE; i++) {
				if (EntityList.getClassFromID(i) == null) {
					result = i;
					break;
				}
			}
		}
		if (result < 0) {
			throw new RuntimeException("EntityID cant generated");
		}
		log.fine(String.format("Tool#getUnusedEntityID - NewEntityID[%s]", result));
		return result;
	}

	public static boolean hasString(String text) {
		return text != null && 0 < text.trim().length();
	}

	public static boolean isClientSide(World world) {
		return world.isRemote;
	}

	public static boolean isForge() {
		return true; // Loader.isModLoaded("Forge");
	}

	public static boolean isServerSide(World world) {
		return !world.isRemote;
	}

	public static boolean isValidTileEntity(TileEntity ent) {
		TileEntity ent2 = ent.getWorldObj().getBlockTileEntity(ent.xCoord, ent.yCoord, ent.zCoord);
		if (ent2 != null && ent != ent2) { // 頻繁に迷い込んでくるので、もしやってきた場合は容赦なく invalidate
			ent.invalidate();
			log.fine(String.format("Tool#isValidTileEntity - InvalidateTileEntity[%s] %d, %d, %d", ent, ent.xCoord, ent.yCoord, ent.zCoord));
		}
		return !ent.isInvalid();
	}

	public static void showMessage(EntityPlayer player, String msg) {
		if (player != null && msg != null) {
			msg = msg.trim();
			if (0 < msg.length()) {
				player.addChatMessage(msg);
			}
		}
	}

	public static void showMessage(World world, String msg) {
		for (Object ent: world.loadedEntityList) {
			if (ent instanceof EntityPlayer) {
				showMessage((EntityPlayer) ent, msg);
			}
		}
	}

	public static void spawnExplodeParticle(World world, double x, double y, double z) {
		Random rand = world.rand;
		for (int j = 0; j < 20; j++) {
			double d = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			world.spawnParticle("explode", x + rand.nextFloat() * 2.0 - 1.0, y + rand.nextFloat(), z + rand.nextFloat() * 2.0 - 1.0, d, d1, d2);
		}
	}

	public static void spawnPortalParticle(World world, double x, double y, double z) {
		Random rand = world.rand;
		for (int i = 0; i < 3; i++) {
			double px = x + (rand.nextDouble() - 0.5D);
			double py = y + rand.nextDouble();
			double pz = z + (rand.nextDouble() - 0.5D);
			double velx = (rand.nextDouble() - 0.5D) * 2.0D;
			double vely = -rand.nextDouble();
			double velz = (rand.nextDouble() - 0.5D) * 2.0D;
			world.spawnParticle("portal", px, py, pz, velx, vely, velz);
		}
	}

	public static boolean tryUseItems(EntityPlayer player, Item item, boolean consumed) {
		ItemStack cnrrentItem = player.getCurrentEquippedItem();
		if (cnrrentItem != null && cnrrentItem.itemID == item.itemID) {
			if (consumed && !player.capabilities.isCreativeMode) {
				cnrrentItem.stackSize--;
				if (cnrrentItem.stackSize <= 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
			}
			return true;
		}
		return false;
	}
}
