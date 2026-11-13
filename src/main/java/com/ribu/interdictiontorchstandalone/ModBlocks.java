package com.ribu.interdictiontorchstandalone;

import com.ribu.interdictiontorchstandalone.block.InterdictionTorchBlock;
import com.ribu.interdictiontorchstandalone.block.InterdictionWallTorchBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

/**
 * 方块注册。
 *
 * 两个方块的属性与原版火把相同：无碰撞、瞬间破坏、光照等级 14、木质音效、被推动时破坏。
 * 墙面款复用落地款的战利品表与翻译键（对应原版 wallVariant）。
 */
public final class ModBlocks {
	private ModBlocks() {
	}

	public static final ResourceKey<Block> INTERDICTION_TORCH_KEY =
			ResourceKey.create(Registries.BLOCK, InterdictionTorch.id("interdiction_torch"));
	public static final ResourceKey<Block> INTERDICTION_WALL_TORCH_KEY =
			ResourceKey.create(Registries.BLOCK, InterdictionTorch.id("interdiction_wall_torch"));

	/** 落地形态的禁止火把。 */
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

	/** 贴墙形态的禁止火把，掉落物与名称继承落地款。 */
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

	/** 触发类初始化。 */
	public static void initialize() {
	}
}

