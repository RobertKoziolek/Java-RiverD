package com.robcio.riverd.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.robcio.riverd.RiverDMain;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
//import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class SplashScreen implements Screen, InputProcessor {
	private final RiverDMain main;
	private Stage stage;
	private boolean centered;

	private Image splashImage;

	public SplashScreen(final RiverDMain main) {
		this.main = main;
		this.stage = new Stage(new FitViewport(RiverDMain.WIDTH, RiverDMain.HEIGHT, main.getCamera()));
	}

	@Override
	public void show() {
		centered = false;
		stage.clear();
		Gdx.input.setInputProcessor(this);

		initializeSplash();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		stage.draw();
	}

	private void update(float delta) {
		stage.act(delta);
		if (splashImage.hasActions() == false) {
			if (centered) {
				main.setScreen(main.getGameScreen());
			} else {
				centerSplash();
			}
		}
	}

	private void initializeSplash() {
		Texture splashTex = main.getAssets().get("images/riverdlogo.png", Texture.class);
		splashImage = new Image(splashTex);
		splashImage.setPosition(stage.getWidth() / 2 - 70, stage.getHeight());
		splashImage.addAction(sequence(alpha(0f), scaleTo(.1f, .1f),
				parallel(fadeIn(2f, Interpolation.pow2), scaleTo(0.7f, 0.7f, 2.5f, Interpolation.pow5),
						moveTo(stage.getWidth() / 2 - 140, stage.getHeight() / 2 - 105, 2f, Interpolation.swing))));
		stage.addActor(splashImage);
	}
	private void centerSplash() {
		centered = true;
		splashImage.clearActions();
		splashImage.addAction(parallel(fadeIn(0.5f),
				sequence(scaleTo(0.7f, 0.7f, 0f), moveTo(stage.getWidth() / 2 - 140, stage.getHeight() / 2 - 105, 0f),
						delay(1.5f), fadeOut(1.25f, Interpolation.pow3))));
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
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

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (centered) {
			main.setScreen(main.getGameScreen());
		} else {
			centerSplash();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
