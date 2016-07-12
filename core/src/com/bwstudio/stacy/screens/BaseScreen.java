package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;

public abstract class BaseScreen implements Screen {
	
	protected MyGame game;
	protected BaseScreen prevScreen;
	
	protected Stage stage;
	
	protected BaseScreen(final MyGame game, BaseScreen prevScreen) {
		this.game = game;
		this.prevScreen = prevScreen;
		this.stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		stage.setViewport(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, game.cam));
	}
}
