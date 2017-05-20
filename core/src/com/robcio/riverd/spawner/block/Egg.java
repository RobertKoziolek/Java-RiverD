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

public class Egg implements BlockInterface {
	private boolean bodyDeleted;
	private final int radius1,radius2;
	private Body body;
	private boolean dead = false, dying = false;
	private Sprite sprite;
	private Vector2 position;
	private int hp, effectTime;
	private ParticleEffect pe;

	public Egg(int x, int y, int radius1,int radius2) {
		this.radius1 = radius1;
		this.radius2=radius2;
		this.hp = 8 + ((radius1+radius2) / 9) * 36;
		body = BodyFactory.createOval(x, y, radius1, radius2, false, false, Constants.COLL_BRICKS,
				(short) (Constants.COLL_BRICKS | Constants.COLL_WALLS | Constants.COLL_BULLETS));
		body.getFixtureList().get(0).setRestitution(0.7f);
		body.setUserData(this);
		
		sprite = TextureManager.createSprite("blockBall" + MathUtils.random(4));
		sprite.setOrigin(radius1,radius2);
		sprite.setSize(radius1 * 2, radius2 * 2);
		
		position = body.getPosition();

		pe = ParticleManager.createParticleEffect("ballDestroyed", 1f * radius1 / 21, false);
	}

	public void render(SpriteBatch batch) {
		if (dying) {
			pe.draw(batch);
		} else {
			float x = position.x * PPM - (radius1);
			float y = position.y * PPM - (radius2);
			sprite.setPosition(x, y);
			sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
			sprite.draw(batch);
		}
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
		bodyDeleted = true;
		return body;
	}


	public boolean hit(int damage) {
		hp -= damage;
		if (hp < 1) {
			die();
			return true;
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

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public boolean shouldBodyBeRemoved() {
		return dying;
	}
}
