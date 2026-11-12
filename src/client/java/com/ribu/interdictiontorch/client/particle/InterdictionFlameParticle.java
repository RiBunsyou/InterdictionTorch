package com.ribu.interdictiontorch.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.RandomSource;

/**
 * 禁止火把的火焰粒子。
 *
 * 实现方式和原版 FlameParticle 完全一致：
 * - 继承 RisingParticle，拿到原版的摩擦、速度衰减、位置抖动、寿命随机
 * - 覆写 move()：只平移包围盒，不做碰撞
 * - 覆写 getQuadSize()：随寿命二次缩小到一半
 * - 覆写 getLightCoords()：叠加方块发光
 * - 层用 OPAQUE
 * 唯一区别是贴图颜色（紫色）和注册的粒子类型。
 */
public class InterdictionFlameParticle extends RisingParticle {
	protected InterdictionFlameParticle(ClientLevel level, double x, double y, double z,
										double vx, double vy, double vz, TextureAtlasSprite sprite) {
		super(level, x, y, z, vx, vy, vz, sprite);
	}

	/** 原版的关键：不做碰撞，粒子不会被火把的碰撞箱挤出去。 */
	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().move(x, y, z));
		this.setLocationFromBoundingbox();
	}

	@Override
	public float getQuadSize(float partialTick) {
		float progress = (this.age + partialTick) / (float) this.lifetime;
		return this.quadSize * (1.0f - progress * progress * 0.5f);
	}

	/** 和原版一样随寿命发光，暗处也能看见。 */
	@Override
	public int getLightCoords(float partialTick) {
		return LightCoordsUtil.addSmoothBlockEmission(
				super.getLightCoords(partialTick),
				(this.age + partialTick) / (float) this.lifetime);
	}

	@Override
	protected SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.OPAQUE;
	}

	/** 客户端粒子工厂。 */
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level,
									   double x, double y, double z,
									   double vx, double vy, double vz,
									   RandomSource random) {
			return new InterdictionFlameParticle(level, x, y, z, vx, vy, vz, this.sprites.get(random));
		}
	}
}
