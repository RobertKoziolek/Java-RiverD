package com.robcio.riverd.utils.map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;

public class WallSensor {

	public WallSensor(Shape shape) {
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		body = BodyFactory.getWorld().createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.BIT_WALLSENSOR;
		fixtureDef.filter.maskBits = Constants.BIT_BULLETS;
		fixtureDef.isSensor=true;
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}

}
