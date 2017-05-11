package com.robcio.riverd;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.robcio.riverd.spawner.SpawnerManager;
import com.robcio.riverd.tower.TowerManager;
import com.robcio.riverd.utils.map.TiledMapHelper;

public class Map {
	// private World world;
	public static final String Easy1 = "maps/hard_second.tmx";
	public static final String Hard1 = "maps/hard_third.tmx";
	
	public static String currentMap = Hard1;
	private SpawnerManager spawnerManager;
	private TowerManager towerManager;

	private TiledMap tiledMap;

	public Map() {
		spawnerManager = new SpawnerManager();
		towerManager = new TowerManager();
		this.tiledMap = new TmxMapLoader().load(currentMap);
		TiledMapHelper.parseTileMapLayerCollisions(this.tiledMap.getLayers().get("collision").getObjects());
		TiledMapHelper.parseTileMapLayerWallSensor(this.tiledMap.getLayers().get("wallsensor").getObjects());
		TiledMapHelper.parseTileMapLayerSpawners(this.tiledMap.getLayers().get("spawners").getObjects(), spawnerManager);
	}

	public void update(float delta) {
		spawnerManager.update(delta);
		towerManager.update(delta);
		clean();
	}

	private void clean() {
		spawnerManager.clean();
		towerManager.clean();
	}

	public void clearBricks() {
		spawnerManager.clearBricks();
	}

	public void renderBricksAndTowers(SpriteBatch batch,float delta) {
		spawnerManager.render(batch);
		towerManager.render(batch,  delta);	
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public TowerManager getTowerManager() {
		return towerManager;
	}

	public void clearTowers() {
		towerManager.clear();
	}
}
