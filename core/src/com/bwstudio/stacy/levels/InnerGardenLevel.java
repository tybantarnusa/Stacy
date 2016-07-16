package com.bwstudio.stacy.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Warp;

public class InnerGardenLevel implements BaseLevel {

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
	
	@Override
	public void buildParticle(ParticleEffect particleEffect) {
		particleEffect.load(Gdx.files.internal("particles/relaxing.party"), Gdx.files.internal("particles"));
	}

	@Override
	public Vector2 getYBounds() {
		return new Vector2(Constants.V_HEIGHT / 4f - 32f, Constants.V_HEIGHT / 4f - 32f);
	}

	@Override
	public Array<Warp> buildWarpPoints(MyGame game) {
		Array<Warp> warps = new Array<Warp>();

		Warp warp = new Warp(game, Level.GARDEN, true, 665, 100);
		warps.add(warp);
		
		return warps;
	}

}
