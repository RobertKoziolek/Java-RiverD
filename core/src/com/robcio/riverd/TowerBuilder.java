package com.robcio.riverd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.robcio.riverd.tower.Tower;
import com.robcio.riverd.tower.TowerManager;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;
import com.robcio.riverd.utils.SoundManager;
import com.robcio.riverd.utils.SoundManager.Sounds;
import com.robcio.riverd.utils.TextureManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TowerBuilder implements InputProcessor {
	static private final int minDistanceFromTowers = 20;
	static private final float minDistanceFromEdge = 7 / Constants.PPM;
	
	private Camera camera;
	private TowerManager towerManager;
	private Vector3 testPoint = new Vector3();
	private Vector2 towerPosition;
	private float visualAngle, realAngle;
	private int type, numberOfTypes;
	private Sprite sprite;
	private boolean building;

	public TowerBuilder(OrthographicCamera camera, TowerManager towerManager) {
		this.camera = camera;
		this.towerManager = towerManager;
		numberOfTypes = Tower.numberOfTypes;
		type = Tower.Arrow;
		sprite = TextureManager.getTower(type);
		sprite.setSize(32, 32);
		sprite.setOrigin(16, 16);
	}

	Body bodyThatWasHit;

	public boolean touchDown(final int x, final int y, int pointer, int newParam) {
		if (pointer == 0) {
			camera.unproject(testPoint.set(x, y, 0));
			bodyThatWasHit = null;//surface on map that is available for building on it
			//FIXME doesn't allow to build at surfaces' connection
			QueryCallback callback = new QueryCallback() {
				private boolean checkForBuildingSurface(Fixture fixture){
					if (fixture.getBody().getType() != BodyType.StaticBody){
						return false;
					}
					float xx = testPoint.x / Constants.PPM, yy = testPoint.y / Constants.PPM;
					List<Vector2> pointsAround = Arrays.asList(
							new Vector2(xx - minDistanceFromEdge, yy),
							new Vector2(xx + minDistanceFromEdge, yy),
							new Vector2(xx, yy + minDistanceFromEdge),
							new Vector2(xx, yy - minDistanceFromEdge));
					for (Vector2 vec : pointsAround){
						if (fixture.testPoint(vec)==false){
							return false;
						}
					}
					return true;
				}

				public boolean reportFixture(Fixture fixture) {
					if (checkForBuildingSurface(fixture)==false){
						return true;
					}
					Vector2 vec = new Vector2(testPoint.x, testPoint.y);
					for (Vector2 towerPosition : towerManager.getPositions()) {
						if (towerPosition.sub(vec).len() < minDistanceFromTowers) {
							return false;
						}
					}
					bodyThatWasHit = fixture.getBody();
					return false;
				}
			};
			BodyFactory.getWorld().QueryAABB(callback, 0, 0, 9, 16);

			if (newParam == Buttons.LEFT && bodyThatWasHit != null) {
				SoundManager.play(Sounds.BuildingStart);
				towerPosition = new Vector2(testPoint.x, testPoint.y);
				building = true;
				bodyThatWasHit = null;
			}
		} else if (pointer == 1) {
			changeTowerType();
		}
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == 0) {
			if (towerPosition != null) {
				camera.unproject(testPoint.set(screenX, screenY, 0));
				Vector2 angleEndPosition = new Vector2(testPoint.x, testPoint.y);

				double dx = towerPosition.x - angleEndPosition.x;
				double dy = towerPosition.y - angleEndPosition.y;
				realAngle = (float) Math.atan2(-dy, -dx);
			}
		}
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (pointer == 0 && towerPosition != null) {
			towerManager.createTower((int) towerPosition.x, (int) towerPosition.y, type, realAngle);
			SoundManager.play(Sounds.BuildingEnd);
			building = false;
			towerPosition = null;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		int building = -1;
		switch (keycode) {
		case Keys.Q:
			building = Tower.Arrow;
			break;
		case Keys.W:
			building = Tower.DoubleArrow;
			break;
		case Keys.E:
			building = Tower.GodArrow;
			break;
		case Keys.R:
			// building = Tower.Arrow;
			break;
		case Keys.A:
			building = Tower.Magic;
			break;
		case Keys.S:
			building = Tower.Icicle;
			break;
		case Keys.D:
			building = Tower.Lunar;
			break;
		case Keys.F:
			// building = Tower.Arrow;
			break;
		}
		if (building != -1) {
			changeTowerType(building);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	public void changeTowerType(int type) {
		this.type = type % numberOfTypes;
		changeSprite();
	}

	private void changeTowerType() {
		type = (type + 1) % numberOfTypes;
		changeSprite();
	}

	private void changeSprite() {
		sprite = TextureManager.getTower(type);
		sprite.setSize(32, 32);
		sprite.setOrigin(16, 16);
	}

	public void draw(SpriteBatch batch) {
		if (building) {
			sprite.setPosition(towerPosition.x - 16, towerPosition.y - 16);
			sprite.setRotation(MathUtils.radiansToDegrees * visualAngle - 45);
			sprite.draw(batch);
		}
	}

	public void update() {
		float totalRotation = realAngle - visualAngle;
		while (totalRotation < -180 * MathUtils.degreesToRadians)
			totalRotation += 360 * MathUtils.degreesToRadians;
		while (totalRotation > 180 * MathUtils.degreesToRadians)
			totalRotation -= 360 * MathUtils.degreesToRadians;
		float change = (totalRotation * 12) * MathUtils.degreesToRadians;
		float newAngle = Math.min(change, Math.max(-change, totalRotation));
		visualAngle += newAngle;
	}

	public void cancelBulding() {
		building = false;
		towerPosition = null;
	}
}
