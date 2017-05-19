package com.robcio.riverd.screens.game;

//import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.robcio.riverd.Map;
import com.robcio.riverd.RiverDMain;
import com.robcio.riverd.TowerBuilder;
import com.robcio.riverd.tower.Tower;
import com.robcio.riverd.utils.TextureManager;

public class TowerMenu implements GestureListener {
	// private Skin skin;
	private RiverDMain main;
	private Stage stage;
	private Table table, mainTable;
	private boolean shown = false;
	private TowerBuilder towerBuilder;
	private boolean slidePossible;
	private int height;

	public TowerMenu(TowerBuilder towerBuilder, RiverDMain main) {
		this.towerBuilder = towerBuilder;
		this.main = main;
		buildButtons();
	}

	private void buildButtons() {
		stage = new Stage(new FitViewport(RiverDMain.WIDTH, RiverDMain.HEIGHT, main.camera));
		table = new Table();
		table.setBackground(TextureManager.createDrawable("bgtile"));
		//TODO drawables into static class
		buttonMap("mapEasy1", Map.Easy1, false);
		buttonMap("mapHard1", Map.Hard1, true);

		table.row();

		buttonTower("towerArrow", Tower.Arrow, false);
		buttonTower("towerArrowDouble", Tower.DoubleArrow, false);
		buttonTower("towerGod", Tower.GodArrow, false);
		
		buttonTower("towerMagic", Tower.Magic, true);
		buttonTower("towerIcicle", Tower.Icicle, false);
		buttonTower("towerLunar", Tower.Lunar, false);
		buttonTower("towerSkull", Tower.Skull, false);
		
		
		mainTable = new Table();
		mainTable.add(table);
		mainTable.setFillParent(true);
		mainTable.addAction(alpha(0f));
		hideMenu();
		stage.addActor(mainTable);
	}

	private void buttonTower(String drawable, final int type, boolean right) {

		ImageButton button = new ImageButton(TextureManager.createDrawable(drawable));
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				towerBuilder.cancelBulding();
				towerBuilder.changeTowerType(type);
				hideMenu();
			}
		});

		float buttonSize = RiverDMain.WIDTH / 7;
		table.add(button).width(buttonSize).pad(9f).height(buttonSize);
		if (right) {
			table.row();
		}
	}

	private void buttonMap(String drawable, final String type, boolean right) {

		ImageButton button = new ImageButton(TextureManager.createDrawable(drawable));
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				// sequence(fadeOut(1f),
				stage.addAction(run(new Runnable() {
					public void run() {
						Map.currentMap = type;
						main.setScreen(main.getSplashScreen());
					}
				}));
			}
		});

		float buttonSize = RiverDMain.WIDTH / 4;
		table.add(button).width(buttonSize).pad(21f).height(buttonSize * 2).colspan(2);
		if (right) {
			table.row();
		}
	}

	public Stage getStage() {
		return stage;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
			if (slidePossible && shown == false && mainTable.hasActions() == false) {
				if (velocityY < -1600f) {
					mainTable.addAction(
							parallel(fadeIn(0.5f, Interpolation.pow2), moveTo(0, 0, 0.5f, Interpolation.pow2)));
					shown = true;
					return true;
				}
			}
		return false;
	}

	private void hideMenu() {
		mainTable.addAction(
				parallel(fadeOut(0.5f, Interpolation.pow2), moveTo(0, -RiverDMain.HEIGHT, 0.5f, Interpolation.pow2)));
		shown = false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (shown) {
			float xywh[] = new float[4];
			xywh[0]=table.getX();
			xywh[1]=table.getY();
			xywh[2]=table.getWidth();
			xywh[3]=table.getHeight();
			if (!(x>xywh[0] && x<xywh[0]+xywh[2] && y>xywh[1] && y<xywh[1]+xywh[3])){
				hideMenu();
			}
			towerBuilder.cancelBulding();
			return true;
		}
		slidePossible = false;
		if (y > height * 7 / 8) {
			slidePossible = true;
		}
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}

	public void resize(int width, int height) {
		this.height = height;
		stage.getViewport().update(width, height, true);
	}

}
