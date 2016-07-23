package com.bwstudio.stacy.levels.garden;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

public class Garden4Level implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		TmxMapLoader.Parameters par = new TmxMapLoader.Parameters();
		par.textureMinFilter = TextureFilter.Linear;
		par.textureMinFilter = TextureFilter.Nearest;
		return new TmxMapLoader().load("maps/garden-04.tmx");
	}
	
	@Override
	public void drawBackground(TiledMapRenderer tmr) {
		tmr.render(new int[] {0, 1, 2});
	}

	@Override
	public void drawForeground(TiledMapRenderer tmr) {
		tmr.render(new int[] {3, 4, 5});
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

		Warp warp = new Warp(game, 32 * 9, -32, 2, 1, Level.GARDEN_INNER_5, -1111, 378, 294 - 262);
		warps.add(warp);
		
		warp = new Warp(game, 32 * 21 - 8, 32 * 5, 1, 10, Level.GARDEN_3, 3, -1111, 50.5f - 82.5f);
		warps.add(warp);
		
		return warps;
	}

}
