package com.robcio.riverd.tower;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.robcio.riverd.tower.projectile.Arrow;
import com.robcio.riverd.tower.projectile.Bolt;
import com.robcio.riverd.tower.projectile.Icicle;
import com.robcio.riverd.tower.projectile.Moon;
import com.robcio.riverd.tower.projectile.ProjectileInterface;
import com.robcio.riverd.utils.TextureManager;

public class Tower {
	public static final int numberOfTypes = 7;
	
	public static final int Arrow = 0;
	public static final int Magic = 1;
	public static final int Lunar = 2;
	public static final int DoubleArrow = 3;
	public static final int Icicle = 4;
	public static final int GodArrow = 5;
	public static final int Skull = 6;

	private static final int speed[] = {22,5,3,17,19,6,3};

	final private int x, y, type;
	private float time = 0f, angle, accuracy = 0.37f;
	private Sprite sprite;
	private ArrayList<ProjectileInterface> bulletList, bulletToDelete;

	public Tower(int x, int y, int type, float angle) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.angle = angle;
		sprite = TextureManager.getTower(type);
		sprite.setRotation(MathUtils.radiansToDegrees * angle - 45);
		sprite.setPosition(x - 16, y - 16);
		sprite.setSize(32, 32);
		sprite.setOrigin(16, 16);

		bulletList = new ArrayList<ProjectileInterface>();
		bulletToDelete = new ArrayList<ProjectileInterface>();
	}

	public void update(float delta) {
		time += delta;
		if (time > 4f / speed[type]) {
			time = 0f;
			shoot();
		}
		for (ProjectileInterface bullet : bulletList) {
			bullet.update(delta);
		}
	}

///E:/JavaWorkspaceFinal/riverd/images/starparticle.png
	public void shoot() {
		ProjectileInterface projectile;
		float tmpAngle = angle + (MathUtils.random() - 0.5f) * accuracy;
		switch (type) {
		case Arrow:
			projectile = new Arrow(x, y, tmpAngle, 7);
			break;
		case Magic:
			projectile = new Bolt(x, y, tmpAngle);
			break;
		case Lunar:
			projectile = new Moon(x, y, tmpAngle);
			break;
		case DoubleArrow:
			projectile = new Arrow(x, y, tmpAngle - 0.05f, 5);
			bulletList.add(projectile);
			projectile = new Arrow(x, y, tmpAngle, 5);
			break;
		case Icicle:
			projectile = new Icicle(x, y, tmpAngle, 8);
			break;
		case GodArrow:
			projectile = new Arrow(x, y, tmpAngle, 38,1.5f);
			break;
		default:
			projectile = new Arrow(x, y, tmpAngle, 10);
			break;
		}
		bulletList.add(projectile);
	}

	public void clean() {
		for (ProjectileInterface bullet : bulletList) {
			if (bullet.isDead()) {
				bulletToDelete.add(bullet);
			}
		}
		bulletList.removeAll(bulletToDelete);
		for (ProjectileInterface bullet : bulletToDelete) {
			bullet.clear();
		}
		bulletToDelete.clear();
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void clear() {
		clean();
		for (ProjectileInterface projectile : bulletList) {
			projectile.clear();
		}
	}

	public void renderProjectiles(SpriteBatch batch) {
		for (ProjectileInterface bullet : bulletList) {
			bullet.render(batch);
		}
	}

	public void renderParticles(SpriteBatch batch,float delta) {
		for (ProjectileInterface bullet : bulletList) {
			bullet.renderParticles(batch,  delta);
		}
	}
	
	public Vector2 getPosition(){
		return new Vector2(x,y);
	}

	public int getProjectileListSize() {
		return bulletList.size();
	}
}
