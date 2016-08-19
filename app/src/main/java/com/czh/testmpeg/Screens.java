package com.czh.testmpeg;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class Screens {

	@SuppressWarnings("deprecation")
	/**
	 * M�todo que devuelve el alto de la pantalla
	 * 
	 * @param context Context de la aplicaci�n
	 * 
	 * @return int con la altura
	 */
	public static int getScreenHeight(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getHeight();

	}

	@SuppressWarnings("deprecation")
	/**
	 * M�todo que devuelve el ancho de la pantalla
	 * 
	 * @param context Context de la aplicaci�n
	 * 
	 * @return int con la ancho
	 */
	public static int getScreenWidth(Context context) {

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();

	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	/**
	 * Valida si el dispositivo es una tableta
	 * 
	 * @param context Context de la aplicaci�n
	 * @return true si es una tablet
	 *         false si es un m�vil
	 */
	public static Boolean isTableta(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
				|| ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Valida si el dispositivo es una tableta de tama�o large (is at least approximately 480x640 dp units)
	 * 
	 * @param context Context de la aplicaci�n
	 * @return true si es una tablet de tama�o Large
	 *         false si es no lo es
	 */
	public static Boolean isTabletaLarge(Context context) {

		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Valida si el dispositivo es una tableta de tama�o large (is at least approximately 720x960 dp units)
	 * 
	 * @param context Context de la aplicaci�n
	 * @return true si es una tablet de tama�o Large
	 *         false si es no lo es
	 */
	public static Boolean isTabletXLarge(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Valida si el dispositivo tiene un tama�o de pantalla standard (is at least approximately 320x470 dp units)
	 * 
	 * @param context Context de la aplicaci�n
	 * @return true si el tama�o de pantalla es standard
	 *         false si es no lo es
	 */

	public static Boolean isNormal(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Valida si el dispositivo es una tableta de tama�o peque�o (is at least approximately 320x426 dp units)
	 * 
	 * @param context Context de la aplicaci�n
	 * @return true si es m�vil peque�o
	 *         false si es no lo es
	 */
	public static Boolean isSmall(Context context) {
		if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Valida la orientaci�n del dispositivo
	 * 
	 * @param ctx Context de la aplicaci�n
	 * 
	 * @return true si la pantalla est� en vertical
	 *         false si est� en horizontal
	 */
	public static boolean isVertical(Context ctx) {
		if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			return true;
		else
			return false;
	}

	/**
	 * Tama�o de la pantalla
	 * 
	 * @param display display
	 * 
	 * @return Point tama�o de la pantalla
	 */
	public static Point getDisplaySize(final Display display) {
		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		return point;
	}
	
	// navigation bar (at the bottom of the screen on a Nexus device)
		public static int getNavigationBarHeight(Context ctx) {
			Resources resources = ctx.getResources();
			int resourceId = resources.getIdentifier("navigation_bar_height",
					"dimen", "android");
			if (resourceId > 0) {
				return resources.getDimensionPixelSize(resourceId);
			}
			return 0;
		}

		// navigation bar (at the bottom of the screen on a Nexus device)
		public static int getStatusBarHeight(Context ctx) {
			Resources resources = ctx.getResources();
			int resourceId = resources.getIdentifier("status_bar_height", "dimen",
					"android");
			if (resourceId > 0) {
				return resources.getDimensionPixelSize(resourceId);
			}
			return 0;
		}

}
