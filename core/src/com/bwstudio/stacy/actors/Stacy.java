package com.bwstudio.stacy.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.Constants;

public class Stacy extends BaseActor {
	
	private boolean isJumping;
	private float jumpHeight;
	private float jumpTime;
	private float maxJumpTime;
	
	private boolean facingRight;
	private float walkingSpeed;
	
	// Animations
	private float timePassed;
	private Animation idleAnimationR;
	private Animation idleAnimationL;
	private Animation startRunAnimationR;
	private Animation startRunAnimationL;
	private Animation runAnimationR;
	private Animation runAnimationL;
	private Animation currentAnimation;
	
	// States
	private enum State {
		IDLE,
		INIT_RUN,
		RUN,
		STOP_RUN,
		JUMP
	}
	private State state;
	
	public Stacy() {
		super();
		setOrigin(getOriginX() + getWidth()/2f, getOriginY() + getHeight() / 2f);
		
		// Properties
		isJumping = false;
		jumpTime = maxJumpTime = 0.3f;
		jumpHeight = 225f;
		
		facingRight = true;
		walkingSpeed = 1.65f;
		
		state = State.IDLE;
		
		
		// Initiate animations
		timePassed = 0;
		
		// Idle
		Texture texture = new Texture("chars/stacy/stacy_stand.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		TextureRegion[] frames = new TextureRegion(texture).split(36, 39)[0];
		TextureRegion[] mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		idleAnimationR = new Animation(0.1f, frames);
		idleAnimationR.setPlayMode(PlayMode.LOOP);
		idleAnimationL = new Animation(0.1f, mirror);
		idleAnimationL.setPlayMode(PlayMode.LOOP);
		
		// Start Run
		texture = new Texture("chars/stacy/stacy_start_run.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		startRunAnimationR = new Animation(0.1f, frames);
		startRunAnimationR.setPlayMode(PlayMode.NORMAL);
		startRunAnimationL = new Animation(0.1f, mirror);
		startRunAnimationL.setPlayMode(PlayMode.NORMAL);
		
		// Run
		texture = new Texture("chars/stacy/stacy_run.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		runAnimationR = new Animation(0.07f, frames);
		runAnimationR.setPlayMode(PlayMode.LOOP);
		runAnimationL = new Animation(0.07f, mirror);
		runAnimationL.setPlayMode(PlayMode.LOOP);
		
		currentAnimation = idleAnimationR;
		setBounds(0, 0, 32, 39);
		setSize(32, 39);
	}
	
	public void update(float delta) {
		// Move
		setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
		
		// Input
		handleInput(delta);
		
		// Animation
	    if (state == State.INIT_RUN && currentAnimation.isAnimationFinished(timePassed)) {
			state = State.RUN;
			currentAnimation = facingRight ? runAnimationR : runAnimationL;
		}
		
		// debug position
		System.out.println((getX() + getWidth() / 2f) + ", " + (getY() + getHeight() / 2f));
	}
	
	private void handleInput(float delta) {
		if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			state = State.IDLE;
			currentAnimation = facingRight ? idleAnimationR : idleAnimationL;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			state = State.INIT_RUN;
			currentAnimation = facingRight ? startRunAnimationR : startRunAnimationL;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			
			setIdle();
		
		} else {
		
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				body.setTransform(body.getTransform().getPosition().x - walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
				body.setAwake(true);
				faceLeft();
				
				if (state != State.INIT_RUN) {
					currentAnimation = runAnimationL;
				}
			}
			
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				body.setTransform(body.getTransform().getPosition().x + walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
				body.setAwake(true);
				faceRight();
				
				if (state != State.INIT_RUN) {
					currentAnimation = runAnimationR;
				}
			}
		
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !isJumping) {
			jumpTime = maxJumpTime;
		}
		
		if (jumpTime > 0 && Gdx.input.isKeyPressed(Input.Keys.UP) ) {
			body.setLinearVelocity(0, jumpHeight / Constants.PPM);
			isJumping = true;
			jumpTime -= delta;
		} else if (isJumping) {
			isJumping = false;
			body.setLinearVelocity(0, jumpHeight / 2f / Constants.PPM);
		}
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	    timePassed += delta;
	};
	
	@Override
	public void draw(Batch batch, float ParentAlpha){
	    TextureRegion currentFrame = currentAnimation.getKeyFrame(timePassed);
	    batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}
	
	public void createPhysics(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.set(new float[] {-5 / Constants.PPM, -17 / Constants.PPM, -5 / Constants.PPM, 15 / Constants.PPM, 5 / Constants.PPM, 15 / Constants.PPM, 5 / Constants.PPM, -17 / Constants.PPM});
		
		// Main Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0f;
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = (short) (Constants.BIT_GROUND | Constants.BIT_OWP | Constants.BIT_ENEMY);
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		
		// Foot Sensor
		shape.set(new float[] {-(getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f - 3f) / Constants.PPM, (getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f - 3f) / Constants.PPM, getWidth() / 2f / Constants.PPM - 13f / Constants.PPM, -(getHeight() / 2f + 2f) / Constants.PPM, -(getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f + 2f) / Constants.PPM});
		fdef.shape = shape;
		fdef.isSensor = true;
		body.createFixture(fdef);
		
		shape.dispose();
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
	
	public void faceRight() {
		facingRight = true;
	}
	
	public void faceLeft() {
		facingRight = false;
	}
	
	public void setIdle() {
		state = State.IDLE;
		currentAnimation = facingRight ? idleAnimationR : idleAnimationL;
	}
	
	public void dispose() {
		idleAnimationL.getKeyFrames()[0].getTexture().dispose();
		startRunAnimationL.getKeyFrames()[0].getTexture().dispose();
		runAnimationL.getKeyFrames()[0].getTexture().dispose();
	}
}
