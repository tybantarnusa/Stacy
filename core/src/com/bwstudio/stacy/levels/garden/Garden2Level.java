package com.bwstudio.stacy.levels.garden;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Warp;
import com.bwstudio.stacy.levels.BaseLevel;
import com.bwstudio.stacy.levels.Level;

public class Garden2Level implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		return new TmxMapLoader().load("maps/garden-02.tmx");
	}
	
	@Override
	public void drawBackground(TiledMapRenderer tmr) {
		tmr.render(new int[] {0, 1, 2});
	}

	@Override
	public void drawForeground(TiledMapRenderer tmr) {
		tmr.render(new int[] {3, 4});
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

		Warp warp = new Warp(game, -25, 32 * 6, 1, 8, Level.GARDEN_1, 630, -1111, false);
		warps.add(warp);
		warp = new Warp(game, 32 * 14, -40, 2, 1, Level.GARDEN_INNER_1, -1111, 320, false);
		warps.add(warp);
		
		return warps;
	}

	@Override
	public Vector2 offset() {
		return new Vector2(126, -200);
	}

}
