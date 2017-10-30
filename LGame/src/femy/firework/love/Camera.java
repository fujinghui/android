package femy.firework.love;

import android.util.Log;

public class Camera {
	public double camera_x = 0, camera_y = 2.0, camera_z = -8;
	public double camera_near_z = 1, camera_far_z = 100;
	public double camera_to_x = 0, camera_to_y = 0, camera_to_z = 0;
	
	
	public double camera_rotate_hor = 0.0f;
	public double camera_rotate_ver = 0.0f;
	
	public int screen_width, screen_height;
	private double cx, cy;
	
	public Camera(int width, int height){
		this.screen_width = width;
		this.screen_height = height;
		this.cx = width*1.0 / 2.0;
		this.cy = height*1.0 / 2.0;
	}
	
	public Camera(){
		
	}
	
	public static double angle = 0.00;
	public static double angle_hor = 0;
	public static double v = 0.1;
	public void transate(Square a){	
		//转化成相机坐标
		a.rx = a.x - camera_x; 
		a.ry = a.y - camera_y;
		a.rz = a.z - camera_z;

		//处理摄像机朝向的问题（暂时不处理，留待以后处理）
		double dx = camera_to_x - camera_x;
		double dy = camera_to_y - camera_y;
		double dz = camera_to_z - camera_z;
		
		
		double angle_hor = Math.atan2(a.rz, a.rx);
		//Log.d("myDebug", angle_hor+"");
		angle_hor += Math.PI / 100000;
		double r = Math.sqrt(a.rx * a.rx + a.rz * a.rz);
		a.rx = r * Math.cos(angle_hor + angle);
		a.rz = r * Math.sin(angle_hor + angle);
		//Log.d("myDebug", "angle:"+angle_hor);
		//Log.d("myDebug", angle_hor+"");
		//开始旋转
		//a.rx = a.rx * Math.cos(camera_rotate_hor) - a.rz * Math.sin(camera_rotate_hor);
		//a.rz = a.rx * Math.sin(camera_rotate_hor) + a.rz * Math.cos(camera_rotate_hor);
		
		//angle += 0.0001;
		
		//超出屏幕，不显示
		if(a.rz < this.camera_near_z)
		{
			a.visible = false;
		}
		else
		{
			a.visible = true;
			//转换到屏幕坐标
			a.rx = a.rx * (this.camera_near_z)/ a.rz;
			a.ry = a.ry * (this.camera_near_z)/ a.rz;
			if(a.rx >= 1 || a.rx <= -1 || a.ry >= 1 || a.ry <= -1)
				a.visible = false;
			else
			{
				a.rx = (cx + a.rx * cx);
				a.ry = (cy - a.ry * cy);
				
				a.rsize = a.size *  this.camera_near_z / (a.rz);
			}
		}
		
	}
}
