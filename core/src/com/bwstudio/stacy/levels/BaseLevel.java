package com.bwstudio.stacy.levels;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Warp;

public interface BaseLevel {
	
	public TiledMap buildMap();
	public void drawBackground(TiledMapRenderer tmr);
	public void drawForeground(TiledMapRenderer tmr);
	public Vector2 getStartingPosition(boolean fromLeft);
	public Vector2 getYBounds();
	public void buildParticle(ParticleEffect particleEffect);
	public Array<Warp> buildWarpPoints(MyGame game);
	
}
