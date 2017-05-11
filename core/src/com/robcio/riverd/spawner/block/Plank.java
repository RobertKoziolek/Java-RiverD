package com.robcio.riverd.spawner.block;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;
import com.robcio.riverd.utils.ParticleManager;
import com.robcio.riverd.utils.TextureManager;

public class Plank implements BlockInterface {
	private boolean bodyDeleted;
	private final int width;
	private final int height;
	private Body body;
	private boolean dead = false, dying = false, damaged=false;
	private Sprite sprite;
	private Vector2 position;
	private int hp, effectTime;
	private ParticleEffect pe;

	public Plank(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		hp = 8 + (width / 17) * 27;
		body = BodyFactory.createBox(x, y, width, height, false, false, (short) Constants.BIT_BRICKS,
				(short) (Constants.BIT_BRICKS | Constants.BIT_WALLS | Constants.BIT_BULLETS));
		body.setUserData(this);
		sprite = TextureManager.createSprite("blockPlank");
		sprite.setOrigin(width / 2, height / 2);
		sprite.setSize(width, height);
		position = body.getPosition();

		pe = ParticleManager.createParticleEffect("plankDestroyed", width * 0.7f / 40, false);
	}

	public void update(float delta) {
		if (!dead && stray()) {
			dead = true;
		}
		if (effectTime > 0) {
			effectTime--;
		} else if (effectTime == 0) {
			body.setGravityScale(1f);
		}
		if (dying) {
			if (pe.isComplete()) {
				dead = true;
			} else {
				pe.update(delta);
			}
		}

		position = body.getPosition();
	}

	private void die() {
		if (!dying) {
			dying = true;
			ParticleManager.setPosition(pe, position);
			pe.start();
		}
	}

	private boolean stray() {
		int x = (int) (position.x * PPM);
		int y = (int) (position.y * PPM);
		if (x < -64 || x > Gdx.graphics.getWidth() + 64 || y < -10) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDead() {
		return dead;
	}

	public Body getBody() {
		return body;
	}

	public Body getBodyForDeletion() {
		if (bodyDeleted)
			return null;
		bodyDeleted=true;
		return body;
	}

	public void render(SpriteBatch batch) {
		if (dying) {
			pe.draw(batch);
		} else {
			float x = position.x * PPM - (width / 2);
			float y = position.y * PPM - (height / 2);
			sprite.setPosition(x, y);
			sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
			sprite.draw(batch);
		}
	}

	public boolean hit(int damage) {
		hp -= damage;
		if (hp < 1) {
			die();
			return true;
		} else if (!damaged && hp < 8){
			damaged=true;
			sprite = TextureManager.createSprite("blockPlankDamaged");
			sprite.setOrigin(width / 2, height / 2);
			sprite.setSize(width, height);
		}
		return false;
	}

	@Override
	public void hit(int damage, float gravityScale, int effectTime) {
		hp -= damage;
		if (hp < 1) {
			die();
		} else {
			this.effectTime = effectTime;
			body.setGravityScale(gravityScale);
		}
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public boolean shouldBodyBeRemoved() {
		return dying;
	}
}
