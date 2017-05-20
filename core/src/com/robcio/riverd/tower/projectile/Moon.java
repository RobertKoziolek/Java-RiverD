package com.robcio.riverd.tower.projectile;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;
import com.robcio.riverd.utils.ParticleManager;
import com.robcio.riverd.utils.TextureManager;

public class Moon implements ProjectileInterface {
	private final int radius = 15, blastRadius = 3;
	private final float blastPower = 2f;
	private Body body;
	private boolean dead = false, exploding = false;
	private float timeToLive = 2f;

	private Sprite sprite;
	private Vector2 position;
	private ParticleEffect pe, peExplode, peShot;
	private boolean exploded;

	public Moon(int x, int y, float angle) {
		pe = ParticleManager.createParticleEffect("moonstars", 0.5f, true);
		peExplode = ParticleManager.createParticleEffect("moonstarsHit", .5f, false);
		peShot = ParticleManager.createParticleEffect("moonstarsShot", 0.3f, true);
		ParticleManager.rotateRad(pe, angle);
		ParticleManager.rotateRad(peShot, angle);

		body = BodyFactory.createCircle(x, y, radius, false, false, Constants.COLL_BULLETS,
				(short) (Constants.COLL_BULLETPROOF | Constants.COLL_WALLSENSOR));
		body.setBullet(true);
		body.setUserData(this);
		body.setGravityScale(0.7f);
		body.getFixtureList().get(0).setRestitution(1f);
		position = body.getPosition();

		ParticleManager.setPosition(peShot, position);

		body.setTransform(position, angle);
		body.applyLinearImpulse(new Vector2(6.7f, 0).rotateRad(angle), position, false);

		sprite = TextureManager.createSprite("projectileMoon");
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOrigin(radius, radius);
	}

	public void update(float delta) {
		timeToLive -= delta;
		sprite.setAlpha(timeToLive > 1 ? 1 : timeToLive);
		if (timeToLive < 0f) {
			die();
		}
		if (!dead && stray()) {
			die();
		}
		position = body.getPosition();
		ParticleManager.setPosition(pe, position);
		pe.update(delta);
		peShot.update(delta);
		if (exploding) {
			peExplode.update(delta);

			if (!exploded && peExplode.getEmitters().get(0).getPercentComplete() > 0.8f) {
				explode();
				exploded = true;
			}
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
		if (dead == false && exploding == false) {
			float x = position.x * PPM - (radius);
			float y = position.y * PPM - (radius);
			sprite.setPosition(x, y);
			sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
			sprite.draw(batch);
		}
	}

	public void renderParticles(SpriteBatch batch,float delta) {
		peShot.draw(batch,  delta);
		pe.draw(batch,  delta);
		peExplode.draw(batch,  delta);
	}

	public void die() {
		dead = true;
		ParticleManager.setContinuous(pe, false);
		ParticleManager.setPosition(peExplode, position);
		if (exploding == false) {
			BodyFactory.getWorld().destroyBody(body);
			peExplode.start();
			ParticleManager.setPosition(peExplode, position);
			exploding = true;
		}
	}

	private void explode() {
		QueryCallback callback = new QueryCallback() {

			public boolean reportFixture(Fixture fixture) {
				Body body = fixture.getBody();
				if (new Vector2(body.getWorldCenter()).sub(position).len() < blastRadius) {
					applyBlastImpulse(body, position, body.getPosition(), blastPower);
				}
				return true;
			}

		};
		World w = BodyFactory.getWorld();
		w.QueryAABB(callback, position.x - blastRadius, position.y - blastRadius, position.x + blastRadius,
				position.y + blastRadius);
	}

	private void applyBlastImpulse(Body body, Vector2 blastCenter, Vector2 applyPoint, float blastPower) {
		Vector2 blastDir = new Vector2(applyPoint);
		blastDir.sub(blastCenter).nor();
		float distance = blastDir.len();
		if (distance == 0)
			return;
		float invDistance = 1 / distance;
		float impulseMag = blastPower * invDistance * invDistance;
		blastDir.x *= impulseMag;
		blastDir.y *= impulseMag;
		body.applyLinearImpulse(blastDir, applyPoint, false);
	}

	public boolean isDead() {
		if (dead && pe.isComplete() && peExplode.isComplete()) {
			pe.dispose();
			peExplode.dispose();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void clear() {
		if (!dead) {
			BodyFactory.getWorld().destroyBody(body);
		}
		peShot.dispose();
		pe.dispose();
		peExplode.dispose();
	}
}
