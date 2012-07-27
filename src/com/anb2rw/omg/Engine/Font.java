
package com.anb2rw.omg.Engine;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
//import android.util.Log;

/**
 * @author impaler
 *
 */
public class Font {

	//private static final String TAG = Font.class.getSimpleName();
	private Bitmap bitmap;	// bitmap containing the character map/sheet

	// Map to associate a bitmap to each character
	private Map<Character, Bitmap> chars = new HashMap<Character, Bitmap>(78);

	private int width;	// width in pixels of one character
	private int height;	// height in pixels of one character

	// the characters in the English alphabet
	private char[] charactersEN = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z' };
	private char[] charactersRU = new char[] { 'à', 'á', 'â', 'ã', 'ä', 'å', '¸',
			'æ', 'ç', 'è', 'é', 'ê', 'ë', 'ì', 'í', 'î', 'ï', 'ð', 'ñ', 'ò',
			'ó', 'ô', 'õ', 'ö', '÷', 'ø', 'ù', 'ú', 'û', 'ü', 'ý', 'þ', 'ÿ' };
	private char[] numbers = new char[] { '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0' };
        private char[] punctuation = new char[] { '!', '?', ',', '.', '<', '>', '-', '+', ' ' };

	public Font(Bitmap bitmap) {
		super();
		this.bitmap = bitmap;
		this.width = 28;
		this.height = 28;
		// Cutting up the glyphs
		// Starting with the first row - lower cases
		for (int i = 0; i < 26; i++) {
			chars.put(charactersEN[i], Bitmap.createBitmap(bitmap,
					0 + (i * width), 0, width, height));
		}
		//Log.d(TAG, "Lowercases initialised");

		// Continuing with the second row - upper cases
		// Note that the row starts at 15px - hardcoded
		for (int i = 0; i < 33; i++) {
			chars.put(charactersRU[i], Bitmap.createBitmap(bitmap,
					0 + (i * width), height, width, height));
		}
		// row 3 for numbers
		//Log.d(TAG, "Uppercases initialised");
		for (int i = 0; i < 10; i++) {
			chars.put(numbers[i], Bitmap.createBitmap(bitmap,
					0 + (i * width), 2*height, width, height));
		}
		//Log.d(TAG, "Numbers initialised");

		for (int i = 0; i < 9; i++) {
			chars.put(punctuation[i], Bitmap.createBitmap(bitmap,
					0 + (i * width), 3*height, width, height));
		}
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
        
        public int stringWidth(String text, float scale) {
            return (int)(width*scale)*text.length();
        }

	/**
	 * Draws the string onto the canvas at <code>x</code> and <code>y</code>
	 * @param text
	 */
	public void drawString(Canvas canvas, String text, int x, int y, float scale, Paint paint) {
		if (canvas == null) {
			//Log.d(TAG, "Canvas is null");
                    return;
		}
                //float size=0.5f;
                Matrix m=new Matrix();
                m.setScale(scale, scale);
                m.postTranslate(x, y);
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.toLowerCase().charAt(i);
			if (chars.get(ch) != null) {
                            canvas.drawBitmap(chars.get(ch).extractAlpha(), m, paint);
			}
                        m.postTranslate(width*scale, 0);
		}
	}
}
