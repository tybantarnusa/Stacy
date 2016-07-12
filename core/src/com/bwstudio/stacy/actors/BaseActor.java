package com.bwstudio.stacy.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class BaseActor extends Image {
	protected Body body;
	protected Fixture fixture;
	
	public BaseActor(Texture texture) {
		super(texture);
	}
	
	protected abstract void update(float delta);
	protected abstract void createPhysics(World world);
	
	public Body getBody() { return body; }
	
}
