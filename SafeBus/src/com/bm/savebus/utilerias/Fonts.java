package com.bm.savebus.utilerias;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {

	public static final int FLAG_BLACK = 1;
	public static final int FLAG_THIN = 3;


	private static Context activity;

	public Fonts(Context context) {
		Fonts.activity = context;
	}

	/**
	 * tipo de fuente
	 * @param tipo (int) tipo de la fuente
	 * @return (Typeface) fuente
	 */
	public Typeface getTypeFace(int tipo) {
		Typeface tf = null;
		if (tipo == FLAG_BLACK) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/Lato-Black.ttf");
		}else if (tipo == FLAG_THIN) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/Lato-Thin.ttf");
		}
		return tf;
	}
}