package com.ribu.interdictiontorch.client;

import com.ribu.interdictiontorch.ModParticles;
import com.ribu.interdictiontorch.client.particle.InterdictionFlameParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.PendingParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * 客户端入口。
 *
 * 为禁止火把的火焰粒子类型注册粒子工厂。
 */
public class InterdictionTorchClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PendingParticleProvider<SimpleParticleType> provider = InterdictionFlameParticle.Provider::new;
		ParticleProviderRegistry.getInstance().register(ModParticles.INTERDICTION_FLAME, provider);
	}
}
