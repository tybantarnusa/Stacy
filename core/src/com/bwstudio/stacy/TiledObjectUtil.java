package com.bwstudio.stacy;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class TiledObjectUtil {
	
	public static void parseGround(World world, MapObjects objects) {
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
			fdef.friction = 0.5f;
			fdef.filter.categoryBits = Constants.BIT_OBSTACLE;
			fdef.filter.maskBits = (short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("ground");
			shape.dispose();
		}
	}
	
	public static void parseWall(World world, MapObjects objects) {
		for (MapObject object : objects) {
			Shape shape;
			
			if (object instanceof PolylineMapObject) {
				shape = createPolyline((PolylineMapObject) object);
			} else if (object instanceof PolygonMapObject) {
				shape = createPolygon((PolygonMapObject) object);
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
			fdef.friction = 0.5f;
			fdef.filter.categoryBits = Constants.BIT_OBSTACLE;
			fdef.filter.maskBits = (short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("wall");
			shape.dispose();
		}
	}
	
	public static void parseCeiling(World world, MapObjects objects) {
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
			fdef.friction = 0.5f;
			fdef.filter.categoryBits = Constants.BIT_OBSTACLE;
			fdef.filter.maskBits = (short) (Constants.BIT_PLAYER | Constants.BIT_ENEMY);
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("ceiling");
			shape.dispose();
		}
	}
	
	public static Array<Body> parseLights(World world, MapObjects objects) {
		Array<Body> lights = new Array<Body>();
		for (MapObject object : objects) {
			CircleShape shape;
			
			if (object instanceof EllipseMapObject) {
				shape = createEllipse((EllipseMapObject) object);
			} else {
				continue;
			}
			
			Body body;
			BodyDef bdef = new BodyDef();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set(shape.getPosition());
			body = world.createBody(bdef);
			String amount = object.getProperties().get("amount", String.class);
			if (amount == null)
				body.setUserData(120f);
			else
				body.setUserData(Float.parseFloat(amount));
			shape.dispose();
			lights.add(body);
		}
		return lights;
	}
	
	public static void parseDeathPoints(World world, MapObjects objects) {
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
			fdef.friction = 0.5f;
			fdef.filter.categoryBits = Constants.BIT_OBSTACLE;
			fdef.filter.maskBits = (short) Constants.BIT_PLAYER;
			fdef.isSensor = true;
			Fixture fixture = body.createFixture(fdef);
			fixture.setUserData("death");
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
			fdef.filter.maskBits = (short) (Constants.BIT_ENEMY);
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
	
	private static PolygonShape createPolygon(PolygonMapObject polygon) {
		float[] vertices = polygon.getPolygon().getTransformedVertices();
		float[] worldVertices = new float[vertices.length];
		
		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = vertices[i] / Constants.PPM;
		}
		
		PolygonShape ps = new PolygonShape();
		ps.set(worldVertices);
		return ps;
	}
	
	private static CircleShape createEllipse(EllipseMapObject circleObject) {
		Ellipse ellipse = circleObject.getEllipse();
		CircleShape cs = new CircleShape();
		cs.setPosition(new Vector2(ellipse.x / Constants.PPM, ellipse.y / Constants.PPM));
		cs.setRadius(ellipse.width / Constants.PPM);
		return cs;
	}
}
