package com.bwstudio.stacy.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
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
	
	public Stacy(Texture texture) {
		super(texture);
		setOrigin(getOriginX() + getWidth()/2f, getOriginY() + getHeight() / 2f);
		
		isJumping = false;
		jumpTime = maxJumpTime = 0.3f;
		jumpHeight = 450f;
	}
	
	public void update(float delta) {
		setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
		
		handleInput(delta);
	}
	
	private void handleInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			body.setTransform(body.getTransform().getPosition().x - 5 / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			setScaleX(-1);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			body.setTransform(body.getTransform().getPosition().x + 5 / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			setScaleX(1);
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
	
	public void createPhysics(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
		body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(getWidth() / 2f / Constants.PPM, getHeight() / 2f / Constants.PPM);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0f;
		fixture = body.createFixture(fdef);
		
		shape.set(new float[] {-(getWidth() / 2f - 5f) / Constants.PPM, -(getHeight() / 2f - 5f) / Constants.PPM, (getWidth() / 2f - 5f) / Constants.PPM, -(getHeight() / 2f - 5f) / Constants.PPM, getWidth() / 2f / Constants.PPM - 5f / Constants.PPM, -(getHeight() / 2f + 5f) / Constants.PPM, -(getWidth() / 2f - 5f) / Constants.PPM, -(getHeight() / 2f + 5f) / Constants.PPM});
		fdef.shape = shape;
		fdef.isSensor = true;
		body.createFixture(fdef);
		
		shape.dispose();
	}
	
}
