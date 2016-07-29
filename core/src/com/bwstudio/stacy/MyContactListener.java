package com.bwstudio.stacy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bwstudio.stacy.actors.Bullet;
import com.bwstudio.stacy.actors.Enemy;
import com.bwstudio.stacy.actors.Enemy1;
import com.bwstudio.stacy.actors.MagicMikeMirror;
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
		
		// Make Stacy fall when her head hit ceiling
		if (a.getUserData().equals("head") &&
			b.getUserData().equals("ceiling")) {
			((Stacy) a.getBody().getFixtureList().first().getUserData()).setOnGround();
		}
		
		if (b.getUserData().equals("head") &&
			a.getUserData().equals("ceiling")) {
			((Stacy) b.getBody().getFixtureList().first().getUserData()).setOnGround();
		}
		
		// Dead
		if (a.getUserData() instanceof Stacy &&
			b.getUserData().equals("death")) {
			((Stacy) a.getUserData()).die();
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData().equals("death")) {
			((Stacy) b.getUserData()).die();
		}
		
		// Enemy
		if (a.getUserData() instanceof Stacy &&
			b.getUserData() instanceof Enemy) {
			Stacy s = ((Stacy) a.getUserData());
			s.giveDamage(0, s.isFacingRight() ? -50 : 50);
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData() instanceof Enemy) {
			Stacy s = ((Stacy) b.getUserData());
			s.giveDamage(0, s.isFacingRight() ? -50 : 50);
		}
		
		// Enemy sight
		if (a.getUserData() instanceof Stacy &&
			b.getUserData().equals("enemy1 sight")) {
			Enemy1 e = ((Enemy1) b.getBody().getFixtureList().get(0).getUserData());
			e.attack((Stacy) a.getUserData());
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData().equals("enemy1 sight")) {
			Enemy1 e = ((Enemy1) a.getBody().getFixtureList().get(0).getUserData());
			e.attack((Stacy) b.getUserData());
		}
		
		// Player hit
		if (a.getUserData() instanceof Stacy &&
			b.getUserData() instanceof Bullet) {
			Stacy s = (Stacy) a.getUserData();
			Bullet p = (Bullet) b.getUserData();
			s.giveDamage(0, p.knockbackForce());
			p.destroy();
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData() instanceof Bullet) {
			Stacy s = (Stacy) a.getUserData();
			Bullet p = (Bullet) b.getUserData();
			s.giveDamage(0, p.knockbackForce());
			p.destroy();
		}
		
		// Bullet hit obstacle
		if (a.getFilterData().categoryBits == Constants.BIT_OBSTACLE &&
			b.getUserData() instanceof Bullet) {
			Bullet p = (Bullet) b.getUserData();
			p.destroy();
		}
		
		if (b.getFilterData().categoryBits == Constants.BIT_OBSTACLE &&
			a.getUserData() instanceof Bullet) {
			Bullet p = (Bullet) b.getUserData();
			p.destroy();
		}

		
		// Bullet Reflect
		if (a.getUserData() instanceof Bullet &&
			b.getUserData() instanceof MagicMikeMirror) {
			Filter filter = a.getFilterData();
			filter.maskBits = (short) (a.getFilterData().maskBits | Constants.BIT_ENEMY);
			a.setFilterData(filter);
		}
		
		if (b.getUserData() instanceof Bullet &&
			a.getUserData() instanceof MagicMikeMirror) {
			Filter filter = b.getFilterData();
			filter.maskBits = (short) (b.getFilterData().maskBits | Constants.BIT_ENEMY);
			b.setFilterData(filter);
		}
		
		// Enemy Hit
		if (a.getUserData() instanceof Bullet &&
			b.getUserData() instanceof Enemy) {
			Enemy e = ((Enemy) b.getUserData());
			Bullet bu = ((Bullet) a.getUserData());
			e.giveDamage(0, a.getBody().getLinearVelocity().x > 0 ? 30 : -30);
			a.getBody().setLinearVelocity(Vector2.Zero);
			bu.destroy();
		}
		
		if (b.getUserData() instanceof Bullet &&
			a.getUserData() instanceof Enemy) {
			Enemy e = ((Enemy) a.getUserData());
			Bullet bu = ((Bullet) b.getUserData());
			e.giveDamage(0, b.getBody().getLinearVelocity().x > 0 ? 30 : -30);
			b.getBody().setLinearVelocity(Vector2.Zero);
			bu.destroy();
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
			Stacy s = ((Stacy) a.getBody().getFixtureList().first().getUserData());
			if (s.getState() != Stacy.State.HURT) s.setOffGround();
		}
		
		if (b.getUserData().equals("foot") &&
			a.getUserData().equals("ground")) {
			Stacy s = ((Stacy) b.getBody().getFixtureList().first().getUserData());
			if (s.getState() != Stacy.State.HURT) s.setOffGround();
		}
		
		// Enemy sight
		if (a.getUserData() instanceof Stacy &&
			b.getUserData().equals("enemy1 sight")) {
			Enemy1 e = ((Enemy1) b.getBody().getFixtureList().get(0).getUserData());
			e.setState(Enemy1.State.STAY_STILL);
			e.removeTarget();
		}
		
		if (b.getUserData() instanceof Stacy &&
			a.getUserData().equals("enemy1 sight")) {
			Enemy1 e = ((Enemy1) a.getBody().getFixtureList().get(0).getUserData());
			e.setState(Enemy1.State.STAY_STILL);
			e.removeTarget();
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
