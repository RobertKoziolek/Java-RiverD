package com.robcio.riverd.spawner;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.robcio.riverd.spawner.block.Ball;
import com.robcio.riverd.spawner.block.BlockInterface;
import com.robcio.riverd.spawner.block.Egg;
import com.robcio.riverd.spawner.block.Plank;
import com.robcio.riverd.utils.BodyFactory;

public class Spawner {
	private final int CAPACITY = 22;

	static private final int PlankSmall = 0;
	static private final int PlankBig = 1;
	static private final int PlankMega = 2;
	static private final int BallSmall = 3;
	static private final int BallBig = 4;
	static private final int BallMega = 5;
	static private final int Egg = 6;
	static private final int EggBig = 7;

	private int x, y,type = BallMega;
	private ArrayList<BlockInterface> brickList = new ArrayList<BlockInterface>();
	private HashSet<BlockInterface> bricksToDelete = new HashSet<BlockInterface>();

	public Spawner(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void update(float delta) {
		for (BlockInterface brick : brickList) {
			brick.update(delta);
		}
	}

	public void changeType() {
		 type = ++type % 8;
	}

	public void spawn() {
		if (brickList.size() > CAPACITY) {
			return;
		}
		switch (type) {
		case PlankSmall:
			brickList.add(new Plank(x, y, 16, 6));
			break;
		case PlankBig:
			brickList.add(new Plank(x, y, 40, 9));
			break;
		case PlankMega:
			brickList.add(new Plank(x, y, 70, 18));
			changeType();
			break;
		case BallSmall:
			brickList.add(new Ball(x, y, 8));
			break;
		case BallBig:
			brickList.add(new Ball(x, y, 13));
			break;
		case BallMega:
			brickList.add(new Ball(x, y, 21));
			changeType();
			break;
		case Egg:
			brickList.add(new Egg(x, y, 6, 12));
			brickList.add(new Egg(x, y, 6, 12));
			break;
		case EggBig:
			brickList.add(new Egg(x, y, 14, 27));
			break;
		}

	}

	public void clean() {
		for (BlockInterface brick : brickList) {
			if (brick.shouldBodyBeRemoved()) {
				try {
					BodyFactory.getWorld().destroyBody(brick.getBodyForDeletion());
				} catch (Exception e) {
				}
			}
			if (brick.isDead()) {
				bricksToDelete.add(brick);
			}
		}
		brickList.removeAll(bricksToDelete);
		for (BlockInterface brick : bricksToDelete) {
			try {
				BodyFactory.getWorld().destroyBody(brick.getBodyForDeletion());
			} catch (Exception e) {
			}
		}
		bricksToDelete.clear();
	}

	public void clearBricks() {// tylko do debugu
		for (BlockInterface brick : brickList) {
			try {
				BodyFactory.getWorld().destroyBody(brick.getBodyForDeletion());
			} catch (Exception e) {
			}
		}
		brickList.clear();
		bricksToDelete.clear();
	}

	public void render(SpriteBatch batch) {
		for (BlockInterface brick : brickList) {
			brick.render(batch);
		}
	}
}
