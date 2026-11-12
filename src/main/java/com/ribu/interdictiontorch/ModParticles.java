package com.ribu.interdictiontorch;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

/**
 * 自定义粒子类型。和原版火焰粒子的区别：紫色烟雾、向上飘、逐渐淡出。
 *
 * 注意：SimpleParticleType 的构造器是 protected，只能靠子类来 new。
 */
public final class ModParticles {
	private ModParticles() {
	}

	/** 仅用于绕过 protected 构造器。 */
	private static final class FlameType extends SimpleParticleType {
		private FlameType() {
			super(false);
		}
	}

	public static final ResourceKey<ParticleType<?>> INTERDICTION_FLAME_KEY =
			ResourceKey.create(Registries.PARTICLE_TYPE, InterdictionTorch.id("interdiction_flame"));

	public static final SimpleParticleType INTERDICTION_FLAME = Registry.register(
			BuiltInRegistries.PARTICLE_TYPE,
			INTERDICTION_FLAME_KEY,
			new FlameType());

	public static void initialize() {
		// 触发静态初始化
	}
}
