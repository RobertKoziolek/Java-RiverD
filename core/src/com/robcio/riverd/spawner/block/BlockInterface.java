package com.robcio.riverd.spawner.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface BlockInterface {
	public void update(float delta);

	public boolean isDead();

	public Body getBody();

	public void render(SpriteBatch batch);

	public boolean hit(int damage);

	public void hit(int damage, float gravityScale, int effectTime);

	public Vector2 getPosition();

	public boolean shouldBodyBeRemoved();

	public Body getBodyForDeletion();
}
