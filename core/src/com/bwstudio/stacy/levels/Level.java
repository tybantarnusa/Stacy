package com.bwstudio.stacy.levels;

import com.bwstudio.stacy.levels.garden.Garden1Level;
import com.bwstudio.stacy.levels.garden.Garden2Level;
import com.bwstudio.stacy.levels.garden.Garden3Level;
import com.bwstudio.stacy.levels.garden.InnerGarden0Level;
import com.bwstudio.stacy.levels.garden.InnerGarden1Level;

public enum Level {
	GARDEN_INNER_0(new InnerGarden0Level()),
	GARDEN_INNER_1(new InnerGarden1Level()),
	GARDEN_1(new Garden1Level()),
	GARDEN_2(new Garden2Level()),
	GARDEN_3(new Garden3Level());
	
	private final BaseLevel instance;
	
	private Level(final BaseLevel levelClass) {
		this.instance = levelClass;
	}
	
	public BaseLevel instance() {
		return instance;
	}
}
