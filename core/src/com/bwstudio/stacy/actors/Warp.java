package com.bwstudio.stacy.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.levels.Level;
import com.bwstudio.stacy.screens.LevelScreen;

public class Warp extends BaseActor {
	
	private final MyGame game;
	private Level levelTo;
	private boolean fromLeft;
	
	public Warp(final MyGame game, Level levelTo, boolean fromLeft, float x, float y) {
		this.game = game;
		this.levelTo = levelTo;
		this.fromLeft = fromLeft;
		setBounds(0, 0, 32, 32);
		setSize(32, 32);
		setPosition(x, y);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void createPhysics(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.StaticBody;
		bdef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(16 / Constants.PPM, 16 / Constants.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0f;
		fdef.isSensor = true;
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
	}

	@Override
	public void dispose() {
		
	}
	
	public void teleport() {
		game.setScreen(new LevelScreen(game, null, fromLeft, levelTo));
	}
	
}
