package com.ribu.interdictiontorch;

import com.ribu.interdictiontorch.block.InterdictionTorchBlock;
import com.ribu.interdictiontorch.block.InterdictionWallTorchBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

/**
 * 方块注册表。
 *
 * 属性链与原版 Blocks.TORCH / WALL_TORCH 逐项一致：
 *   of().noCollision().instabreak().lightLevel(s -> 14).sound(WOOD).pushReaction(POPPED)
 *
 * 墙面款还额外套用了原版的 wallVariant()：
 *   复用地面款的战利品表 + 复用地面款的翻译键
 */
public final class ModBlocks {
	private ModBlocks() {
	}

	public static final ResourceKey<Block> INTERDICTION_TORCH_KEY =
			ResourceKey.create(Registries.BLOCK, InterdictionTorch.id("interdiction_torch"));
	public static final ResourceKey<Block> INTERDICTION_WALL_TORCH_KEY =
			ResourceKey.create(Registries.BLOCK, InterdictionTorch.id("interdiction_wall_torch"));

	/** 立在地上的禁止火把，属性与原版火把完全相同。 */
	public static final Block INTERDICTION_TORCH = Registry.register(
			BuiltInRegistries.BLOCK,
			INTERDICTION_TORCH_KEY,
			new InterdictionTorchBlock(
					ModParticles.INTERDICTION_FLAME,
					BlockBehaviour.Properties.of()
							.setId(INTERDICTION_TORCH_KEY)
							.noCollision()
							.instabreak()
							.lightLevel(state -> 14)
							.sound(SoundType.WOOD)
							.pushReaction(PushReaction.POPPED)));

	/** 贴在墙上的禁止火把，等价于原版的 wallVariant(TORCH, true)。 */
	public static final Block INTERDICTION_WALL_TORCH = Registry.register(
			BuiltInRegistries.BLOCK,
			INTERDICTION_WALL_TORCH_KEY,
			new InterdictionWallTorchBlock(
					ModParticles.INTERDICTION_FLAME,
					BlockBehaviour.Properties.of()
							.setId(INTERDICTION_WALL_TORCH_KEY)
							.overrideLootTable(INTERDICTION_TORCH.getLootTable())
							.overrideDescription(INTERDICTION_TORCH.getDescriptionId())
							.noCollision()
							.instabreak()
							.lightLevel(state -> 14)
							.sound(SoundType.WOOD)
							.pushReaction(PushReaction.POPPED)));

	public static void initialize() {
		// 触发静态初始化
	}
}
