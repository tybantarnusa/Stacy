package com.bwstudio.stacy.levels;

public enum Level {
	GARDEN(new GardenLevel());
	
	private final BaseLevel instance;
	
	private Level(final BaseLevel levelClass) {
		this.instance = levelClass;
	}
	
	public BaseLevel instance() {
		return instance;
	}
}
