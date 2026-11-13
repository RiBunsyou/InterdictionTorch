package com.ribu.interdictiontorchstandalone;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;

/**
 * 粒子类型注册。
 */
public final class ModParticles {
	private ModParticles() {
	}

	/** SimpleParticleType 的构造器为 protected，通过子类实例化。 */
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

	/** 触发类初始化。 */
	public static void initialize() {
	}
}

