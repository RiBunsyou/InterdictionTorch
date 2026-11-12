package com.ribu.interdictiontorch.block;

import com.ribu.interdictiontorch.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * 禁止火把的力场。
 *
 * 推力不是"每 tick 加一点加速度"，而是每 tick 保证一个最小向外速度：
 * 这样怪物自己的行走速度（约 0.23 格/tick）永远盖不过力场，
 * 不会出现"僵尸慢慢踱步挤进来"的情况。
 */
public class InterdictionTorchBlockEntity extends BlockEntity {
	/** 作用半径（格）。 */
	public static final double RADIUS = 8.0;
	/** 力场边缘处要求的最小向外速度（格/tick）。 */
	private static final double PUSH_SPEED_EDGE = 0.35;
	/** 火把正中心处要求的最小向外速度（格/tick）。 */
	private static final double PUSH_SPEED_CENTER = 0.75;
	/** 靠近火把时给的向上初速度，让怪物被顶起来。 */
	private static final double HOP_SPEED = 0.16;

	public InterdictionTorchBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.INTERDICTION_TORCH, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, InterdictionTorchBlockEntity be) {
		if (level.isClientSide()) {
			return;
		}

		Vec3 center = Vec3.atCenterOf(pos);
		AABB area = new AABB(pos).inflate(RADIUS);

		for (Entity entity : level.getEntitiesOfClass(Entity.class, area)) {
			if (entity instanceof Monster) {
				pushAway(entity, center);
			} else if (entity instanceof Projectile) {
				deflect(entity, center);
			}
		}
	}

	/**
	 * 保证怪物有一个"至少这么大"的向外速度。
	 *
	 * 因为每 tick 都会检查，怪物一旦进入范围就会被持续往外推，
	 * 无论它自己怎么往前走、被别的怪怎么挤，都推不进来。
	 */
	private static void pushAway(Entity entity, Vec3 center) {
		Vec3 offset = entity.position().subtract(center);
		Vec3 horizontal = new Vec3(offset.x, 0.0, offset.z);
		double distance = horizontal.length();

		if (distance > RADIUS) {
			return;
		}

		Vec3 direction = distance < 0.05 ? new Vec3(1.0, 0.0, 0.0) : horizontal.normalize();

		// 越靠近中心，要求的向外速度越大
		double falloff = 1.0 - distance / RADIUS;
		double targetSpeed = PUSH_SPEED_EDGE + (PUSH_SPEED_CENTER - PUSH_SPEED_EDGE) * falloff;

		// 当前速度在"向外方向"上的分量
		double outwardSpeed = entity.getDeltaMovement().dot(direction);

		if (outwardSpeed < targetSpeed) {
			double missing = targetSpeed - outwardSpeed;
			entity.push(direction.x * missing, 0.0, direction.z * missing);
		}

		// 贴地且离得近时，顶一下让它离地
		if (entity.onGround() && distance < RADIUS * 0.6) {
			entity.push(0.0, HOP_SPEED, 0.0);
		}
	}

	/** 弹射物：把朝向火把的速度分量反射出去，表现为"被弹开"。 */
	private static void deflect(Entity projectile, Vec3 center) {
		Vec3 motion = projectile.getDeltaMovement();

		if (motion.lengthSqr() < 1.0E-4) {
			return;
		}

		Vec3 offset = projectile.position().subtract(center);
		double distance = offset.length();

		if (distance > RADIUS || distance < 0.01) {
			return;
		}

		Vec3 normal = offset.normalize();
		double approach = motion.dot(normal);

		// 只在"正在靠近火把"时反射，已经飞走的不再干扰
		if (approach >= 0) {
			return;
		}

		Vec3 reflected = motion.subtract(normal.scale(2.0 * approach)).scale(0.6);
		projectile.setDeltaMovement(reflected);
	}
}
