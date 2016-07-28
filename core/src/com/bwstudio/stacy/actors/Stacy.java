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
import com.bwstudio.stacy.DataThroughLevels;
import com.bwstudio.stacy.screens.LevelScreen;

public class Stacy extends BaseActor {
	
	private boolean isJumping;
	private float jumpTime;
	private float maxJumpTime;
	private float minJumpHeight;
	private float minJumpVelocity;
	
	private int health;
	
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
	private Animation hurtR;
	private Animation hurtL;
	private Animation currentAnimation;
	
	private float hurtTime;
	
	// States
	public enum State {
		IDLE,
		INIT_RUN,
		RUN,
		STOP_RUN,
		JUMP,
		HURT
	}
	private State state;
	
	public Stacy(World world, float startingPosX, float startingPosY) {
		super();
		setOrigin(getOriginX() + getWidth()/2f, getOriginY() + getHeight() / 2f);
		setPosition(startingPosX, startingPosY);
		createPhysics(world);
		DataThroughLevels.STACY_W = getWidth();
		DataThroughLevels.STACY_H = getHeight();
		
		// Properties
		isJumping = false;
		jumpTime = maxJumpTime = 0.55f;
		minJumpHeight = 0.2f;
		minJumpVelocity = (float) Math.sqrt(2 * Math.abs(body.getWorld().getGravity().y) * minJumpHeight);
		facingRight = true;
		walkingSpeed = 1.65f;
		hurtTime = 0;
		state = State.IDLE;
		health = 10;
		
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
		
		// Hurt
		texture = new Texture("chars/stacy/stacy_hurt.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		hurtR = new Animation(0.07f, frames);
		hurtR.setPlayMode(PlayMode.LOOP);
		hurtL = new Animation(0.07f, mirror);
		hurtL.setPlayMode(PlayMode.LOOP);
		
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
	    
	    if (state == State.IDLE) {
	    	currentAnimation = facingRight ? idleAnimationR : idleAnimationL;
	    }
	    
	    if (body.getLinearVelocity().y == 0 && state == State.JUMP) {
	    	state = State.IDLE;
	    }
	    
	    if (hurtTime > 0) {
	    	hurtTime -= delta;
	    	currentAnimation = facingRight ? hurtR : hurtL;
	    } else if (state == State.HURT) {
	    	state = State.IDLE;
	    }
	    
	    // Jump
//	    body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 0.9f);
	    if (body.getLinearVelocity().y != 0 && state != State.HURT) {
	    	state = State.JUMP;
	    }
	    
		// debug position
//		System.out.println((getX() + getWidth() / 2f) + ", " + (getY() + getHeight() / 2f));
	}
	
	private void handleInput(float delta) {
		
		if (state != State.HURT) {
		
			if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT) && state != State.JUMP) {
				state = State.IDLE;
				currentAnimation = facingRight ? idleAnimationR : idleAnimationL;
			}
			
			if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				state = state != State.JUMP ? State.INIT_RUN : State.JUMP;
				currentAnimation = facingRight ? startRunAnimationR : startRunAnimationL;
			}
			
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				
				setIdle();
			
			} else {
			
				if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
					body.setTransform(body.getTransform().getPosition().x - walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
					body.setAwake(true);
					faceLeft();
					
					if (state != State.INIT_RUN && state != State.JUMP) {
						state = State.RUN;
						currentAnimation = runAnimationL;
					}
				}
				
				if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
					body.setTransform(body.getTransform().getPosition().x + walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
					body.setAwake(true);
					faceRight();
					
					if (state != State.INIT_RUN && state != State.JUMP) {
						state = State.RUN;
						currentAnimation = runAnimationR;
					}
				}
			
			}
	
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && state != State.JUMP) {
				jumpTime = maxJumpTime;
				state = State.JUMP;
			}
			
			if (jumpTime > 0 && Gdx.input.isKeyPressed(Input.Keys.UP) ) {
				body.setLinearVelocity(0, jumpTime * Math.abs(body.getWorld().getGravity().y));
				isJumping = true;
				jumpTime -= delta;
			} else if (isJumping) {
				isJumping = false;
//				body.setLinearVelocity(0, jumpHeight / 2f / Constants.PPM);
			}

			if (jumpTime > 0 && !Gdx.input.isKeyPressed(Input.Keys.UP) && state == State.JUMP) {
				jumpTime = 0;
				if (body.getLinearVelocity().y > minJumpVelocity)
					body.setLinearVelocity(body.getLinearVelocity().x, minJumpVelocity);
			}
		
		}

	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	    timePassed += delta;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
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
		fdef.friction = 0.2f;
		fdef.filter.categoryBits = Constants.BIT_PLAYER;
		fdef.filter.maskBits = (short) (Constants.BIT_OBSTACLE | Constants.BIT_OWP | Constants.BIT_ENEMY);
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		
		// Foot Sensor
		shape.set(new float[] { -(getWidth() / 2f - 4f) / Constants.PPM,
								-(getHeight() / 2f + 15f) / Constants.PPM,
								(getWidth() / 2f - 4f) / Constants.PPM,
								-(getHeight() / 2f + 15f) / Constants.PPM,
								(getWidth() / 2f - 4f) / Constants.PPM,
								-(getHeight() / 2f + 20f) / Constants.PPM,
								-(getWidth() / 2f - 4f) / Constants.PPM,
								-(getHeight() / 2f + 20f) / Constants.PPM});
		fdef.shape = shape;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("foot");
		
		// Head Sensor
		shape.set(new float[] {-3 / Constants.PPM, 3 / Constants.PPM, -3 / Constants.PPM, 15 / Constants.PPM, 3 / Constants.PPM, 15 / Constants.PPM, 3 / Constants.PPM, 3 / Constants.PPM});
		fdef.shape = shape;
		fdef.isSensor = true;
		body.createFixture(fdef).setUserData("head");
		
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
	
	public void setOnGround() {
		isJumping = false;
		jumpTime = 0;
	}
	
	public void setOffGround() {
		if (state != State.HURT)
			state = State.JUMP;
	}
	
	public void giveDamage(int amount, float knockback) {
		state = State.HURT;
		hurtTime = Math.abs(knockback) < 100 ? 0.5f : 0.5f;
		body.setLinearVelocity(0, 0);
		body.applyForceToCenter(knockback, Math.abs(knockback) * 1.5f, true);
		LevelScreen.shake = 1f;
	}
	
	public void dispose() {
		idleAnimationL.getKeyFrames()[0].getTexture().dispose();
		startRunAnimationL.getKeyFrames()[0].getTexture().dispose();
		runAnimationL.getKeyFrames()[0].getTexture().dispose();
		hurtL.getKeyFrames()[0].getTexture().dispose();
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}
	
	public boolean isJumping() {
		return isJumping;
	}
	
	public void setJumpTime(float jumpTime) {
		this.jumpTime = jumpTime;
	}
	
	public float getJumpTime() {
		return jumpTime;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}

	public void setYVelocity(float yVelocity) {
		body.setLinearVelocity(body.getLinearVelocity().x, yVelocity);
	}
	
	public void die() {
		health = 0;
		state = State.HURT;
	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean isDead() {
		return health == 0;
	}
	
}
