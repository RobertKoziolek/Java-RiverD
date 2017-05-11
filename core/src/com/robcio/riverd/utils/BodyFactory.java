package com.robcio.riverd.utils;

import static com.robcio.riverd.utils.Constants.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyFactory {
	private final static float gravity = -9.8f;
	private final static boolean doSleep = false;
	private static World world = new World(new Vector2(0, gravity), doSleep);

	public static World getWorld() {
		return world;
	}

	public static World getClearWorld() {
		world.dispose();
		world = new World(new Vector2(0, gravity), doSleep);
		return world;
	}

	public static Body createBox(int x, int y, int width, int height, boolean isStatic, boolean canRotate, short cbits,
			short mbits) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / PPM / 2, height / PPM / 2);

		return getBody(x, y, shape, isStatic, canRotate, cbits, mbits);
	}

	public static Body createCircle(int x, int y, int radius, boolean isStatic, boolean canRotate, short cbits,
			short mbits) {
		CircleShape shape = new CircleShape();
		shape.setRadius(radius / PPM);
		return getBody(x, y, shape, isStatic, canRotate, cbits, mbits);
	}

	public static Body createOval(int x, int y, float radius1, float radius2, boolean isStatic, boolean canRotate,
			short cbits, short mbits) {
		PolygonShape shape = new PolygonShape();
		Vector2 vertices[] = new Vector2[8];
		for (int i = 0; i < 8; ++i)
			vertices[i] = new Vector2();
		radius1 /= PPM;
		radius2 /= PPM;
		float dent = 0.7f;
		vertices[0].set(-radius1, 0);
		vertices[1].set(dent * -radius1, dent * radius2);
		vertices[2].set(0, radius2);
		vertices[3].set(dent * radius1, dent * radius2);
		vertices[4].set(radius1, 0);
		vertices[5].set(dent * radius1, dent * -radius2);
		vertices[6].set(0, -radius2);
		vertices[7].set(dent * -radius1, dent * -radius2);
		shape.set(vertices);
		return getBody(x, y, shape, isStatic, canRotate, cbits, mbits);
	}

	public static Body createArrow(int x, int y, float scale, boolean isStatic, boolean canRotate, short cbits,
			short mbits) {
		PolygonShape shape = new PolygonShape();
		Vector2 vertices[] = new Vector2[4];
		for (int i = 0; i < 4; ++i)
			vertices[i] = new Vector2();
		vertices[0].set(-0.4f * scale, 0 * scale);
		vertices[1].set(0.1f * scale, -0.025f * scale);
		vertices[2].set(0.4f * scale, 0 * scale);
		vertices[3].set(0.1f * scale, 0.025f * scale);
		shape.set(vertices);
		return getBody(x, y, shape, isStatic, canRotate, cbits, mbits);
	}

	private static Body getBody(int x, int y, Shape shape, boolean isStatic, boolean canRotate, short cbits,
			short mbits) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = canRotate;
		bodyDef.position.set(x / PPM, y / PPM);
		if (isStatic) {
			bodyDef.type = BodyType.StaticBody;
		} else {
			bodyDef.type = BodyType.DynamicBody;
		}
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = cbits;
		fixtureDef.filter.maskBits = mbits;
		Body tmpBody = world.createBody(bodyDef);
		tmpBody.createFixture(fixtureDef);
		shape.dispose();
		return tmpBody;
	}
}
