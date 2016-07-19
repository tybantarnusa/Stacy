package com.bwstudio.stacy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bwstudio.stacy.Constants;
import com.bwstudio.stacy.DataThroughLevels;
import com.bwstudio.stacy.MyContactListener;
import com.bwstudio.stacy.MyGame;
import com.bwstudio.stacy.TiledObjectUtil;
import com.bwstudio.stacy.actors.Stacy;
import com.bwstudio.stacy.actors.Warp;
import com.bwstudio.stacy.levels.Level;

public class LevelScreen extends BaseScreen {
	
	private Level level;
	
	private Stacy stacy;
	private Array<Warp> warps;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;
	private Array<Body> owpBodies;
	
	private ParticleEffect pe;
	
	private Stage hud;
	private Label label;
	private Label position;
	private Label textbox;
	private String[] textboxBuffers;
	private int textboxBuffersIndex;
	
	private InteractionState interactionState;
	
	private enum InteractionState {
		GAMEPLAY,
		INIT_TEXTBOX,
		TEXTBOX_FIRST,
		TEXTBOX,
		TEXTBOX_DONE,
		TEXTBOX_PAUSE,
		HIDE_TEXTBOX
	}
	
	public LevelScreen(final MyGame game, Level level, float startingPosX, float startingPosY, boolean faceRight) {
		super(game, null);
		
		this.level = level;
		
		// Initial interaction state
		interactionState = InteractionState.GAMEPLAY;
		
		// Create Box2D world
		world = new World(new Vector2(0, -9.8f/2f), true);
		b2dr = new Box2DDebugRenderer();
		world.setContactListener(new MyContactListener());
		
		// Create main character
		stacy = new Stacy(world, startingPosX, startingPosY);
		stacy.setJumping(DataThroughLevels.STACY_IS_JUMPING);
		stacy.setJumpTime(DataThroughLevels.STACY_JUMP_TIME);
		stacy.setState(DataThroughLevels.STACY_STATE);
		stacy.setYVelocity(DataThroughLevels.STACY_Y_VELOCITY);
		
		if (faceRight) stacy.faceRight(); else stacy.faceLeft();
		
		// Add entities to stage
		stage.addActor(stacy);
		
		// Build map
		owpBodies = new Array<Body>();
		map = level.instance().buildMap();
		tmr = new OrthogonalTiledMapRenderer(map);
		// Parsing collisions
		TiledObjectUtil.parseGround(world, map.getLayers().get("grounds").getObjects());
		if (map.getLayers().get("walls") != null)
			TiledObjectUtil.parseWall(world, map.getLayers().get("walls").getObjects());
		if (map.getLayers().get("ceilings") != null)
			TiledObjectUtil.parseCeiling(world, map.getLayers().get("ceilings").getObjects());
		if (map.getLayers().get("owp") != null)
			owpBodies = TiledObjectUtil.parseOneWayPlatforms(world, map.getLayers().get("owp").getObjects());
		if (map.getLayers().get("death") != null)
			TiledObjectUtil.parseDeathPoints(world, map.getLayers().get("death").getObjects());
		// Particle effects
		pe = new ParticleEffect();
		level.instance().buildParticle(pe);
		
		// Create warp points
		warps = level.instance().buildWarpPoints(game);
		for (Warp warp : warps) {
			warp.createPhysics(world);
		}
		
		// Build HUD
		hud = new Stage(new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT));
		label = new Label("DEVELOPMENT BUILD", new LabelStyle(new BitmapFont(), Color.WHITE));
		label.setPosition(Constants.V_WIDTH - 170, 10);
		position = new Label("X: 999.9 Y: 999.9", new LabelStyle(new BitmapFont(), Color.WHITE));
		position.setPosition(Constants.V_WIDTH - 120, Constants.V_HEIGHT - 25);
		textbox = new Label("", new LabelStyle(new BitmapFont(), Color.WHITE));
		
