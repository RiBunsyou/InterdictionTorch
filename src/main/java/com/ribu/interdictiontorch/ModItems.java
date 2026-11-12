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
 * 物品注册。
 *
 * 物品为 StandingAndWallBlockItem：点击地面放置落地款，点击墙面放置贴墙款。
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
	 * 注册物品。
	 *
	 * 方块物品会先把方块写入 Item.BY_BLOCK 映射，展示框与物品实体据此选择方块模型。
	 */
	private static Item register(ResourceKey<Item> key, Item item) {
		if (item instanceof BlockItem blockItem) {
			blockItem.registerBlocks(Item.BY_BLOCK, item);
		}

		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	/** 将物品加入创造模式物品栏。 */
	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
				.register(output -> output.accept(INTERDICTION_TORCH));
	}
}
