package com.bwstudio.stacy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bwstudio.stacy.levels.Level;
import com.bwstudio.stacy.screens.LevelScreen;

public class MyGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera cam;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Constants.V_WIDTH, Constants.V_HEIGHT);
		setScreen(new LevelScreen(this, null, true, Level.GARDEN_INNER));

		Strings.buildLanguage();
	}

	@Override
	public void render () {
		super.render();
		
		// Fullscreen
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4) ||
			((Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) ||
			((Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
			) {
			Constants.FULLSCREEN = !Constants.FULLSCREEN;
			
			if (Constants.FULLSCREEN) {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				Gdx.input.setCursorCatched(true);
			} else {
				Gdx.graphics.setWindowedMode((int) Constants.V_WIDTH, (int) Constants.V_HEIGHT);
				Gdx.input.setCursorCatched(false);
			}
		}
		
	}
	
	public void dispose() {
		batch.dispose();
		getScreen().dispose();
	}
}
