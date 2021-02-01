package com.hardgforgif.dragonboatracing.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hardgforgif.dragonboatracing.Game;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.resizable = false;
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new Game(), config);
	}
}
