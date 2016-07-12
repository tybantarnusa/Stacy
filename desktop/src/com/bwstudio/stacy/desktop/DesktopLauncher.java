package com.bwstudio.stacy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = Constants.FULLSCREEN;
		config.width = (int) Constants.V_WIDTH;
		config.height = (int) Constants.V_HEIGHT;
		config.title = Constants.TITLE;
		config.resizable = false;
		new LwjglApplication(new MyGame(), config);
	}
}
