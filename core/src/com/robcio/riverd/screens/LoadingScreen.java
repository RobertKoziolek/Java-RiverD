package com.robcio.riverd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.robcio.riverd.RiverDMain;
import com.robcio.riverd.utils.SoundManager;
import com.robcio.riverd.utils.TextureManager;

public class LoadingScreen implements Screen {

	private RiverDMain main;

	private ShapeRenderer shapeRenderer;
	private float progress;

	public LoadingScreen(RiverDMain main) {
		this.main = main;
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void show() {
		queueAssets();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		
		float height = RiverDMain.HEIGHT;
		float width = RiverDMain.WIDTH;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.rect(0, 0, width, height*progress);
		shapeRenderer.end();

	}

	private void update(float delta) {
		progress = main.assets.getProgress();
		if (main.assets.update()) {
			TextureManager.setAssetManager(main.assets);
			SoundManager.setAssetManager(main.assets);
			main.setScreen(main.getSplashScreen());
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

	private void queueAssets() {
		main.assets.load("images/riverdlogo.png", Texture.class);

		main.assets.load("images/packed/game.atlas", TextureAtlas.class);
		main.assets.load("ui/uiskin.atlas", TextureAtlas.class);

		main.assets.load("sound/arrow.wav", Sound.class);
		main.assets.load("sound/arrowhit.wav", Sound.class);
		main.assets.load("sound/building.wav", Sound.class);
		main.assets.load("sound/building2.wav", Sound.class);
	}
}
