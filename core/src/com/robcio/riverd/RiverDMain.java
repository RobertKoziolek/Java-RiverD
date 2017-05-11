package com.robcio.riverd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.robcio.riverd.screens.LoadingScreen;
import com.robcio.riverd.screens.SplashScreen;
import com.robcio.riverd.screens.game.GameScreen;

public class RiverDMain extends Game {
	public static final int WIDTH = 9 * 32;
	public static final int HEIGHT = 16 * 32;
	public static final String TITLE = "RiverD";
	public static final float VERSION = 0.1f;

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public AssetManager assets;

	public LoadingScreen loadingScreen;
	public SplashScreen splashScreen;
	public GameScreen gameScreen;
	public BitmapFont font;

	@Override
	public void create() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 10, 10 * (WIDTH / HEIGHT));

		assets = new AssetManager();
		loadingScreen = new LoadingScreen(this);
		splashScreen = new SplashScreen(this);
		gameScreen = new GameScreen(this);

		font = new BitmapFont();
		this.setScreen(loadingScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		loadingScreen.dispose();
		splashScreen.dispose();
		gameScreen.dispose();

		// System.out.println(batch.maxSpritesInBatch);// XXX debug max sprites
		// in
		// batch
		batch.dispose();
		assets.dispose();
		font.dispose();
	}
}
