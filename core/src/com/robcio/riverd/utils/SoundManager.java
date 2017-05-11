package com.robcio.riverd.utils;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	static public class Sounds {
		public static final int ArrowFired = 0;
		public static final int BoltFired = 1;
		public static final int ArrowHit = 2;
		public static final int BoltHit = 3;
		public static final int BuildingStart = 4;
		public static final int BuildingEnd = 5;
	}

	static private HashMap<Integer, Sound> hash = new HashMap<Integer, Sound>();

	public static void setAssetManager(AssetManager assets) { 
		for (int i = 0; i < 6; ++i) {
			switch (i) {
			case 0:
				hash.put(i, assets.get("sound/arrow.wav", Sound.class));
				break;
			case 1:
				hash.put(i, assets.get( "sound/arrowhit.wav", Sound.class));
				break;
			case 2:
				hash.put(i, assets.get("sound/arrow.wav", Sound.class));
				break;
			case 3:
				hash.put(i, assets.get("sound/arrow.wav", Sound.class));
				break;
			case 4:
				hash.put(i, assets.get("sound/building.wav", Sound.class));
				break;
			case 5:
				hash.put(i, Gdx.audio.newSound(Gdx.files.internal("sound/building2.wav")));
				break;
			}
		}
//	hash.put(i, Gdx.audio.newSound(Gdx.files.internal("sound/arrow.wav")));
	}

	public static void play(int sound) {
		hash.get(sound).play(0.5f);
	}

//	public static void dispose() {
//		for (Sound sound : hash.values()) {
//			sound.dispose();
//		}
//	}

}
