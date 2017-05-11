package com.robcio.riverd.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.robcio.riverd.tower.Tower;

public class TextureManager {
	static private TextureAtlas atlas;
	static private Skin buttonSkin;
	public static void setAssetManager(AssetManager assets) {
		atlas = assets.get("images/packed/game.atlas", TextureAtlas.class);
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
	}
	
	public static Drawable createDrawable(String name){
		return buttonSkin.getDrawable(name);
	}
	
	public static Sprite createSprite(String name){
		return atlas.createSprite(name);
	}

	public static TextureRegion createTexReg(String name){
		return atlas.findRegion(name);
	}
	

	public static Sprite getTower(int type) {
		switch (type) {
		case Tower.Arrow:
			return atlas.createSprite("towerArrow");
		case Tower.Magic:
			return atlas.createSprite("towerMagic");
		case Tower.Lunar:
			return atlas.createSprite("towerLunar");
		case Tower.DoubleArrow:
			return atlas.createSprite("towerArrowDouble");
		case Tower.Icicle:
			return atlas.createSprite("towerIcicle");
		case Tower.GodArrow:
			return atlas.createSprite("towerGod");
		case Tower.Skull:
			return atlas.createSprite("towerSkull");
		default:
			return atlas.createSprite("towerLunar");
		}
	}

	public static TextureAtlas getAtlas() {
		return atlas;
	}
}
