package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.TiledObjectUtil;
import com.bwstudio.stacy.actors.Stacy;

public class LevelGardenScreen extends BaseScreen {
	
	private Stacy stacy;
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;
	
	private Stage hud;
	private Label label;
	
	public LevelGardenScreen(final MyGame game, BaseScreen prevScreen) {
		super(game, prevScreen);
		
		world = new World(new Vector2(0, -9.8f), true);
		b2dr = new Box2DDebugRenderer();
		game.cam.position.x = game.cam.viewportWidth / 4f;
		
		// Create entities
		Texture texture = new Texture("stacy.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		stacy = new Stacy(texture);
		stacy.setPosition(50, game.cam.position.y + 96);
		stacy.createPhysics(world);
		
		// Add entities to stage
		stage.addActor(stacy);
		
		createPlatform();
		
		// Build map
		map = new TmxMapLoader().load("maps/garden-inner-00.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		TiledObjectUtil.parseTiledObjectLayer(world, map.getLayers().get("collisions").getObjects());
		
		hud = new Stage(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT));
	
		label = new Label("DEVELOPMENT BUILD", new LabelStyle(new BitmapFont(), Color.WHITE));
		label.setPosition(Constants.V_WIDTH - 170, 10);
		hud.addActor(label);
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
		update(delta);
		tmr.render(new int[] {0});
		stage.draw();
		tmr.render(new int[] {1});
		game.batch.begin();
		hud.draw();
		game.batch.end();
		
//		b2dr.render(world, game.batch.getProjectionMatrix().cpy().scale(Constants.PPM, Constants.PPM, 0));
		
		world.step(1/60f, 6, 2);
	}
	
	public void update(float delta) {
		game.batch.setProjectionMatrix(game.cam.combined);
		game.cam.update();
		game.cam.zoom = 0.5f;
		tmr.setView(game.cam);
		stage.act();
		stacy.update(delta);
		
		// Camera follows main character
		float offset = stacy.getScaleX() < 0 ? -0.75f : 0.75f;
		float targetPosX = (stacy.getBody().getTransform().getPosition().x  + offset) * Constants.PPM;
		targetPosX = MathUtils.clamp(targetPosX, game.cam.viewportWidth / 4f, map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - game.cam.viewportWidth / 4f);
		game.cam.position.x = (game.cam.position.x + (targetPosX - game.cam.position.x) * 0.03f);
		game.cam.position.y = MathUtils.clamp(game.cam.position.y, game.cam.viewportHeight / 4f - 32, Integer.MAX_VALUE);
		Gdx.graphics.setTitle(Constants.TITLE + " | FPS: " + Gdx.graphics.getFramesPerSecond());
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
		world.dispose();
		b2dr.dispose();
		
		tmr.dispose();
		map.dispose();
	}
	
}
