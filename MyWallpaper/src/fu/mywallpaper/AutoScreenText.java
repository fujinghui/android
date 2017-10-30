package fu.mywallpaper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.util.Log;

public class AutoScreenText {
	private int SW, SH, SX, SY;
	private int TW, TH;
	private int TextSize = 30;
	private Paint paint;
	public AutoScreenText(Context context){
		init();
	}
	public AutoScreenText(){
		init();
	}
	private void init(){
		paint = new Paint();
		paint.setTextSize(TextSize);
		paint.setColor(Color.GREEN);
		SX = 0; SY = 0;
		SW = 100; SH = 100;
	}
	public void setScreenWH(int w, int h){
		this.SW = w;
		this.SH = h;
	}
	public void setScreenXY(int x, int y){
		this.SX = x;
		this.SY = y;
	}
	public void setTextSize(int size){
		this.TextSize = size;
		paint.setTextSize(size);
	}
	public void showText(Canvas canvas, String text){
		int textLength = text.length();
		int i;
		int positionX = SX, positionY = SY;
		String buffer = null;
		for(i = 0; i < textLength; i ++)
		{
			buffer = text.substring(i, i + 1);
			positionX += (TextSize/2);
			if(positionX + TextSize > (SX + SW) || buffer.equals("-"))
			{
				positionX = SX;
				positionY += (TextSize);
			}
			if(positionY + TextSize > (SY + SH))
				break;
			if(!buffer.equals("-"))
				canvas.drawText(buffer, positionX, positionY, paint);
		}
	}
	int scroll_y = 0;
	private boolean isShow = true;
	String bottom_alert = "_";
	public void showText(Canvas canvas, String text, int start, int end){
		int i;
		int positionX = SX, positionY = SY;
		String buffer = null;
		if(end == 0)
			scroll_y = 0;
		for(i = start; i < end; i ++)
		{
			buffer = text.substring(i, i + 1);
			positionX += (TextSize/2);
			if(positionX + TextSize >= (SX + SW) || (buffer.equals("-")&&i!=end-1))
			{
				positionX = SX;
				positionY += (TextSize);
			}
			if(positionY + scroll_y + TextSize >= (SY + SH))
				scroll_y -= TextSize;
			if(!buffer.equals("-") && positionY + scroll_y >= SY && positionY + scroll_y <= (SY + SH))
				canvas.drawText(buffer, positionX, positionY + scroll_y, paint);
		}
		if(isShow)
		{
			positionX += (TextSize/2);
			canvas.drawText(bottom_alert, positionX, positionY + scroll_y, paint);
			isShow = false;
		}
		else
			isShow = true;
	}
}