		Table table = new Table();
		table.setFillParent(true);
		table.top();
		table.pad(10);
		table.add(textbox).expandX().align(Align.topLeft);

		hud.addActor(table);
		hud.addActor(label);
		hud.addActor(position);
		
		// Initialize camera position
		float offset = stacy.isFacingRight() ? 0.75f : -0.75f;
		float targetPosX = (stacy.getBody().getTransform().getPosition().x + offset) * Constants.PPM;
		targetPosX = MathUtils.clamp(targetPosX, game.cam.viewportWidth / 4f, map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - game.cam.viewportWidth / 4f);
		game.cam.position.x = targetPosX;
		if (level.instance().getYBounds() != null)
			game.cam.position.y = MathUtils.clamp(game.cam.position.y, level.instance().getYBounds().x, level.instance().getYBounds().y);
		else {
			float targetPosY = (stacy.getBody().getTransform().getPosition().y) * Constants.PPM;
			targetPosY = MathUtils.clamp(targetPosY, game.cam.viewportHeight / 4f, map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - game.cam.viewportHeight / 4f);
			game.cam.position.y = targetPosY;
		}
	}
	
	private void showTextBox() {
		if (interactionState == InteractionState.GAMEPLAY) {
			stacy.setIdle();
			textboxBuffersIndex = 0;
			interactionState = InteractionState.INIT_TEXTBOX;
		}
		
		if (interactionState == InteractionState.INIT_TEXTBOX) {
			interactionState = InteractionState.TEXTBOX;
		}
	}
	
	private void finishTextBox() {
		interactionState = InteractionState.GAMEPLAY;
		textboxBuffers = null;
		textboxBuffersIndex = 0;
	}
	
	private float timeWrite = 0.025f;
	private int writingIndex = 0;
	private void writeTextBox(String s, float delta) {
		
		if (interactionState == InteractionState.TEXTBOX) {
			
			if (timeWrite > 0) {
				timeWrite -= delta;
			}
			
			if (timeWrite < 0) {
				textbox.setText(textbox.getText().toString() + s.charAt(writingIndex));
				writeTextBoxDirectly(textbox.getText().toString());
				writingIndex++;
				timeWrite = 0.025f;
			}
			
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				writeTextBoxDirectly(s);
				writingIndex = s.length();
				timeWrite = 0.025f;
			}

			if (writingIndex == s.length())
				interactionState = InteractionState.TEXTBOX_DONE;
		}
	}
	
	private void writeTextBox(String[] s) {
		if (interactionState == InteractionState.TEXTBOX || interactionState == InteractionState.TEXTBOX_PAUSE || interactionState == InteractionState.TEXTBOX_DONE) {
			textboxBuffers = s;
			writeTextBox();
		}
	}
	
	private void writeTextBox() {
		if (interactionState == InteractionState.TEXTBOX) {
			if (textboxBuffersIndex == textboxBuffers.length) {
				finishTextBox();
				return;
			}
			
			writeTextBox(textboxBuffers[textboxBuffersIndex], Gdx.graphics.getDeltaTime());
		}
		
		if (interactionState == InteractionState.TEXTBOX_DONE) {
			timeWrite -= Gdx.graphics.getDeltaTime();
			if (timeWrite < 0) {
				interactionState = InteractionState.TEXTBOX_PAUSE;
				timeWrite = 0.025f;
			}
		}
		
		if (interactionState == InteractionState.TEXTBOX_PAUSE) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
				textboxBuffersIndex++;
				writingIndex = 0;
				interactionState = InteractionState.TEXTBOX;
				textbox.setText("");
			}
		}
	}
	
	private void writeTextBoxDirectly(String s) {
		Table textBoxTable = (Table) hud.getActors().get(0);
		textBoxTable.reset();
		textBoxTable.setHeight(500);
		textbox.setText(s);
		textBoxTable.top();
		textBoxTable.add(textbox).expandX().align(Align.topLeft).pad(10);
	}

	@Override
	public void show() {
		pe.setPosition(200, 50);
		pe.scaleEffect(0.5f);
		pe.start();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
		
		level.instance().drawBackground(tmr);
		stage.draw();
		level.instance().drawForeground(tmr);
		
		game.batch.begin();
		pe.draw(game.batch);
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
		stage.act(delta);
		
		DataThroughLevels.STACY_X = stacy.getX();
		DataThroughLevels.STACY_Y = stacy.getY();
		
		// Particle effects update
		pe.update(delta);
		if (pe.isComplete()) {
			pe.reset();
		}
		
		// Update Stacy during gameplay
		if (interactionState == InteractionState.GAMEPLAY)
			stacy.update(delta);
		
		// One-Way Platforms
		for (Body owp : owpBodies) {
			Fixture owpFixture = owp.getFixtureList().first();
			Filter filterData = owpFixture.getFilterData();
			Vector2 pos = new Vector2();
			((ChainShape) owp.getFixtureList().first().getShape()).getVertex(0, pos);
			filterData.maskBits =
					(short) (pos.y * Constants.PPM > stacy.getY() + 2 ?
					Constants.BIT_ENEMY :
					Constants.BIT_ENEMY | Constants.BIT_PLAYER);

			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				filterData.maskBits = Constants.BIT_ENEMY;
			}
			
			owpFixture.setFilterData(filterData);
		}
		
		// Camera follows main character
		float offset = stacy.isFacingRight() ? 0.75f : -0.75f;
		float targetPosX = (stacy.getBody().getTransform().getPosition().x + offset) * Constants.PPM;
		targetPosX = MathUtils.clamp(targetPosX, game.cam.viewportWidth / 4f, map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - game.cam.viewportWidth / 4f);
		game.cam.position.x = game.cam.position.x + (targetPosX - game.cam.position.x) * 0.03f;
		if (level.instance().getYBounds() != null)
			game.cam.position.y = MathUtils.clamp(game.cam.position.y, level.instance().getYBounds().x, level.instance().getYBounds().y);
		else {
			float targetPosY = (stacy.getBody().getTransform().getPosition().y + 0.5f) * Constants.PPM;
			targetPosY = MathUtils.clamp(targetPosY, game.cam.viewportHeight / 4f, map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - game.cam.viewportHeight / 4f);
			game.cam.position.y = game.cam.position.y + (targetPosY - game.cam.position.y) * 0.03f;
		}
