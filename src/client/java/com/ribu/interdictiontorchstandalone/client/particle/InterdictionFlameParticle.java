package com.ribu.interdictiontorchstandalone.client.particle;

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
 * 继承自 RisingParticle，摩擦、速度衰减、位置抖动与寿命随机均由父类提供。
 * 覆写内容与原版 FlameParticle 相同：无碰撞移动、随寿命二次缩小、随寿命发光、OPAQUE 渲染层。
 * 差异仅为贴图颜色与注册的粒子类型。
 */
public class InterdictionFlameParticle extends RisingParticle {
	protected InterdictionFlameParticle(ClientLevel level, double x, double y, double z,
										double vx, double vy, double vz, TextureAtlasSprite sprite) {
		super(level, x, y, z, vx, vy, vz, sprite);
	}

	/** 只平移包围盒，跳过方块碰撞检测。 */
	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().move(x, y, z));
		this.setLocationFromBoundingbox();
	}

	/** 随寿命按二次曲线缩小，结束时为初始大小的一半。 */
	@Override
	public float getQuadSize(float partialTick) {
		float progress = (this.age + partialTick) / (float) this.lifetime;
		return this.quadSize * (1.0f - progress * progress * 0.5f);
	}

	/** 随寿命叠加方块发光。 */
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

