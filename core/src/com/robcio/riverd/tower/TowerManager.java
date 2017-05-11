package com.robcio.riverd.tower;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TowerManager {
	private ArrayList<Tower> towerList;

	public TowerManager() {
		towerList = new ArrayList<Tower>();
	}

	public void update(float delta) {
		for (Tower tower : towerList) {
			tower.update(delta);
		}
	}

	public void clean() {
		for (Tower tower : towerList) {
			tower.clean();
		}
	}

	public void render(SpriteBatch batch,float delta) {
		for (Tower tower : towerList) {
			tower.render(batch);
		}
		for (Tower tower : towerList) {
			tower.renderProjectiles(batch);
		}
		for (Tower tower : towerList) {
			tower.renderParticles(batch,  delta);
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void createTower(int x, int y, int type, float angle) {
		towerList.add(new Tower(x, y, type, angle));
	}

	public void clear() {
		for (Tower tower : towerList) {
			tower.clear();
		}
		towerList.clear();
	}

	public int getNumberOfProjectiles() {
		int i = 0;
		for (Tower tower : towerList) {
			i += tower.getProjectileListSize();
		}
		return i;
	}
	
	public ArrayList<Vector2> getPositions(){
		ArrayList<Vector2> list = new ArrayList<Vector2>();
		for (Tower tower : towerList){
			list.add(tower.getPosition());
		}
		return list;
	}

}
