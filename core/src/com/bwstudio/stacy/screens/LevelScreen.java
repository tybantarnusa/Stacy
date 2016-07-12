package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.actors.Stacy;

public class LevelScreen extends BaseScreen {
	
	private Stacy stacy;
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private Body owp;
	
	public LevelScreen(final MyGame game, BaseScreen prevScreen) {
		super(game, prevScreen);
		
		world = new World(new Vector2(0, -9.8f), true);
		b2dr = new Box2DDebugRenderer();
		
		// Create entities
		Texture texture = new Texture("stacy.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		stacy = new Stacy(texture);
		stacy.createPhysics(world);
		
		// Add entities to stage
		stage.addActor(stacy);
		
		createPlatform();
		owp = createOneWayPlatform();
	}

	private Body createOneWayPlatform() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(-(Constants.V_WIDTH/2f/Constants.PPM), -100/Constants.PPM);
		Body body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(100/Constants.PPM, 10/Constants.PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		shape.dispose();
		return body;
	}
	
	private void createPlatform() {
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(-(Constants.V_WIDTH/2f/Constants.PPM), -200/Constants.PPM);
		Body body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Constants.V_WIDTH/Constants.PPM, 10/Constants.PPM);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		shape.dispose();
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(game.cam.combined);
		
		stage.draw();
		
		b2dr.render(world, game.batch.getProjectionMatrix().cpy().scale(Constants.PPM, Constants.PPM, 0));
		
		game.batch.begin();
		game.batch.end();

		update(delta);
		world.step(1/60f, 6, 2);
	}
	
	float magnitude = 0;
	float oriX = game.cam.position.x;
	float oriY = game.cam.position.y;
	
	public void update(float delta) {
		stage.act();
		stacy.update(delta);
		
		if (owp.getPosition().y > stacy.getBody().getPosition().y - (stacy.getHeight()/2f)/Constants.PPM) {
			owp.getFixtureList().first().setSensor(true);
		} else {
			owp.getFixtureList().first().setSensor(false);
		}
		
		float offset = stacy.getScaleX() < 0 ? -0.75f : 0.75f;
		game.cam.position.x = (game.cam.position.x + ((stacy.getBody().getTransform().getPosition().x  + offset) * Constants.PPM - game.cam.position.x) * 0.03f);
		game.cam.position.y = 0;
		oriX = game.cam.position.x;
		oriY = game.cam.position.y;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && magnitude < 0.5f) {
			magnitude = 10;
		}
		
		magnitude = shake(magnitude);
	}
	
	private float shake(float magnitude) {
		if (magnitude > 0.5f) {	
			float x = MathUtils.random(oriX - magnitude, oriX + magnitude);
			float y = MathUtils.random(oriY - magnitude, oriY + magnitude);
			
			game.cam.position.set(x, y, 0);
			
			return magnitude - 0.5f;
		}

		game.cam.position.set(oriX, oriY, 0);
		
		return 0;
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
}
