package com.ribu.interdictiontorch.block;

import com.ribu.interdictiontorch.ModBlockEntities;
import com.ribu.interdictiontorch.ModParticles;
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
 * 立在地上的禁止火把。
 * 方块行为完全继承原版 TorchBlock，只额外挂了一个力场 BlockEntity。
 */
public class InterdictionTorchBlock extends TorchBlock implements EntityBlock {
	public InterdictionTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
		super(flameParticle, properties);
	}

	/**
	 * 与原版 TorchBlock.animateTick 逐行一致：
	 * 位置 (x+0.5, y+0.7, z+0.5)，速度全 0，每 tick 一颗烟雾 + 一颗火焰。
	 * 唯一的区别是火焰粒子换成了自定义的紫色类型。
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

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : ModBlockEntities.ticker();
	}
}
