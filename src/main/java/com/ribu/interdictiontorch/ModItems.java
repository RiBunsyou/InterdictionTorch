package com.ribu.interdictiontorch;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;

/**
 * 物品注册表。只有一件物品：放地上是火把，点墙上是墙上的火把。
 */
public final class ModItems {
	private ModItems() {
	}

	public static final ResourceKey<Item> INTERDICTION_TORCH_KEY =
			ResourceKey.create(Registries.ITEM, InterdictionTorch.id("interdiction_torch"));

	public static final Item INTERDICTION_TORCH = register(INTERDICTION_TORCH_KEY,
			new StandingAndWallBlockItem(
					ModBlocks.INTERDICTION_TORCH,
					ModBlocks.INTERDICTION_WALL_TORCH,
					Direction.DOWN,
					new Item.Properties().setId(INTERDICTION_TORCH_KEY)));

	/**
	 * 和原版 Items.registerBlock 一样的注册流程。
	 *
	 * 关键在 registerBlocks：它把方块写进 Item.BY_BLOCK 映射，
	 * 展示框/物品实体等地方会通过 Block.byItem 反查方块并改用 3D 方块模型。
	 * 少了这一步，物品只会用平面的 item 模型，展示框里就会是"一张纸片"。
	 */
	private static Item register(ResourceKey<Item> key, Item item) {
		if (item instanceof BlockItem blockItem) {
			blockItem.registerBlocks(Item.BY_BLOCK, item);
		}

		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
				.register(output -> output.accept(INTERDICTION_TORCH));
	}
}
