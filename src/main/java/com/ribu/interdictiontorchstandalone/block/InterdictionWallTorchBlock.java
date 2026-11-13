package com.ribu.interdictiontorchstandalone.block;

import com.ribu.interdictiontorchstandalone.ModBlockEntities;
import com.ribu.interdictiontorchstandalone.ModParticles;
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
 * 贴墙形态的禁止火把。
 *
 * 朝向、贴墙判定与碰撞箱继承自 WallTorchBlock，另挂载力场方块实体。
 */
public class InterdictionWallTorchBlock extends WallTorchBlock implements EntityBlock {
	public InterdictionWallTorchBlock(SimpleParticleType flameParticle, BlockBehaviour.Properties properties) {
		super(flameParticle, properties);
	}

	/**
	 * 在把头顶端生成烟雾与火焰粒子。
	 *
	 * 水平方向沿朝向反方向偏移 0.27，高度偏移 0.7 与 0.22，速度为零，
	 * 与原版 WallTorchBlock.animateTick 相同。
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

	/** 仅在逻辑侧返回 ticker，客户端返回 null。 */
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : ModBlockEntities.ticker();
	}
}

