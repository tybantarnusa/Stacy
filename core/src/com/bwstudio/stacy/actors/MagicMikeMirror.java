package com.bwstudio.stacy.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.Constants;

public class MagicMikeMirror extends ShieldEntity {
	private Animation left;
	private Animation right;
	private Animation currentAnimation;
	
	private float timePassed;
	
	public MagicMikeMirror(World world) {
		super();
		
		Texture texture = new Texture("things/mirror.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		TextureRegion[] frames = new TextureRegion(texture).split(36, 39)[0];
		TextureRegion[] mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		right = new Animation(0.07f, frames);
		right.setPlayMode(PlayMode.NORMAL);
		left = new Animation(0.07f, mirror);
		left.setPlayMode(PlayMode.NORMAL);
		
		currentAnimation = right;
		
		timePassed = 0;
		
		setSize(36, 39);
		setBounds(0, 0, 36, 39);
		createPhysics(world);
	}
	
	@Override
	public void update(float delta) {}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	    timePassed += delta;
	    setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
	    TextureRegion currentFrame = currentAnimation.getKeyFrame(timePassed);
	    batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void createPhysics(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.KinematicBody;
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1 / Constants.PPM, 20 / Constants.PPM);
		
		// Main Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.restitution = 1;
		fdef.friction = 0;
		fdef.filter.categoryBits = Constants.BIT_SHIELD;
		fdef.filter.maskBits = (short) (Constants.BIT_BULLET | Constants.BIT_ENEMY);
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
	}
	
	public void faceRight() {
		currentAnimation = right;
	}
	
	public void faceLeft() {
		currentAnimation = left;
	}
	
	public void resetTime() {
		timePassed = 0;
	}

	@Override
	public void dispose() {
		left.getKeyFrames()[0].getTexture().dispose();
	}

}
