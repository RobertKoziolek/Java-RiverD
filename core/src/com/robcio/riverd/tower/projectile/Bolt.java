package com.robcio.riverd.tower.projectile;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;
import com.robcio.riverd.utils.ParticleManager;

public class Bolt implements ProjectileInterface {
	private final int radius = 4;
	private Body body;
	private boolean dead = false, dying = false;

	private Vector2 position;
	private Vector2 punchPosition;
	private ParticleEffect pe, pePunch;

	public Bolt(int x, int y, float angle) {
		pe = ParticleManager.createParticleEffect("magic", 0.3f, true);
		ParticleManager.rotateRad(pe, angle);
		pePunch = ParticleManager.createParticleEffect("magicHit", 0.2f, false);

		body = BodyFactory.createCircle(x, y, radius, false, false, Constants.COLL_BULLETS,
				(short) (Constants.COLL_BRICKS | Constants.COLL_BULLETPROOF | Constants.COLL_WALLSENSOR));
		body.setBullet(true);
		body.setUserData(this);
		body.setGravityScale(0f);
		position = body.getPosition();

		body.setTransform(position, angle);
		body.applyLinearImpulse(new Vector2(0.2f, 0).rotateRad(angle), position, false);
	}

	public void update(float delta) {
		if (!dead && stray()) {
			die(null);
		}
		position = body.getPosition();
		ParticleManager.setPosition(pe, position);
		pe.update(delta);
		if (punchPosition != null) {
			ParticleManager.setPosition(pePunch, punchPosition);
		}
		pePunch.update(delta);
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

	public Body getBody() {
		return body;
	}

	public void passFirstWall() {
		Filter filter = new Filter();

		filter.categoryBits = Constants.COLL_BULLETS;
		filter.maskBits = (short) (Constants.COLL_BRICKS | Constants.COLL_BULLETPROOF | Constants.COLL_WALLS);
		filter.groupIndex = 0;

		body.getFixtureList().get(0).setFilterData(filter);
	}

	public void render(SpriteBatch batch) {
	}

	public void renderParticles(SpriteBatch batch,float delta) {
		pe.draw(batch,  delta);
		pePunch.draw(batch,  delta);
	}

	public void die(Vector2 punchBlock) {
		this.punchPosition = punchBlock;
		dead = true;
		ParticleManager.setContinuous(pe, false);
		ParticleManager.setPosition(pePunch, position);
		if (dying == false) {
			pePunch.start();
			dying = true;
		}
	}

	public boolean isDead() {
		if (dead && pe.isComplete() && pePunch.isComplete()) {
			pe.dispose();
			pePunch.dispose();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		BodyFactory.getWorld().destroyBody(body);
		pe.dispose();
		pePunch.dispose();
	}
}
