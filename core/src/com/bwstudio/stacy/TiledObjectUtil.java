package com.bwstudio.stacy;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class TiledObjectUtil {
	
	public static void parseTiledObjectLayer(World world, MapObjects objects) {
		for (MapObject object : objects) {
			Shape shape;
			
			if (object instanceof PolylineMapObject) {
				shape = createPolyline((PolylineMapObject) object);
			} else {
				continue;
			}
			
			Body body;
			BodyDef bdef = new BodyDef();
			bdef.type = BodyDef.BodyType.StaticBody;
			body = world.createBody(bdef);
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.density = 1.0f;
			fdef.filter.categoryBits = Constants.BIT_GROUND;
			fdef.filter.categoryBits = (short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("ground");
			shape.dispose();
		}
	}
	
	public static Array<Body> parseOneWayPlatforms(World world, MapObjects objects) {
		Array<Body> owpBodies = new Array<Body>();
		for (MapObject object : objects) {
			Shape shape;
			
			if (object instanceof PolylineMapObject) {
				shape = createPolyline((PolylineMapObject) object);
			} else {
				continue;
			}
			
			Body body;
			BodyDef bdef = new BodyDef();
			bdef.type = BodyDef.BodyType.StaticBody;
			body = world.createBody(bdef);
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.density = 1.0f;
			fdef.filter.categoryBits = Constants.BIT_OWP;
			fdef.filter.categoryBits = (short) (Constants.BIT_ENEMY);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("owp");
			shape.dispose();
			owpBodies.add(body);
		}
		return owpBodies;
	}
	
	private static ChainShape createPolyline(PolylineMapObject polyline) {
		float[] vertices = polyline.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];
		
		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
		}
		
		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}
	
}
