package tools;

import android.graphics.Color;
import android.graphics.Paint;

public class Paints {
	private int MAX = 7;
	public Paint WHITE_PAINT;
	public Paint BLACK_PAINT;
	public Paint BLUE_PAINT;
	public Paint RED_PAINT;
	public Paints(){
		WHITE_PAINT = new Paint();
		BLACK_PAINT = new Paint();
		BLUE_PAINT = new Paint();
		RED_PAINT = new Paint();
		
		WHITE_PAINT.setColor(Color.WHITE);
		BLACK_PAINT.setColor(Color.BLACK);
		BLUE_PAINT.setColor(Color.BLUE);
		RED_PAINT.setColor(Color.RED);
		WHITE_PAINT.setTextSize(25);
		
	}
}
