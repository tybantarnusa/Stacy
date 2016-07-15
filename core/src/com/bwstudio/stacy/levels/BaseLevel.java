package com.bwstudio.stacy.levels;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

public interface BaseLevel {
	
	public TiledMap buildMap();
	public void drawBackground(TiledMapRenderer tmr);
	public void drawForeground(TiledMapRenderer tmr);
	public Vector2 getStartingPosition(boolean fromLeft);
	public void buildParticle(ParticleEffect particleEffect);
	
}
