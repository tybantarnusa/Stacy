package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Stacy;

public class LevelScreen extends BaseScreen {
	
	private Stacy stacy;
	private World world;
	
	public LevelScreen(final MyGame game, BaseScreen prevScreen) {
		super(game, prevScreen);
		
		// Create entities
		stacy = new Stacy("stacy.png");
		stacy.setPosition(0, 0);
		
		// Add entities to stage
		stage.addActor(stacy);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		stage.draw();
		
		game.batch.begin();
		game.batch.end();
	}
	
	public void update(float delta) {
		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
