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
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemWithMetadata extends ItemBlock {
	public ItemWithMetadata(int id) {
		super(id);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getIconFromDamage(int damage) {
		return Block.blocksList[getBlockID()].getBlockTextureFromSideAndMetadata(2, damage);
	}
	
	@Override
	public String getItemNameIS(ItemStack item) {
		return super.getItemName() + "/" + item.getItemDamage();
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
}