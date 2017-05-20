package com.robcio.riverd.tower.projectile;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;
import com.robcio.riverd.utils.TextureManager;

public class Arrow implements ProjectileInterface {
	private final int width = 123 / 5;
	private final int height = 16 / 5;
	private final float scale = width / 15;

	private Body body;
	private boolean dead = false, attacking = true;
	private int timeToLive = 210, damage;

	private Sprite sprite;
	private Vector2 position;

	public Arrow(int x, int y, float angle, int damage, float scale) {

		this.damage = damage;
		body = BodyFactory.createArrow(x, y, scale, false, false, Constants.COLL_BULLETS,
				(short) (Constants.COLL_BRICKS | Constants.COLL_BULLETPROOF | Constants.COLL_WALLSENSOR));
		body.setBullet(true);
		body.getFixtureList().get(0).setDensity(0.5f);
		body.resetMassData();
		body.setUserData(this);

		position = body.getPosition();

		body.setTransform(position, angle);

		if (scale != 1f) {
			sprite = TextureManager.createSprite("projectileArrowGod");
			sprite.setOrigin(width / 2, height / 2);
			sprite.setSize(width * scale, height * scale);
			body.applyLinearImpulse(new Vector2(0.2f * scale * 2, 0).rotateRad(angle), position, false);
		}
	}

	public Arrow(int x, int y, float angle, int damage) {
		this(x, y, angle, damage, 1f);
		sprite = TextureManager.createSprite("projectileArrow");
		sprite.setOrigin(width / 2, height / 2);
		sprite.setSize(width, height);
		body.applyLinearImpulse(new Vector2(0.2f, 0).rotateRad(angle), position, false);
	}

	public void update(float delta) {
		if (!dead) {
			if (timeToLive-- < 0 || stray()) {
				dead = true;
				return;
			}
			applyDrag();
			position = body.getPosition();
		}
	}

	private boolean stray() {
		int x = (int) (position.x * PPM);
		int y = (int) (position.y * PPM);
		if (x < -10 || x > Gdx.graphics.getWidth() + 10 || y < -10 || y > Gdx.graphics.getHeight() + 10) {
			return true;
		} else {
			return false;
		}
	}

	private void applyDrag() {
		Vector2 pointingDirection = body.getWorldVector(new Vector2(1, 0));
		Vector2 flightDirection = body.getLinearVelocity().nor();
		float flightSpeed = (float) Math.sqrt(Math.pow(flightDirection.x, 2) + Math.pow(flightDirection.y, 2));

		float dot = pointingDirection.dot(flightDirection);
		float dragConstant = 0.45f;

		float dragForceMagnitude = (1 - Math.abs(dot)) * flightSpeed * flightSpeed * dragConstant * body.getMass();

		Vector2 tailPosition = body.getWorldPoint(new Vector2(-1.4f, 0));

		flightDirection.x *= -dragForceMagnitude;
		flightDirection.y *= -dragForceMagnitude;

		body.applyForce(flightDirection, tailPosition, false);
	}

	public boolean isDead() {
		return dead;
	}

	public Body getBody() {
		return body;
	}

	public int attack() {
		int buffer = damage;
		damage -= 10;
		if (damage > 0) {
			return buffer;
		}
		looseEdge();
		return buffer;
	}

	public void looseEdge() {
		Filter filter = new Filter();
		filter.categoryBits = 0;
		filter.maskBits = 0;
		filter.groupIndex = 0;

		body.getFixtureList().get(0).setFilterData(filter);
		attacking = false;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void passFirstWall() {
		if (attacking == true) {
			Filter filter = new Filter();

			filter.categoryBits = Constants.COLL_BULLETS;
			filter.maskBits = (short) (Constants.COLL_BRICKS | Constants.COLL_BULLETPROOF | Constants.COLL_WALLS);
			filter.groupIndex = 0;

			body.getFixtureList().get(0).setFilterData(filter);
		}
	}

	public float getScale() {
		return scale;
	}

	public void render(SpriteBatch batch) {
		float x = position.x * PPM - (width / 2);
		float y = position.y * PPM - (height / 2);
		sprite.setPosition(x, y);
		sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
		sprite.draw(batch);
	}

	public void renderParticles(SpriteBatch batch, float delta) {
	}

	@Override
	public void clear() {
		BodyFactory.getWorld().destroyBody(body);
	}
}
