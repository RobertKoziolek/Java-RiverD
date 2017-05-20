package com.robcio.riverd.utils.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.robcio.riverd.utils.BodyFactory;
import com.robcio.riverd.utils.Constants;

public class BuildingSpace {
	private static int index= 0 ;
	public BuildingSpace(Shape shape) {
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		body = BodyFactory.getWorld().createBody(def);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constants.COLL_BUILDINGSPACE;
		fixtureDef.filter.maskBits = Constants.COLL_BULLETS;
		body.createFixture(fixtureDef);
		body.setUserData(this);
	}

}
