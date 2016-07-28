package com.bwstudio.stacy.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bwstudio.stacy.Constants;

public class Enemy1 extends BaseActor implements Enemy {

	private State state;
	private float stateTimer;
	
	private float timePassed;
	private Animation walkingL;
	private Animation walkingR;
	private Animation currentAnimation;
	
	private Array<Bullet> bullets;
	private Array<Bullet> toBeRemoved;
	
	private Stacy target;
	
	public enum State {
		WALK_LEFT,
		WALK_RIGHT,
		STAY_STILL,
		ATTACKING
	}
	
	public Enemy1() {
		timePassed = 0;
		Texture texture = new Texture("chars/enemies/tomcruise.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		TextureRegion[] frames = new TextureRegion(texture).split(36, 40)[0];
		TextureRegion[] mirror = new TextureRegion(texture).split(36, 40)[0];
		for(TextureRegion m : mirror) {
			m.flip(true, false);
		}
		walkingL = new Animation(0.2f, frames);
		walkingL.setPlayMode(PlayMode.LOOP);
		walkingR = new Animation(0.2f, mirror);
		walkingR.setPlayMode(PlayMode.LOOP);
		currentAnimation = walkingL;
		state = State.WALK_LEFT;
		stateTimer = 0;
		
		bullets = new Array<Bullet>();
		toBeRemoved = new Array<Bullet>();
		
		setSize(36, 40);
		setBounds(0, 0, 36, 40);
	}
	
	@Override
	public void update(float delta) {}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
	    TextureRegion currentFrame = currentAnimation.getKeyFrame(timePassed);
	    for (Bullet bullet : bullets) {
	    	bullet.draw(batch, parentAlpha);
	    }
	    batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		timePassed += delta;
		for (Bullet bullet : bullets) {
			if (bullet.getBody() == null) toBeRemoved.add(bullet);
	    	bullet.act(delta);
	    }
		
		for (Bullet b : toBeRemoved) {
			bullets.removeValue(b, true);
		}
		toBeRemoved.clear();
		
		// Move
		setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
		if (state == State.WALK_LEFT) {
			stateTimer += delta;
			body.setTransform(body.getTransform().getPosition().x - 0.2f / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			if (stateTimer > 10) {
				setState(State.WALK_RIGHT);
			}
		} else if (state == State.WALK_RIGHT) {
			stateTimer += delta;
			body.setTransform(body.getTransform().getPosition().x + 0.2f / Constants.PPM, body.getTransform().getPosition().y, 0);
			body.setAwake(true);
			if (stateTimer > 10) {
				setState(State.WALK_LEFT);
			}
		} else if (state == State.STAY_STILL) {
			stateTimer += delta;
			if (stateTimer > 1) {
				setState(currentAnimation == walkingL ? State.WALK_LEFT : State.WALK_RIGHT);
			}
		} else {
			stateTimer += delta;
			if (target.getBody().getPosition().x < body.getPosition().x) {
				currentAnimation = walkingL;
			} else {
				currentAnimation = walkingR;
			}
			if (stateTimer > 1.5f) {
				bullets.add(new Bullet(getX(Align.center), getY(Align.center), target, body.getWorld()));
				stateTimer = 0;
			}
		}
		
		// Animation
		if (state == State.WALK_LEFT) {
			currentAnimation = walkingL;
		} else if (state == State.WALK_RIGHT) {
			currentAnimation = walkingR;
		}
	}

	@Override
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
		fdef.filter.categoryBits = Constants.BIT_ENEMY;
		fdef.filter.maskBits = (short) (Constants.BIT_OBSTACLE | Constants.BIT_OWP | Constants.BIT_PLAYER);
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		shape.dispose();
		
		// Sensor Radius
		CircleShape shape2 = new CircleShape();
		shape2.setRadius(150 / Constants.PPM);
		fdef.isSensor = true;
		fdef.shape = shape2;
		body.createFixture(fdef).setUserData("enemy1 sight");
		shape2.dispose();
	}
	
	public void attack(Stacy stacy) {
		setState(State.ATTACKING);
		target = stacy;
	}
	
	public void setState(State state) {
		stateTimer = 0;
		this.state = state;
	}
	
	public State getState() { return state; }

	@Override
	public void dispose() {
		walkingL.getKeyFrames()[0].getTexture().dispose();
	}

}
