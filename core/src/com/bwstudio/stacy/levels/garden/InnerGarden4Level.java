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

public class InnerGarden4Level implements BaseLevel {

	@Override
	public TiledMap buildMap() {
		TmxMapLoader.Parameters par = new TmxMapLoader.Parameters();
		par.textureMinFilter = TextureFilter.Linear;
		par.textureMinFilter = TextureFilter.Nearest;
		return new TmxMapLoader().load("maps/garden-inner-04.tmx");
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

		Warp warp = new Warp(game, -32 + 8, 32 * 10, 1, 2, Level.GARDEN_INNER_3, 413, -1111, 50.5f - 306.5f);
		warps.add(warp);
		warp = new Warp(game, 440, 75, 1, 3, Level.GARDEN_INNER_6, 3, -1111, 114.5f - 50.5f);
		warps.add(warp);
		warp = new Warp(game, 320, 32 * 13, 2, 1, Level.GARDEN_INNER_5, -1111, 3.5f, 282 - 346);
		warps.add(warp);
		
		return warps;
	}

}
