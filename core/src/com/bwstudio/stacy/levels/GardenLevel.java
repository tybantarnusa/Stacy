package com.bwstudio.stacy.levels;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Warp;

public class GardenLevel implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		return new TmxMapLoader().load("maps/garden-01.tmx");
	}
	
	@Override
	public void drawBackground(TiledMapRenderer tmr) {
		tmr.render(new int[] {0, 1});
	}

	@Override
	public void drawForeground(TiledMapRenderer tmr) {
		tmr.render(new int[] {2, 3});
	}

	@Override
	public Vector2 getStartingPosition(boolean fromLeft) {
		return fromLeft ?
				new Vector2(5.45f, 50.5f) :
				new Vector2(636, 83);
	}
	
	@Override
	public Vector2 getYBounds() {
		return null;
	}
	
	@Override
	public void buildParticle(ParticleEffect particleEffect) {}

	@Override
	public Array<Warp> buildWarpPoints(MyGame game) {
		Array<Warp> warps = new Array<Warp>();

		Warp warp = new Warp(game, Level.GARDEN_INNER, false, -25, 64);
		warps.add(warp);
		
		return warps;
	}

}
