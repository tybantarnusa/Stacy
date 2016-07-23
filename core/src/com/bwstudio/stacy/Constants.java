package com.bwstudio.stacy;

public class Constants {
	
	public static boolean DEBUG = false;

	// Game Configurations
	public static String TITLE = "Stacy";
	public static boolean FULLSCREEN = false;
	public static float V_WIDTH = 800f;
	public static float V_HEIGHT = 600f;
	public static String VERSION = "0.0.3";
	
	// Pixel Per Meter
	public static float PPM = 100f;
	
	// Collision Bits
	public static short BIT_OBSTACLE = 0b00000001;
	public static short BIT_PLAYER = 0b00000010;
	public static short BIT_OWP = 0b00000100;
	public static short BIT_ENEMY = 0b00001000;
	public static short BIT_BULLET_ENEMY = 0b00010000;
	public static short BIT_BULLET_PLAYER = 0b00100000;
	
}
