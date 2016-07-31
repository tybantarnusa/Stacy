package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.FontBuilder;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.levels.Level;

public class TitleScreen extends BaseScreen {
	private Texture img;
	private BitmapFont font;
	private float time;
	private Image cursor;
	private int index;
	
	public TitleScreen(MyGame game, BaseScreen prevScreen) {
		super(game, null);
	}

	@Override
	public void show() {
		img = new Texture("images/titlescreen.png");
		img.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		Image image = new Image(img);
		image.scaleBy(1.5f);
		image.setPosition(-Constants.V_WIDTH/2f, -Constants.V_HEIGHT/2f+20f);
		image.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 1)));
		font = FontBuilder.build(36, Color.WHITE, "fonts/gothic_pixel.ttf");
		stage.addActor(image);
		
		Texture cursorTex = new Texture("images/cursor.png");
		cursorTex.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		cursor = new Image(cursorTex);
		cursor.scaleBy(1.5f);
		cursor.setPosition(-375, -74);
		cursor.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(1.25f), Actions.alpha(1)));
		stage.addActor(cursor);
		
		time = 0;
		index = 0;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			index = MathUtils.clamp(index + 1, 0, 1);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
			index = MathUtils.clamp(index - 1, 0, 1);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
			if (index == 0) {
				game.setScreen(new LevelScreen(game, Level.GARDEN_INNER_0, 102.5f, 114.5f));
			} else {
				Gdx.app.exit();
			}
		}
		
		if (index == 0) {
			cursor.setPosition(-375, -74);
		} else {
			cursor.setPosition(-375, -115);
		}
		
		if (time > 1.25f) {
			game.batch.begin();
			font.draw(game.batch, "New Game", 60, 250);
			font.draw(game.batch, "Exit Game", 60, 210);
			game.batch.end();
		} else {
			time += delta;
		}
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
		img.dispose();
		stage.dispose();
	}

}
