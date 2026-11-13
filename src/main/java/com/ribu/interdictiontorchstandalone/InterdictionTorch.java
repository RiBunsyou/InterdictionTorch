package com.ribu.interdictiontorchstandalone;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 模组主入口。
 */
public class InterdictionTorch implements ModInitializer {
	public static final String MOD_ID = "interdictiontorchstandalone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** 构造本模组命名空间下的标识符。 */
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModItems.initialize();

		LOGGER.info("Interdiction Torch loaded.");
	}
}

