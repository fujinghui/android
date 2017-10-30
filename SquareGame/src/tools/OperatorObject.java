package tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.Toast;

public class OperatorObject{
	public int x, y;
	public int MoveR;
	public int mx, my;
	public int r = 20;
	public double angle;
	public double moveAngle = 0.1;
	public double endAngle;
	public Context context;
	private int centerX,centerY;
	private Paint paint;
	private RectF rect;
	private boolean isMove = false;
	public OperatorObject(){
		paint = new Paint();
		paint.setColor(Color.WHITE);	//设置小球的显示颜色
		rect = new RectF();
		rect.set(x, y, x + r, y + r);	//设置小球要显示的位置
	}
	public void drawSelf(Canvas canvas){
		if(isMove)
		{
			int newX,newY;
			x = (int) (centerX - MoveR * Math.cos(angle));
			y = (int) (centerY - MoveR * Math.sin(angle));
			newX = x;
			newY = y;
			rect.set(newX , newY, newX + r, newY + r);
			angle = angle + moveAngle;					//角度增加
			if(angle > endAngle)
			{
				isMove = false;							//已经旋转了180度，不再旋转
			//	x = newX;
			//	y = newY;
			}
		}
		canvas.drawArc(rect, 0, 360, false, paint);
	}
	public void setMoveTo(int ToX, int ToY){
		double distance = Math.sqrt((double)((ToX - x) * (ToX - x) + (ToY - y) * (ToY - y)));
		MoveR = (int)distance / 2;
		double sinx = (double)((ToY - y) / distance);
		if(MoveR >= 10 && sinx < 1.0)
		{
			isMove = true;
			angle = arcsin(sinx);//-Math.asin((double)((ToY - y)/(ToX - x)));			//求解两个点之间的与x轴之间的角度
			endAngle = angle + Math.PI;
			centerX = (ToX + x) / 2;
			centerY = (ToY + y) / 2;
//			rect.set((float)(centerX - MoveR), (float)centerY - MoveR, (float)centerX + MoveR, (float)centerY + MoveR);
		}
	}
	private double arcsin(double y){
		double minAngle = 0.0, maxAngle = Math.PI / 2.0;
		double x = (minAngle + maxAngle) / 2.0;
		int loop = 40;
		double sin_x;
		while(loop > 0)
		{
			sin_x = Math.sin(x);
			if(sin_x > y)
				minAngle = x;
			else if(sin_x < y)
				maxAngle = y;
			else
				return x;
			loop --;
		}
		return x;
	}
}
