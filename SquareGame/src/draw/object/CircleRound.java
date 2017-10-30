package draw.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CircleRound{
	public int centerX = 0;
	public int centerY = 0;
	private int R = 10;
	//tempAx，tempAy始终是加速度的绝对值
	private int addRSpeed = 1;
	private int tempAx = 0;
	private int tempAy = 0;
	public int WIDTH = 0;
	public int HEIGHT = 0;
	
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	private Paint paint = new Paint();
	//在屏幕坐标系统上物体的坐标，宽高
	private int thisCenterX = 0;
	private int thisCenterY = 0;
	private int thisR = 0;
	private int thisWidth = 0;
	private int thisHeight = 0;
	
	private boolean isShow = false;
	
	//世界地图的宽高
	private final int MAP_WIDTH = 8196;
	private final int MAP_HEIGHT= 8196;
	private final int MAP_RIGHT_MOVE_X = 13;
	private final int MAP_RIGHT_MOVE_Y = 13;
	public CircleRound(int screenW, int screenH){
		SCREEN_WIDTH = screenW;
		SCREEN_HEIGHT = screenH;
		//设置世界坐标中的宽高
		WIDTH = 500;
		HEIGHT = 500;
		//设置屏幕坐标中的宽高
		thisWidth = ((WIDTH * SCREEN_WIDTH) >> MAP_RIGHT_MOVE_X);
		thisHeight = ((HEIGHT * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
		thisR = ((this.R * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
		thisCenterX = ((this.centerX * SCREEN_WIDTH) >> MAP_RIGHT_MOVE_X);
		thisCenterY = ((this.centerY * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
		paint.setColor(Color.WHITE);
		paint.setAlpha(125);
	}
	public void draw(Canvas canvas){
		if(this.isShow)
		{
			R = R + (addRSpeed ++);
			if(R >= 4000)
				this.isShow = false;
			thisR = ((this.R * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
			canvas.drawCircle(thisCenterX, thisCenterY, thisR, paint);
		//	canvas.drawArc(new RectF(), startAngle, sweepAngle, useCenter, paint)
		}
	}
	public void setCenter(int x, int y){
		thisCenterX = x;
		thisCenterY = y;
		this.centerX = x * MAP_WIDTH / SCREEN_WIDTH;
		this.centerY = y * MAP_HEIGHT / SCREEN_HEIGHT;
		this.isShow = true;
		this.R = 10;
		this.addRSpeed = 0;
	}
	
}
