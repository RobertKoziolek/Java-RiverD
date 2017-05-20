package com.robcio.riverd.utils.map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;

public class Wall {

	public Wall(Shape shape) {

		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		body = BodyFactory.getWorld().createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.COLL_WALLS;
		fixtureDef.filter.maskBits = Constants.COLL_OTHER | Constants.COLL_BRICKS | Constants.COLL_BULLETS | Constants.COLL_WALLSENSOR;
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}

}
