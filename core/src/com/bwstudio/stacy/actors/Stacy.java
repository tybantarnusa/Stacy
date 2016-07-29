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
	private ShieldEntity shield;
	
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
	private Animation jumpL;
	private Animation jumpR;
	private Animation fallL;
	private Animation fallR;
	private Animation mirrorL;
	private Animation mirrorR;
	private Animation currentAnimation;
	
	private float hurtTime;
	
	// States
	public enum State {
		IDLE,
		INIT_RUN,
		RUN,
		STOP_RUN,
		JUMP,
		HURT,
		SHIELD
	}
	
	private enum Shield {
		NONE,
		MIRROR
	}
	private State state;
	private Shield selectedShield;
	
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
		shield = null;
		selectedShield = Shield.NONE;
		
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
		
		// Jump
		texture = new Texture("chars/stacy/stacy_jump.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		jumpR = new Animation(0.07f, frames);
		jumpR.setPlayMode(PlayMode.NORMAL);
		jumpL = new Animation(0.07f, mirror);
		jumpL.setPlayMode(PlayMode.NORMAL);
			
		// Fall
		texture = new Texture("chars/stacy/stacy_fall.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		fallR = new Animation(0.07f, frames);
		fallR.setPlayMode(PlayMode.LOOP);
		fallL = new Animation(0.07f, mirror);
		fallL.setPlayMode(PlayMode.LOOP);
		
		// MIRROR 
		texture = new Texture("chars/stacy/stacy_mirror.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		frames = new TextureRegion(texture).split(36, 39)[0];
		mirror = new TextureRegion(texture).split(36, 39)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		mirrorR = new Animation(0.07f, frames);
		mirrorR.setPlayMode(PlayMode.NORMAL);
		mirrorL = new Animation(0.07f, mirror);
		mirrorL.setPlayMode(PlayMode.NORMAL);	
		
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
	    
	    if (state == State.JUMP) {
	    	if (body.getLinearVelocity().y > 0)
	    		currentAnimation = facingRight ? jumpR : jumpL;
	    	else if (body.getLinearVelocity().y < 0)
	    		currentAnimation = facingRight ? fallR : fallL;
    		else
    			state = State.IDLE;
	    }
	    
	    if (hurtTime > 0) {
	    	hurtTime -= delta;
	    	currentAnimation = facingRight ? hurtR : hurtL;
	    } else if (state == State.HURT) {
	    	state = State.IDLE;
	    }
	    
	    if (state == State.HURT && shield != null) {
	    	shield.getBody().setUserData("destroy");
			shield = null;
	    }
	    
	    // Jump
//	    body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * 0.9f);
	    if (body.getLinearVelocity().y != 0 && state != State.HURT) {
	    	state = State.JUMP;
	    }
	    
		// debug position
//		System.out.println((getX() + getWidth() / 2f) + ", " + (getY() + getHeight() / 2f));
	    selectedShield = Shield.MIRROR;
	}
	
	private void handleInput(float delta) {
		
		if (state != State.HURT && state != State.JUMP) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
				state = State.SHIELD;
				timePassed = 0;
				switch (selectedShield) {
				case MIRROR:
					currentAnimation = facingRight ? mirrorR : mirrorL;
					shield = new MagicMikeMirror(body.getWorld());
					shield.getBody().setTransform(body.getTransform().getPosition().x + (facingRight?20:-20) / Constants.PPM, body.getTransform().getPosition().y, 0);
					((MagicMikeMirror) shield).resetTime();
					if (facingRight) ((MagicMikeMirror) shield).faceRight(); else ((MagicMikeMirror) shield).faceLeft();
					break;
				default:
					break;
				}
			}
			
			if (Gdx.input.isKeyPressed(Input.Keys.Z) && shield != null) {
				state = State.SHIELD;
				switch (selectedShield) {
				case MIRROR:
					break;
				default:
					break;
				}
			}
			
			if (!Gdx.input.isKeyPressed(Input.Keys.Z) && state == State.SHIELD && shield != null) {
				shield.getBody().setUserData("destroy");
				shield = null;
				state = State.IDLE;
			}
		}
		
		if (state == State.SHIELD) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				faceLeft();
			} else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				faceRight();
			}
			
			currentAnimation = facingRight ? mirrorR : mirrorL;
			shield.getBody().setTransform(body.getTransform().getPosition().x + (facingRight?20:-20) / Constants.PPM, body.getTransform().getPosition().y, 0);
			if (facingRight) ((MagicMikeMirror) shield).faceRight(); else ((MagicMikeMirror) shield).faceLeft();
		}
		
		if (state != State.HURT && state != State.SHIELD) {
			
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
				timePassed = 0;
				state = State.JUMP;
			}
			
			if (jumpTime > 0 && Gdx.input.isKeyPressed(Input.Keys.UP) ) {
				body.setLinearVelocity(0, jumpTime * Math.abs(body.getWorld().getGravity().y));
				isJumping = true;
				jumpTime -= delta;
			} else if (isJumping) {
				isJumping = false;
				timePassed = 0;
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
	    if (shield != null) shield.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
	    TextureRegion currentFrame = currentAnimation.getKeyFrame(timePassed);
	    if (shield != null) shield.draw(batch, parentAlpha);
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
		fdef.filter.maskBits = (short) (Constants.BIT_OBSTACLE | Constants.BIT_OWP | Constants.BIT_ENEMY | Constants.BIT_BULLET);
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
		hurtTime = 0.5f;
		
		if (knockback < 0) faceRight(); else faceLeft();
		
		body.setLinearVelocity(0, 0);
		body.applyForceToCenter(knockback, Math.abs(knockback) * 1.5f, true);
		LevelScreen.shake = 1f;
	}
	
	public void dispose() {
		idleAnimationL.getKeyFrames()[0].getTexture().dispose();
		startRunAnimationL.getKeyFrames()[0].getTexture().dispose();
		runAnimationL.getKeyFrames()[0].getTexture().dispose();
		hurtL.getKeyFrames()[0].getTexture().dispose();
		jumpL.getKeyFrames()[0].getTexture().dispose();
		fallL.getKeyFrames()[0].getTexture().dispose();
		mirrorL.getKeyFrames()[0].getTexture().dispose();
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
