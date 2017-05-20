package com.robcio.riverd.utils.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.robcio.riverd.spawner.SpawnerManager;
import com.robcio.riverd.utils.Constants;

public class TiledMapHelper {
	public static void parseTileMapLayerCollisions(MapObjects mapObjects) {
		for (MapObject object : mapObjects) {
			Shape shape;
			if (object instanceof PolygonMapObject) {
				shape = createPolygon((PolygonMapObject) object);
			} else if (object instanceof PolylineMapObject) {
				shape = createPolyLine((PolylineMapObject) object);
			} else {
				continue;
			}
			new Wall(shape);
			shape.dispose();
		}
	}
	public static void parseTileMapLayerBuildingSpace(MapObjects mapObjects) {
		for (MapObject object : mapObjects) {
			Shape shape;
			if (object instanceof PolygonMapObject) {
				shape = createPolygon((PolygonMapObject) object);
			} else {
				continue;
			}
			new BuildingSpace(shape);
			shape.dispose();
		}
	}

	public static void parseTileMapLayerWallSensor(MapObjects mapObjects) {
		for (MapObject object : mapObjects) {
			Shape shape;
			if (object instanceof PolylineMapObject) {
				shape = createPolyLine((PolylineMapObject) object);
			} else {
				continue;
			}
			new WallSensor(shape);
			shape.dispose();
		}
	}

	public static void parseTileMapLayerSpawners(MapObjects mapObjects, SpawnerManager spawnerManager) {
		for (MapObject object : mapObjects) {
			if (object instanceof PolylineMapObject) {
				float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
				Vector2[] worldVertices = new Vector2[vertices.length / 2];
				for (int i = 0; i < worldVertices.length; ++i) {
					worldVertices[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);
				}
				for (int i = 0; i < worldVertices.length; ++i) {
					int x = (int) worldVertices[i].x;
					int y = (int) worldVertices[i].y;
					spawnerManager.createSpawnerAt(x, y);
				}
			} else {
				continue;
			}
		}
	}

	private static Shape createPolygon(PolygonMapObject object) {
		float[] vertices = object.getPolygon().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		for (int i = 0; i < worldVertices.length; ++i) {
			worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
		}
		PolygonShape shape = new PolygonShape();
		shape.set(worldVertices);

		return shape;
	}

	private static ChainShape createPolyLine(PolylineMapObject polyLineObject) {
		float[] vertices = polyLineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		for (int i = 0; i < worldVertices.length; ++i) {
			worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
		}
		ChainShape shape = new ChainShape();
		shape.createChain(worldVertices);

		return shape;
	}

}
