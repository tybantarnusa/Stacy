package com.bwstudio.stacy;

public class Strings {

	public static String NEW_GAME;
	public static String DEMO;
	public static String LIPSUM;
	
	private static void buildEnglish() {
		NEW_GAME = "New Game";
		DEMO = "This is an example text demo.";
		LIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi eget purus ante. Mauris turpis nisi, porttitor et dui vel, vehicula\n"
				+ "vestibulum risus. Proin porta urna vitae metus semper, non consectetur nibh egestas. Donec eu lorem augue. Phasellus\n"
				+ "eget eros sapien. Donec vehicula rhoncus dapibus. Vestibulum lacinia urna eu tortor porttitor vestibulum vitae laoreet\n"
				+ "lacus. Morbi scelerisque odio id odio consequat, vitae mollis velit mattis.";
	}
	
	private static void buildIndonesian() {
		NEW_GAME = "Permainan Baru";
		DEMO = "Ini contoh teks untuk demonstrasi.";
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
