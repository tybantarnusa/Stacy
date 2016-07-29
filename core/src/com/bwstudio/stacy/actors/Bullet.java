package com.bwstudio.stacy.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.bwstudio.stacy.Constants;

public class Bullet extends BaseActor {

	private Vector2 direction;
	private float force;
	private float lifeTime;
	private Animation animation;
	
	public Bullet(float x, float y, BaseActor target, World world) {
		setSize(22, 22);
		setBounds(0, 0, 22, 22);
		
		setPosition(x, y);
		
		direction = new Vector2(target.getX(Align.center) - x, target.getY(Align.center) - y).nor();
		force = 90;
		lifeTime = 0;
		
		Texture texture = new Texture("things/bulletfire.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		TextureRegion[] frames = new TextureRegion(texture).split(22, 22)[0];
		animation = new Animation(75f/1000f, frames);
		animation.setPlayMode(PlayMode.LOOP);
		
		createPhysics(world);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (body != null)
			setPosition(body.getPosition().x * Constants.PPM - getWidth() / 2f, body.getPosition().y * Constants.PPM - getHeight() / 2f);
		
		lifeTime += delta;
		if (lifeTime > 30) destroy();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
	    TextureRegion currentFrame = animation.getKeyFrame(lifeTime);
	    batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void createPhysics(World world) {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyType.DynamicBody;
		bdef.gravityScale = 0;
		bdef.position.set(getX() / Constants.PPM, getY() / Constants.PPM);
		body = world.createBody(bdef);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(8 / Constants.PPM);
		
		// Main Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.friction = 0;
		fdef.filter.categoryBits = Constants.BIT_ENEMY;
		fdef.filter.maskBits = (short) (Constants.BIT_OBSTACLE | Constants.BIT_PLAYER | Constants.BIT_SHIELD);
		fixture = body.createFixture(fdef);
		fixture.setUserData(this);
		shape.dispose();
		body.applyForceToCenter(direction.scl(force), true);
	}
	
	public int knockbackForce() {
		return direction.x < 0 ? -50 : 50;
	}
	
	public void destroy() {
		if (body != null) body.setUserData("destroy");
		body = null;
	}
	
	@Override
	public void dispose() {
		animation.getKeyFrames()[0].getTexture().dispose();
	}

	@Override
	public void update(float delta) {}

}
