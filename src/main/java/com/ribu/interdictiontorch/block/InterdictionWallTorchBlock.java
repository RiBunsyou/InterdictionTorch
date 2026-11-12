package com.ribu.interdictiontorch.block;

import com.ribu.interdictiontorch.ModBlockEntities;
import com.ribu.interdictiontorch.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 贴在墙上的禁止火把。
 * 朝向、贴墙判定、碰撞箱全部继承原版 WallTorchBlock。
 */
public class InterdictionWallTorchBlock extends WallTorchBlock implements EntityBlock {
	public InterdictionWallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
		super(flameParticle, properties);
	}

	/**
	 * 与原版 WallTorchBlock.animateTick 逐行一致：
	 * 沿朝向反方向偏移 0.27，高度 +0.7 再 +0.22，速度全 0。
	 */
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		Direction opposite = state.getValue(FACING).getOpposite();

		double x = pos.getX() + 0.5 + 0.27 * opposite.getStepX();
		double y = pos.getY() + 0.7 + 0.22;
		double z = pos.getZ() + 0.5 + 0.27 * opposite.getStepZ();

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
