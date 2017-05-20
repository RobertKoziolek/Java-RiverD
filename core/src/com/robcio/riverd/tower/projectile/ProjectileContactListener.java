package com.robcio.riverd.tower.projectile;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.robcio.riverd.spawner.block.Ball;
import com.robcio.riverd.spawner.block.BlockInterface;
import com.robcio.riverd.spawner.block.Egg;
import com.robcio.riverd.spawner.block.Plank;
import com.robcio.riverd.utils.map.Wall;

public class ProjectileContactListener implements ContactListener {
	private ArrayList<BodyWelder> welderList = new ArrayList<BodyWelder>();

	@Override
	public void beginContact(Contact contact) {

	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		if (fixtureA.getBody().getUserData() instanceof ProjectileInterface) {
			if (fixtureB.isSensor()) {
				ProjectileInterface ifA = (ProjectileInterface) fixtureA.getBody().getUserData();
				ifA.passFirstWall();
			}
		} else if (fixtureB.getBody().getUserData() instanceof ProjectileInterface) {
			if (fixtureA.isSensor()) {
				ProjectileInterface ifB = (ProjectileInterface) fixtureB.getBody().getUserData();
				ifB.passFirstWall();
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		if (fixtureA == null || fixtureB == null)
			return;
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();

		if (check(bodyA.getUserData(), bodyB.getUserData())) {
			if (weld(bodyA, bodyB))
			 contact.setEnabled(false);
		} else if (check(bodyB.getUserData(), bodyA.getUserData())) {
			if (weld(bodyB, bodyA))
			 contact.setEnabled(false);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	private boolean weld(Body bodyA, Body bodyB) {
		if (bodyA.getUserData() instanceof Arrow) {
			Arrow arrow = (Arrow) bodyA.getUserData();
			if (arrow.isAttacking()) {
				if (bodyB.getUserData() instanceof Wall) {
				} else {
					BlockInterface block = (BlockInterface) bodyB.getUserData();
					if (block.hit(arrow.attack())){
						return true;
					} 
				}
				arrow.looseEdge();
				float angle = bodyB.getAngle() - bodyA.getAngle();
				welderList.add(new BodyWelder(bodyA, bodyB, angle));
			}
		} else if (bodyA.getUserData() instanceof Icicle) {
			Icicle icicle = (Icicle) bodyA.getUserData();
			if (icicle.isAttacking()) {
				if (bodyB.getUserData() instanceof Wall) {
					icicle.attack();
				} else {
					BlockInterface block = (BlockInterface) bodyB.getUserData();
					block.hit(icicle.attack());
				}
				icicle.die(null);
				float angle = bodyB.getAngle() - bodyA.getAngle();
				welderList.add(new BodyWelder(bodyA, bodyB, angle));
			}
		} else if (bodyA.getUserData() instanceof Bolt) {
			if (bodyB.getUserData() instanceof Wall) {
				((Bolt) bodyA.getUserData()).die(null);
			} else {
				BlockInterface block = (BlockInterface) bodyB.getUserData();
				((Bolt) bodyA.getUserData()).die(block.getPosition());
				block.hit(0, -1.5f, 100);
			}
		} else if (bodyA.getUserData() instanceof Moon) {
			if (bodyB.getUserData() instanceof Wall) {
				// ((Moon) bodyA.getUserData()).die(null);
			} else {
				// BlockInterface block = (BlockInterface) bodyB.getUserData();
				// ((Moon) bodyA.getUserData()).die(block);
				// block.hit(0, -1.5f, 100);
			}
		}
		return false;
	}

	private boolean check(final Object userDataA, final Object userDataB) {
		return ((userDataA instanceof Bolt || userDataA instanceof Arrow || userDataA instanceof Moon
				|| userDataA instanceof Icicle)
				&& (userDataB instanceof Plank || userDataB instanceof Ball || userDataB instanceof Egg || userDataB instanceof Wall));
	}

	public void weldEverythingWaiting() {
		if (welderList.isEmpty() == false) {
			for (BodyWelder welder : welderList) {
				welder.weld();
			}
			welderList.clear();
		}
	}
}
