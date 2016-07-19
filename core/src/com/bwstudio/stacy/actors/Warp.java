package com.bwstudio.stacy.actors;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.DataThroughLevels;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.levels.Level;
import com.bwstudio.stacy.screens.LevelScreen;

public class Warp extends BaseActor {
	
	private final MyGame game;
	private Level levelTo;
	private float levelToX, levelToY;
	private boolean fromLeft;
	
	public Warp(final MyGame game, float x, float y, Level levelTo, float levelToX, float levelToY, boolean fromLeft) {
		this.game = game;
		this.levelTo = levelTo;
		this.levelToX = levelToX;
		this.levelToY = levelToY;
		this.fromLeft = fromLeft;
		setBounds(0, 0, 32, 32);
		setSize(32, 32);
		setPosition(x, y);
	}
	
	public Warp(final MyGame game, float x, float y, float scaleWidth, float scaleHeight, Level levelTo, float levelToX, float levelToY, boolean fromLeft) {
		this.game = game;
		this.levelTo = levelTo;
		this.levelToX = levelToX;
		this.levelToY = levelToY;
		this.fromLeft = fromLeft;
		setBounds(0, 0, 32 * scaleWidth, 32 * scaleHeight);
		setSize(32 * scaleWidth, 32 * scaleHeight);
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
		shape.setAsBox(getWidth() / 2f / Constants.PPM, getHeight() / 2f / Constants.PPM);
		
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
		Stacy stacy = ((LevelScreen) game.getScreen()).getPlayer();
		DataThroughLevels.STACY_IS_JUMPING = stacy.isJumping();
		DataThroughLevels.STACY_STATE = stacy.getState();
		DataThroughLevels.STACY_Y_VELOCITY = stacy.getBody().getLinearVelocity().y;
		this.levelToX = levelToX < -1000 ? stacy.getX() + stacy.getWidth() / 2f + levelTo.instance().offset().x : levelToX;
		this.levelToY = levelToY < -1000 ? stacy.getY() + stacy.getHeight() / 2f + levelTo.instance().offset().y : levelToY;
		System.out.println(levelToX + ", " + levelToY);
		game.setScreen(new LevelScreen(game, levelTo, levelToX, levelToY, fromLeft));
	}
	
}
