package com.robcio.riverd.utils;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.ScaledNumericValue;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleManager {
	static public ParticleEffect createParticleEffect(String name, boolean start) {
		ParticleEffect pe = new ParticleEffect();
		pe.load(Gdx.files.internal("particles/" + name + ".fx"), TextureManager.getAtlas());
		pe.setEmittersCleanUpBlendFunction(false);
		if (start)
			pe.start();
		return pe;
	}

	static public ParticleEffect createParticleEffect(String name, float scale, boolean start) {
		ParticleEffect pe = createParticleEffect(name, start);
		pe.scaleEffect(scale);
		return pe;
	}

	static public void rotateDeg(ParticleEffect pe, float angle) {
		for (ParticleEmitter em : pe.getEmitters()) {
			ScaledNumericValue emitterAngle = em.getAngle();
			float angleHighMin = emitterAngle.getHighMin();
			float angleHighMax = emitterAngle.getHighMax();
			float spanHigh = angleHighMax - angleHighMin;
			emitterAngle.setHigh(angle - spanHigh / 2.0f, angle + spanHigh / 2.0f);

			float angleLowMin = emitterAngle.getLowMin();
			float angleLowMax = emitterAngle.getLowMax();
			float spanLow = angleLowMax - angleLowMin;
			emitterAngle.setLow(angle - spanLow / 2.0f, angle + spanLow / 2.0f);
		}
	}

	static public void rotateRad(ParticleEffect pe, float angle) {
		float angleDeg = MathUtils.radiansToDegrees * angle;
		rotateDeg(pe, angleDeg);
	}

	public static void setPosition(ParticleEffect pe, Vector2 position) {
		for (ParticleEmitter em : pe.getEmitters()) {
			em.setPosition(position.x * PPM, position.y * PPM);
		}
	}

	public static void setContinuous(ParticleEffect pe, boolean isContinuous) {
		for (ParticleEmitter em : pe.getEmitters()) {
			em.setContinuous(isContinuous);
		}
	}
}
