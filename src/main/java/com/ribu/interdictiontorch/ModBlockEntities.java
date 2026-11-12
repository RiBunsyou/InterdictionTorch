package com.ribu.interdictiontorch;

import com.ribu.interdictiontorch.block.InterdictionTorchBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

/**
 * 方块实体类型注册。
 *
 * BlockEntityType 由构造器创建，接收方块实体工厂和可挂载的方块集合。
 */
public final class ModBlockEntities {
	private ModBlockEntities() {
	}

	public static final ResourceKey<BlockEntityType<?>> INTERDICTION_TORCH_KEY =
			ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, InterdictionTorch.id("interdiction_torch"));

	public static final BlockEntityType<InterdictionTorchBlockEntity> INTERDICTION_TORCH =
			Registry.register(
					BuiltInRegistries.BLOCK_ENTITY_TYPE,
					INTERDICTION_TORCH_KEY,
					new BlockEntityType<>(
							InterdictionTorchBlockEntity::new,
							Set.of(ModBlocks.INTERDICTION_TORCH, ModBlocks.INTERDICTION_WALL_TORCH)));

	/** 返回力场使用的 ticker。 */
	public static <T extends BlockEntity> BlockEntityTicker<T> ticker() {
		return (level, pos, state, blockEntity) -> {
			if (blockEntity instanceof InterdictionTorchBlockEntity torch) {
				InterdictionTorchBlockEntity.serverTick(level, pos, state, torch);
			}
		};
	}

	/** 触发类初始化。 */
	public static void initialize() {
	}
}
