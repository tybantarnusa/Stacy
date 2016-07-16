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
		
		// Warping
		if (a.getUserData() instanceof Stacy &&
			b.getUserData() instanceof Warp) {
			((Warp) b.getUserData()).teleport();
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData() instanceof Warp) {
			((Warp) a.getUserData()).teleport();
		}
		
		// On Ground
		if (a.getUserData().equals("foot") &&
			b.getUserData().equals("ground")) {
			((Stacy) a.getBody().getFixtureList().first().getUserData()).setOnGround();
		}
		
		if (b.getUserData().equals("foot") &&
			a.getUserData().equals("ground")) {
			((Stacy) b.getBody().getFixtureList().first().getUserData()).setOnGround();
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		
		if (a == null || b == null) return;
		if (a.getUserData() == null || b.getUserData() == null) return;
		
		// Off Ground
		if (a.getUserData().equals("foot") &&
			b.getUserData().equals("ground")) {
			((Stacy) a.getBody().getFixtureList().first().getUserData()).setOffGround();
		}
		
		if (b.getUserData().equals("foot") &&
			a.getUserData().equals("ground")) {
			((Stacy) b.getBody().getFixtureList().first().getUserData()).setOffGround();
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
