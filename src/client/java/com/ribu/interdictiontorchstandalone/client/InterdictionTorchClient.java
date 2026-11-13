package com.ribu.interdictiontorchstandalone.client;

import com.ribu.interdictiontorchstandalone.InterdictionTorch;
import com.ribu.interdictiontorchstandalone.ModParticles;
import com.ribu.interdictiontorchstandalone.client.particle.InterdictionFlameParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.PendingParticleProvider;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;

/**
 * 客户端入口。
 *
 * 注册火焰粒子的工厂，并注册内置高清资源包。
 */
public class InterdictionTorchClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PendingParticleProvider<SimpleParticleType> provider = InterdictionFlameParticle.Provider::new;
		ParticleProviderRegistry.getInstance().register(ModParticles.INTERDICTION_FLAME, provider);

		registerBuiltinPacks();
	}

	/**
	 * 注册模组内置资源包。
	 *
	 * 资源包内容位于 jar 内的 resourcepacks/hd 目录，激活类型 NORMAL 表示默认关闭，
	 * 玩家可在资源包列表中自行启用。
	 */
	private static void registerBuiltinPacks() {
		FabricLoader.getInstance().getModContainer(InterdictionTorch.MOD_ID).ifPresent(container ->
				ResourceLoader.registerBuiltinPack(
						InterdictionTorch.id("hd"),
						container,
						Component.translatable("resourcepack.interdictiontorchstandalone.hd"),
						PackActivationType.NORMAL));
	}
}
