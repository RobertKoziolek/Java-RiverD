package com.robcio.riverd.tower.projectile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.robcio.riverd.utils.BodyFactory;

public class BodyWelder {
	private Body bodyA, bodyB;
	private float angle;

	public BodyWelder(Body bodyA, Body bodyB, float angle) {
		this.bodyA = bodyA;
		this.bodyB = bodyB;
		this.angle=angle;
	}

	public void weld() {
		Vector2 anchor = bodyA.getWorldPoint(new Vector2(0.001f, 0));

		WeldJointDef weldJointDef = new WeldJointDef();
		weldJointDef.bodyA = bodyA;
		weldJointDef.bodyB = bodyB;
		Vector2 vec = bodyA.getLocalPoint(anchor);
		weldJointDef.localAnchorA.set(vec);
		vec = bodyB.getLocalPoint(anchor);
		weldJointDef.localAnchorB.set(vec);
		
		weldJointDef.referenceAngle = angle;
		BodyFactory.getWorld().createJoint(weldJointDef);
	}
}
