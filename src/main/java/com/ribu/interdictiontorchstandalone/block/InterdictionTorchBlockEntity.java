package com.ribu.interdictiontorchstandalone.block;

import com.ribu.interdictiontorchstandalone.ModBlockEntities;
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
 * 每个逻辑 tick 处理一次半径内的实体：敌对生物获得不低于目标值的向外速度，
 * 靠近火把的弹射物反射其速度分量。
 */
public class InterdictionTorchBlockEntity extends BlockEntity {
	/** 作用半径，单位为格。 */
	public static final double RADIUS = 12.0;
	/** 力场边缘的向外目标速度，单位为格每 tick。 */
	private static final double PUSH_SPEED_EDGE = 0.50;
	/** 火把中心的向外目标速度，单位为格每 tick。 */
	private static final double PUSH_SPEED_CENTER = 1.05;
	/** 靠近火把时施加的向上速度，单位为格每 tick。 */
	private static final double HOP_SPEED = 0.20;

	public InterdictionTorchBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.INTERDICTION_TORCH, pos, state);
	}

	/** 力场的主循环。 */
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
	 * 使敌对生物获得不低于目标值的向外速度。
	 *
	 * 目标速度随距离线性变化，中心为 PUSH_SPEED_CENTER，边缘为 PUSH_SPEED_EDGE。
	 * 每 tick 补齐一次差额，因此该速度不受生物自身移动与相互挤压的影响。
	 */
	private static void pushAway(Entity entity, Vec3 center) {
		Vec3 offset = entity.position().subtract(center);
		Vec3 horizontal = new Vec3(offset.x, 0.0, offset.z);
		double distance = horizontal.length();

		if (distance > RADIUS) {
			return;
		}

		Vec3 direction = distance < 0.05 ? new Vec3(1.0, 0.0, 0.0) : horizontal.normalize();

		double falloff = 1.0 - distance / RADIUS;
		double targetSpeed = PUSH_SPEED_EDGE + (PUSH_SPEED_CENTER - PUSH_SPEED_EDGE) * falloff;

		double outwardSpeed = entity.getDeltaMovement().dot(direction);

		if (outwardSpeed < targetSpeed) {
			double missing = targetSpeed - outwardSpeed;
			entity.push(direction.x * missing, 0.0, direction.z * missing);
		}

		if (entity.onGround() && distance < RADIUS * 0.6) {
			entity.push(0.0, HOP_SPEED, 0.0);
		}
	}

	/** 反射弹射物速度中朝向火把的分量。 */
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

		if (approach >= 0) {
			return;
		}

		Vec3 reflected = motion.subtract(normal.scale(2.0 * approach)).scale(0.6);
		projectile.setDeltaMovement(reflected);
		// setDeltaMovement 只改服务端数值，不会触发速度同步包，必须手动标记
		projectile.needsSync = true;
	}
}

