package com.robcio.riverd.spawner;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpawnerManager {
	private ArrayList<Spawner> spawnerList;

	public SpawnerManager() {
		spawnerList = new ArrayList<Spawner>();
	}

	public void createSpawnerAt(int x, int y) {
		spawnerList.add(new Spawner(x, y));
	}

	public void changeType() {
		for (Spawner spawner : spawnerList) {
			spawner.changeType();
		}
	}

	public void spawn() {
		for (Spawner spawner : spawnerList) {
			spawner.spawn();
		}
	}

	private float time, changeTypeTime;

	public void update(float delta) {
		time += delta;
		changeTypeTime += delta;
		if (changeTypeTime > 7f) {
			changeTypeTime = 0;
			changeType();
		}
		if (time > 1.5f) {
			time = 0;
			spawn();
		}
		for (Spawner spawner : spawnerList) {
			spawner.update(delta);
		}
	}

	public void clean() {
		for (Spawner spawner : spawnerList) {
			spawner.clean();
		}
	}

	public void clearBricks() {
		for (Spawner spawner : spawnerList) {
			spawner.clearBricks();
		}
	}

	public void render(SpriteBatch batch) {
		for (Spawner spawner : spawnerList) {
			spawner.render(batch);
		}
	}
}
