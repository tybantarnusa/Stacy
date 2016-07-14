package com.bwstudio.stacy.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

public class GardenLevel implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		return new TmxMapLoader().load("maps/garden-inner-00.tmx");
	}
	
	@Override
	public void drawBackground(TiledMapRenderer tmr) {
		tmr.render(new int[] {0});
	}

	@Override
	public void drawForeground(TiledMapRenderer tmr) {
		tmr.render(new int[] {1, 2});
	}

	@Override
	public Vector2 getStartingPosition(boolean fromLeft) {
		return fromLeft ?
				new Vector2(50, 83) :
				new Vector2(636, 83);
	}

}
