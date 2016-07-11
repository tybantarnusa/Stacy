package com.bwstudio.stacy.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class BaseActor extends Image {
	
	public BaseActor(String imagePath) {
		super(new Texture(imagePath));
	}
	
}
