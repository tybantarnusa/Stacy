package com.bwstudio.stacy.levels;

import com.bwstudio.stacy.levels.garden.Garden1Level;
import com.bwstudio.stacy.levels.garden.Garden2Level;
import com.bwstudio.stacy.levels.garden.Garden3Level;
import com.bwstudio.stacy.levels.garden.Garden4Level;
import com.bwstudio.stacy.levels.garden.InnerGarden0Level;
import com.bwstudio.stacy.levels.garden.InnerGarden1Level;
import com.bwstudio.stacy.levels.garden.InnerGarden2Level;
import com.bwstudio.stacy.levels.garden.InnerGarden3Level;
import com.bwstudio.stacy.levels.garden.InnerGarden4Level;
import com.bwstudio.stacy.levels.garden.InnerGarden5Level;
import com.bwstudio.stacy.levels.garden.InnerGarden6Level;
import com.bwstudio.stacy.levels.garden.InnerGarden7Level;

public enum Level {
	GARDEN_INNER_0(new InnerGarden0Level()),
	GARDEN_INNER_1(new InnerGarden1Level()),
	GARDEN_INNER_2(new InnerGarden2Level()),
	GARDEN_INNER_3(new InnerGarden3Level()),
	GARDEN_INNER_4(new InnerGarden4Level()),
	GARDEN_INNER_5(new InnerGarden5Level()),
	GARDEN_INNER_6(new InnerGarden6Level()),
	GARDEN_INNER_7(new InnerGarden7Level()),
	GARDEN_1(new Garden1Level()),
	GARDEN_2(new Garden2Level()),
	GARDEN_3(new Garden3Level()),
	GARDEN_4(new Garden4Level());
	
	private final BaseLevel instance;
	
	private Level(final BaseLevel levelClass) {
		this.instance = levelClass;
	}
	
	public BaseLevel instance() {
		return instance;
	}
}
