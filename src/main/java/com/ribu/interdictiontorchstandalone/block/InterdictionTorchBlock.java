package com.ribu.interdictiontorchstandalone.block;

import com.ribu.interdictiontorchstandalone.ModBlockEntities;
import com.ribu.interdictiontorchstandalone.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 落地形态的禁止火把。
 *
 * 碰撞箱、支撑判定与放置逻辑继承自 TorchBlock，另挂载力场方块实体。
 */
public class InterdictionTorchBlock extends TorchBlock implements EntityBlock {
	public InterdictionTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
		super(flameParticle, properties);
	}

	/**
	 * 在把头顶端生成烟雾与火焰粒子。
	 *
	 * 生成位置为方块中心、高度 0.7，速度为零，与原版 TorchBlock.animateTick 相同。
	 */
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		double x = pos.getX() + 0.5;
		double y = pos.getY() + 0.7;
		double z = pos.getZ() + 0.5;

		level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
		level.addParticle(ModParticles.INTERDICTION_FLAME, x, y, z, 0.0, 0.0, 0.0);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InterdictionTorchBlockEntity(pos, state);
	}

	/** 仅在逻辑侧返回 ticker，客户端返回 null。 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : ModBlockEntities.ticker();
	}
}

