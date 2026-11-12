package com.ribu.interdictiontorch.client;

import com.ribu.interdictiontorch.ModParticles;
import com.ribu.interdictiontorch.client.particle.InterdictionFlameParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.PendingParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;

public class InterdictionTorchClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PendingParticleProvider<SimpleParticleType> provider = InterdictionFlameParticle.Provider::new;
		ParticleProviderRegistry.getInstance().register(ModParticles.INTERDICTION_FLAME, provider);
	}
}
