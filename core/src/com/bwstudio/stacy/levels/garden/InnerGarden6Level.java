package com.bwstudio.stacy.levels.garden;

import com.badlogic.gdx.Gdx;
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

public class InnerGarden6Level implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		TmxMapLoader.Parameters par = new TmxMapLoader.Parameters();
		par.textureMinFilter = TextureFilter.Linear;
		par.textureMinFilter = TextureFilter.Nearest;
		return new TmxMapLoader().load("maps/garden-inner-06.tmx");
	}
	
	@Override
	public void drawBackground(TiledMapRenderer tmr) {
		tmr.render(new int[] {0});
	}

	@Override
	public void drawForeground(TiledMapRenderer tmr) {
		tmr.render(new int[] {1, 2, 3});
	}

	@Override
	public void buildParticle(ParticleEffect particleEffect) {
		particleEffect.load(Gdx.files.internal("particles/relaxing.party"), Gdx.files.internal("particles"));
	}

	@Override
	public Vector2 getYBounds() {
		return null;
	}

	@Override
	public Array<Warp> buildWarpPoints(MyGame game) {
		Array<Warp> warps = new Array<Warp>();

		Warp warp = new Warp(game, -32 + 8, 32 * 4 + 16, 1, 3, Level.GARDEN_INNER_4, 413f, -1111, 50.5f - 114.5f);
		warps.add(warp);
		warp = new Warp(game, 32 * 21 - 8, 32 * 4 + 16, 1, 3, Level.GARDEN_INNER_7, 3f, -1111, 50.5f - 114.5f);
		warps.add(warp);
		
		return warps;
	}

}
