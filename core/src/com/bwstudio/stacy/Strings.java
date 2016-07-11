package com.bwstudio.stacy;

public class Strings {

	public static String NEW_GAME;
	
	private static void buildEnglish() {
		NEW_GAME = "New Game";
	}
	
	private static void buildIndonesian() {
		NEW_GAME = "Permainan Baru";
	}
	
	public enum Language {
		ENGLISH,
		INDONESIAN
	}
	
	public static void buildLanguage(Language language) {
		switch (language) {
		case ENGLISH:
			buildEnglish();
			break;
		case INDONESIAN:
			buildIndonesian();
			break;
		default:
			buildEnglish();
			break;
		}
	}
	
	public static void buildLanguage() {
		buildLanguage(Language.ENGLISH);
	}
	
}
