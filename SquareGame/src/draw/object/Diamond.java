package draw.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

public class Diamond {
	public int x = 0;
	public int y = 0;
	private int mx = 0;
	private int my = 0;
	private int ax = 0;
	private int ay = 0;
	//tempAx，tempAy始终是加速度的绝对值
	private int tempAx = 0;
	private int tempAy = 0;
	public int WIDTH = 0;
	public int HEIGHT = 0;
	
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	private Rect rectPosition = new Rect();
	private Paint paint = new Paint();
	//在屏幕坐标系统上物体的坐标，宽高
	private int thisX = 0;
	private int thisY = 0;
	private int thisWidth = 0;
	private int thisHeight = 0;
	
	//世界地图的宽高
	private final int MAP_WIDTH = 8196;
	private final int MAP_HEIGHT= 8196;
	private final int MAP_RIGHT_MOVE_X = 13;
	private final int MAP_RIGHT_MOVE_Y = 13;
	//控制方块变量
	public boolean isMove = true;
	public boolean isPitchOn = false;
	public boolean isAcceleratedSpeed = false;
	public Diamond(int screenW, int screenH){
		SCREEN_WIDTH = screenW;
		SCREEN_HEIGHT = screenH;
		//设置世界坐标中的宽高
		WIDTH = 500;
		HEIGHT = 500;
		//设置屏幕坐标中的宽高
		thisWidth = ((WIDTH * SCREEN_WIDTH) >> MAP_RIGHT_MOVE_X);
		thisHeight = ((HEIGHT * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
		paint.setColor(Color.WHITE);
	}
	public void draw(Canvas canvas){
		if(isMove)//当前物体可移动，才让物体可移动
		{
//			if(isAcceleratedSpeed)	//允许有加速度的控制变量
//			{
//				//加速度
//			//	this.mx = this.mx + this.ax;
//			//	this.my = this.my + this.ay;
//				if(this.mx >= -this.tempAx && this.mx <= this.tempAx)
//				{
//					this.ax = 0;
//					this.ay = 0;
//					this.tempAx = 0;
//					this.tempAy = 0;
//					this.mx = 0;
//					this.my = 0;
//					isAcceleratedSpeed = false;		//不允许加速度可以使用
//				}
//			}
			this.x += this.mx;
			this.y += this.my;
			if(this.x < 0)
				this.x = 0;
			else if(this.x + this.WIDTH > this.MAP_WIDTH)
				this.x = this.MAP_WIDTH - this.WIDTH;
			if(this.y < 0)
				this.y = 0;
			else if(this.y + this.HEIGHT > this.MAP_HEIGHT)
				this.y = this.MAP_HEIGHT - this.HEIGHT;
			
			thisX = x * SCREEN_WIDTH >> MAP_RIGHT_MOVE_X;
			thisY = y * SCREEN_HEIGHT >> MAP_RIGHT_MOVE_Y;
		}
		rectPosition.set(thisX, thisY, thisX + thisWidth, thisY + thisHeight);
		canvas.drawRect(rectPosition, paint);
//		canvas.drawLines(, paint)
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
		rectPosition.set(x * SCREEN_WIDTH / MAP_WIDTH, y * SCREEN_HEIGHT / MAP_HEIGHT, x * SCREEN_WIDTH / MAP_WIDTH + WIDTH * SCREEN_WIDTH / MAP_WIDTH, y * SCREEN_HEIGHT / MAP_HEIGHT + HEIGHT * SCREEN_HEIGHT / MAP_HEIGHT);
	}
	
	public boolean isContains(Context context, int x,int y){	//判断该点是否包含在当前的方块中
		//将屏幕坐标转换为世界坐标系
		int tempX = (x << MAP_RIGHT_MOVE_X) / SCREEN_WIDTH;
		int tempY = (y << MAP_RIGHT_MOVE_Y) / SCREEN_HEIGHT;
		//Toast.makeText(context, "x:"+x+"\ndown x:" + tempX, Toast.LENGTH_LONG).show();
		if(tempX >= this.x && tempX <= this.x + this.WIDTH)
		{
			if(tempY >= this.y && tempY <= this.y + this.HEIGHT)
			{
				return true;
			}
		}
		return false;
	}
	public void setMoveSpeed(int toX, int toY){
		int toWorldX = ((toX * MAP_WIDTH) / SCREEN_WIDTH - x);
		int toWorldY = ((toY * MAP_HEIGHT) / SCREEN_HEIGHT - y);
		this.mx = toWorldX >> 5;	//转化坐标并且计算移动速度
		this.my = toWorldY >> 5;
		this.ax = -this.mx >> 8;
		this.ay = -this.my >> 8;
		this.tempAx = ax>0 ? ax : -ax;
		this.tempAy = ay>0 ? ay : -ay;
		
	}
	public void setWH(int w, int h){
		this.WIDTH = w;
		this.HEIGHT = h;
		this.thisWidth = this.WIDTH * this.SCREEN_WIDTH / this.MAP_WIDTH;
		this.thisHeight = this.HEIGHT * this.SCREEN_HEIGHT / this.MAP_HEIGHT;
	}
	
	public void setColor(int color){
		paint.setColor(color);
	}
}
