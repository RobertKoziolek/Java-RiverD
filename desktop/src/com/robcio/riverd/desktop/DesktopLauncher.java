package com.robcio.riverd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.robcio.riverd.RiverDMain;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Settings settings = new Settings();
		settings.maxWidth = 512;
		settings.maxHeight = 512;
		TexturePacker.process(settings, "E:\\Projekty\\RiverDWorkSpace\\RiverD\\images", "images/packed", "game");

		config.width=RiverDMain.WIDTH;
		config.height=RiverDMain.HEIGHT;
		config.title=RiverDMain.TITLE+" v"+RiverDMain.VERSION;
		new LwjglApplication(new RiverDMain(), config);
	}
}
