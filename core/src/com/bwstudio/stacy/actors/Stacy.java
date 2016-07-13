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
	private Animation currentAnimation;
	
	public Stacy() {
		super();
		setOrigin(getOriginX() + getWidth()/2f, getOriginY() + getHeight() / 2f);
		
		// Properties
		isJumping = false;
		jumpTime = maxJumpTime = 0.2f;
		jumpHeight = 225f;
		
		facingRight = true;
		walkingSpeed = 2f;
		
		// Initiate animations
		timePassed = 0;
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
		currentAnimation = idleAnimationR;
		setBounds(0, 0, 32, 39);
		setSize(32, 39);
	}
	
	public void update(float delta) {
		setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
		
		handleInput(delta);
	}
	
	private void handleInput(float delta) {
//		if (!Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (facingRight) {
				currentAnimation = idleAnimationR;
			} else {
				currentAnimation = idleAnimationL;
			}
//		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.setTransform(body.getTransform().getPosition().x - walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			facingRight = false;
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.setTransform(body.getTransform().getPosition().x + walkingSpeed / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			facingRight = true;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
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
	public void act(float deltaTime) {
	    super.act(deltaTime);
	    timePassed += deltaTime;
	}
	
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
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0f;
		fixture = body.createFixture(fdef);
		
		shape.set(new float[] {-(getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f - 3f) / Constants.PPM, (getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f - 3f) / Constants.PPM, getWidth() / 2f / Constants.PPM - 13f / Constants.PPM, -(getHeight() / 2f + 2f) / Constants.PPM, -(getWidth() / 2f - 13f) / Constants.PPM, -(getHeight() / 2f + 2f) / Constants.PPM});
		fdef.shape = shape;
		fdef.isSensor = true;
		body.createFixture(fdef);
		
		shape.dispose();
	}
	
	public boolean isFacingRight() {
		return facingRight;
	}
}
