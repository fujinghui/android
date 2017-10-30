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
	//tempAx��tempAyʼ���Ǽ��ٶȵľ���ֵ
	private int tempAx = 0;
	private int tempAy = 0;
	public int WIDTH = 0;
	public int HEIGHT = 0;
	
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	private Rect rectPosition = new Rect();
	private Paint paint = new Paint();
	//����Ļ����ϵͳ����������꣬���
	private int thisX = 0;
	private int thisY = 0;
	private int thisWidth = 0;
	private int thisHeight = 0;
	
	//�����ͼ�Ŀ��
	private final int MAP_WIDTH = 8196;
	private final int MAP_HEIGHT= 8196;
	private final int MAP_RIGHT_MOVE_X = 13;
	private final int MAP_RIGHT_MOVE_Y = 13;
	//���Ʒ������
	public boolean isMove = true;
	public boolean isPitchOn = false;
	public boolean isAcceleratedSpeed = false;
	public Diamond(int screenW, int screenH){
		SCREEN_WIDTH = screenW;
		SCREEN_HEIGHT = screenH;
		//�������������еĿ��
		WIDTH = 500;
		HEIGHT = 500;
		//������Ļ�����еĿ��
		thisWidth = ((WIDTH * SCREEN_WIDTH) >> MAP_RIGHT_MOVE_X);
		thisHeight = ((HEIGHT * SCREEN_HEIGHT) >> MAP_RIGHT_MOVE_Y);
		paint.setColor(Color.WHITE);
	}
	public void draw(Canvas canvas){
		if(isMove)//��ǰ������ƶ�������������ƶ�
		{
//			if(isAcceleratedSpeed)	//�����м��ٶȵĿ��Ʊ���
//			{
//				//���ٶ�
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
//					isAcceleratedSpeed = false;		//��������ٶȿ���ʹ��
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
	
	public boolean isContains(Context context, int x,int y){	//�жϸõ��Ƿ�����ڵ�ǰ�ķ�����
		//����Ļ����ת��Ϊ��������ϵ
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
		this.mx = toWorldX >> 5;	//ת�����겢�Ҽ����ƶ��ٶ�
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