//		System.out.println("Cam: " + game.cam.position + " | Bounds: " + "(" +  game.cam.viewportWidth / 4f + ", " + (map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class) - game.cam.viewportWidth / 4f) + "), (" + game.cam.viewportHeight / 4f + ", " + (map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class) - game.cam.viewportHeight / 4f) + ")");
		
		// Debug player position
		position.setText(String.format("X: %.1f Y: %.1f", stacy.getX() + stacy.getWidth() / 2f, stacy.getY() + stacy.getHeight() / 2f));
		
		// Title and FPS counter
		Gdx.graphics.setTitle(Constants.TITLE + " | FPS: " + Gdx.graphics.getFramesPerSecond());
		
		// Textbox writer
		writeTextBox(textboxBuffers);
		
		
		// Debug new features
//		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
//			textboxBuffers = new String[] {"You smell a relaxing scent from the shroom.", "However, you feel uncomfortable when you see it."};
//			showTextBox();
//		}
//		
//		if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
//			stacy.giveDamage(0, 50);
//		}
	}
	
	public void setTextboxStrings(String[] s) {
		textboxBuffers = s;
	}
	
	public Stacy getPlayer() {
		return stacy;
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
		hud.dispose();
		stacy.dispose();
		world.dispose();
		b2dr.dispose();
		
		tmr.dispose();
		map.dispose();
	}
	
}
