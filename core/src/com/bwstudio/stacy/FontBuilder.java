package com.bwstudio.stacy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontBuilder {
	public static BitmapFont build(int size, Color color, String font) {
		FreeTypeFontParameter par = new FreeTypeFontParameter();
		par.size = size;
		par.color = color;
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(font));
		BitmapFont generated = gen.generateFont(par);
		gen.dispose();
		return generated;
	}
}
