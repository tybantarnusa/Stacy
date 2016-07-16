package com.bwstudio.stacy;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bwstudio.stacy.actors.Stacy;
import com.bwstudio.stacy.actors.Warp;

public class MyContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();

		if (a == null || b == null) return;
		if (a.getUserData() == null || b.getUserData() == null) return;
		
		if (a.getUserData() instanceof Stacy &&
			b.getUserData() instanceof Warp) {
			((Warp) b.getUserData()).teleport();
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData() instanceof Warp) {
			((Warp) a.getUserData()).teleport();
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		if (a == null || b == null) return;
		if (a.getUserData() == null || b.getUserData() == null) return;
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
