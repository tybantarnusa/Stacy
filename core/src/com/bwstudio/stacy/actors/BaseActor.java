package com.bwstudio.stacy.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BaseActor extends Actor {
	protected Body body;
	protected Fixture fixture;
	
	public BaseActor() {
		super();
	}
	
	public abstract void update(float delta);
	public abstract void createPhysics(World world);
	
	public Body getBody() { return body; }
	
	public abstract void dispose();
	
}
