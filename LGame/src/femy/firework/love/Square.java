package femy.firework.love;

import android.graphics.Color;

public class Square {
	public final static int TYPE_STATIC = 0;			//静态物体
	public final static int TYPE_DYNAMIC = 1;			//静态物体
	//颜色
	public int color = Color.rgb(255, 255, 255);
	public int alpha = 255;
	//转化之前的变量
	public double x, y, z, size;
	//转换之后
	public double rx, ry, rz, rsize;
	public double vx = 0, vy = 0, vz = 0;
	public double ax = 0, ay = 0, az = 0;
	public boolean visible = true;
	public int type = TYPE_STATIC;
	double G = -0.00098*2;
	int steps = 0;
	public Square(){
		x = 0;y = 0;z = 0;size = 0;
	}
	public Square(double x, double y, double z, double size){
		this.x = x; this.y = y; this.z = z; this.size = size;
	}
	public void setPosition(double x, double y, double z){
		this.x = x; this.y = y; this.z = z;
		int steps = 0;
	}
	public void physics(){
		if(this.type == this.TYPE_DYNAMIC)
		{
			this.x += this.vx; this.y += this.vy; this.z += this.vz;
			this.vx += this.ax; this.vy += this.ay; this.vz += this.az;
			this.vy += G;
			steps ++;
		}
	}
	public void des_physics(){
		if(steps <= 0 || this.type == TYPE_STATIC)
			return;
		this.vy -= G;
		this.vx -= this.ax; this.vy -= this.ay; this.vz -= this.az;
		this.x -= this.vx; this.y -= this.vy; this.z -= this.vz;
		steps --;
	}
}
