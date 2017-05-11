package com.robcio.riverd;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.robcio.riverd.RiverDMain;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer=false;
		config.useCompass=false;
//		config.width=RiverDMain.WIDTH;
//		config.height=RiverDMain.HEIGHT;
//		config.title=RiverDMain.TITLE+"v"+RiverDMain.VERSION;
		initialize(new  RiverDMain(), config);
	}
}
