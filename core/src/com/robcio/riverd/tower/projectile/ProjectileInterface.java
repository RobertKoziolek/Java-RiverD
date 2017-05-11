package com.robcio.riverd.tower.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public interface ProjectileInterface {
	public void update(float delta);

	public boolean isDead();

	public Body getBody();

	public void render(SpriteBatch batch);

	public void clear();

	public void passFirstWall();

	public void renderParticles(SpriteBatch batch, float delta);
}
