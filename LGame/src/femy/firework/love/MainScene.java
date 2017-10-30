package femy.firework.love;

import java.util.ArrayList;

import android.graphics.Color;
import android.util.Log;
import femy.framework.base.Game;
import femy.framework.base.Screen;
import femy.framework.game.Assets;

public class MainScene extends Screen{
	/*
	 * 与3D图形有关的函数
	 */
	private Camera camera;
	private double cmz = 0.004;
	private double angle = 0.003;
	int count = 80;						//一次爆炸20个粒子
	int physics_steps = 10;
	private int cx, cy;
	
	private ArrayList<Square> heap = new ArrayList<Square>();
	
	private ArrayList<Square> ground = new ArrayList<Square>();
	private ArrayList<Square> particle = new ArrayList<Square>();
	private ArrayList<Square> textlist = new ArrayList<Square>();
	
	private int heap_max = 400;
	
	private void drawSquare(Square a){
		camera.transate(a);
		if(a.visible)
		{
			/*
			double tx = a.rx, ty = a.ry, tz = a.rz;
			double vx = a.vx, vy = a.vy, vz = a.vz;
			double ax = a.ax, ay = a.ay, az = a.az;
			//画出他的五个轨迹
			double size = a.rsize;
			for(int i = physics_steps; i > 0; i --)
			{
				camera.transate(a);
				game.getGraphics().drawRect((int)a.rx, (int)a.ry, (int)size*i/10, (int)size*i/10, Color.argb(200*i/10, 255, 25, 90));
				a.des_physics();
				a.des_physics();
			}
			for(int i = 0; i < physics_steps; i ++)
			{
				a.physics();
				a.physics();
			}*/
			
			game.getGraphics().drawRect((int)a.rx, (int)a.ry, (int)a.rsize, (int)a.rsize, Color.argb(200, 255, 25, 90));
			//game.getGraphics().drawPixmap(Assets.sparks[0], (int)a.rx, (int)a.ry, (int)a.rsize, (int)a.rsize, 0, 0, Assets.sparks[0].getWidth(), Assets.sparks[0].getHeight());
		}
	}
	private Square getSquare(){
		if(heap.size() == 0)
			return null;
		Square obj = heap.get(0);
		heap.remove(0);
		return obj;
	}
	//产生地面
	private void productGround(){
		int x_count = 10, z_count = 10;
		int bian = 10;
		for(int i = 0; i < x_count; i ++)
		{
			for(int j = 0; j < z_count; j ++)
			{
				Square t = getSquare();
				if(t != null)
				{
					t.x = bian * i*1.0/x_count - bian/2;
					t.z = bian * j*1.0/z_count - bian/2;
					t.y = 0;
					t.size = 40;t.vx = 0;t.vy = 0;t.vz = 0;
					t.G = 0;
					ground.add(t);
				}
			}
		}
	}
	/*
	 * 3D图形相关结构
	 */
	//产生一个烟花
	private void productFirework(double x, double y, double z){
		double angle_hor, angle_ver;
		double v = 0.08;
		for(int i = 0; i < count && heap.size() > 0; i ++)
		{
			Square s = getSquare();
			angle_hor = Math.PI * 2 * Math.random();
			angle_ver = Math.PI * 2 * Math.random();
			s.setPosition(x, y, z);
			s.vx = v * Math.sin(angle_ver)*Math.cos(angle_hor);
			s.vz = v * Math.sin(angle_ver)*Math.sin(angle_hor);
			s.vy = 0.05+ v* Math.cos(angle_ver);
//			s.size = 100;
			s.type = Square.TYPE_DYNAMIC;
			
			particle.add(s);
		}
	}
	private void productText(){
		//你
		char text2[] = {
				0x11,0x00,0x11,0x00,0x11,0x00,0x23,0xFC,0x22,0x04,0x64,0x08,0xA8,0x40,0x20,0x40,
				0x21,0x50,0x21,0x48,0x22,0x4C,0x24,0x44,0x20,0x40,0x20,0x40,0x21,0x40,0x20,0x80};
		char text3[] = {
				0x00,0x00,0x00,0x00,0x10,0x00,0xF8,0x3F,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
				0x00,0x00,0x00,0x00,0x00,0x00,0x04,0x00,0xFE,0xFF,0x00,0x00,0x00,0x00,0x00,0x00
		};
		//爱
		char text[] = {
				0x78,0x00,0x80,0x3F,0x10,0x11,0x20,0x09,0xFE,0x7F,0x02,0x42,0x04,0x82,0xF8,0x7F,
				0x00,0x04,0xF0,0x07,0x20,0x0A,0x40,0x09,0x80,0x10,0x60,0x11,0x1C,0x22,0x08,0x0C};
		for(int i = 0; i < 16; i ++)
		{
			for(int j = 0; j < 8; j ++)
			{
				if(((text[i*2+1]>>(7-j))&0x01) == 1)
				{
					Square t = getSquare();
					t.setPosition(j - 8, 16-i, 8);
					t.size = 100;
					textlist.add(t);
				}
			}
			for(int j = 0; j < 8; j ++)
			{
				if(((text[i*2]>>(7-j))&0x01) == 1)
				{
					Square t = getSquare();
					t.size = 100;
					t.setPosition(j, 16-i, 8);
					textlist.add(t);
				}
			}
		}
	}
	private long time = 0;
	//处理烟花逻辑
	public void dealLogic(){
		for(int i = 0; i < particle.size(); i ++)
		{
			Square t = particle.get(i);
			if((t.vx)*(t.vx)+(t.vz*t.vz) < 0.000001)
			{
				//将该对象回收到堆中
				heap.add(particle.get(i));
				//删除该对象
				particle.remove(i);
			}
			else
			{
				particle.get(i).physics();
				particle.get(i).vx /= 1.06;
				particle.get(i).vy /= 1.06;
				particle.get(i).vz /= 1.06;
			}
		}
		if(System.currentTimeMillis() - time > 2000)
		{
			time = System.currentTimeMillis();
			if(heap.size()>0)
			{
				
				productFirework(8*Math.random()-4, 3+Math.random(), 8*Math.random()-4);
			}
		}
		boolean flag = false;
		
		//处理加速度传感器的输入
		if(game.getInput().getAccelX() > 3.0)
		{
			//camera.camera_z -= 0.02 * (game.getInput().getAccelX()-6.0)*2;
			
			camera.camera_x -= camera.v * Math.sin(camera.angle);
			camera.camera_z -= camera.v * Math.cos (camera.angle);
			
			flag = true;
		}
		else if(game.getInput().getAccelX() < 0.0)
		{
			//camera.camera_z += 0.02 * -1 * (game.getInput().getAccelX()-3.0)*2;
			camera.camera_x += camera.v * Math.sin(camera.angle);
			camera.camera_z += camera.v * Math.cos (camera.angle);
			flag = true;
		}
		if(game.getInput().getAccelY() > 0.4)
		{
			//camera.camera_x += 0.02 * ((game.getInput().getAccelY()-0.4)*2);
			camera.angle += 0.01;
		}
		else if(game.getInput().getAccelY() < -0.4)
		{
			//camera.camera_x -= 0.02 * -1 *(game.getInput().getAccelY()+0.4)*2;
			camera.angle -= 0.01;
		}
	}
	
	public MainScene(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
		cx = game.getGraphics().getWidth() / 2;
		cy = game.getGraphics().getHeight() / 2;
		camera = new Camera(game.getGraphics().getWidth(), game.getGraphics().getHeight());
		//先在堆中放置100个对象
		for(int i = 0; i < heap_max; i ++)
		{
			heap.add(new Square(0, 0, 0, 40));
		}
		productGround();
		productText();
		//productFirework(0, 4, 0);
	}
	int rotate_forward = 1;
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		game.getGraphics().clear(Color.BLACK);
		dealLogic();
		//画烟花
		for(int i = 0; i < particle.size(); i ++)
		{
			drawSquare(particle.get(i));
		} 
		for(int i = 0; i < ground.size(); i ++)
		{
			drawSquare(ground.get(i));
		}
		for(int i = 0; i < textlist.size(); i ++)
		{
			drawSquare(textlist.get(i));
		}
		
		//camera.camera_z -= 0.01;
		//camera.camera_x = camera.camera_x * Math.cos(0.004) - camera.camera_z * Math.sin(0.004);
		//camera.camera_z = camera.camera_x * Math.sin(0.004) + camera.camera_z * Math.cos(0.004);
		
		//camera.camera_rotate_hor = Math.PI;
		
		angle += rotate_forward * Math.PI / 100;
		if(angle >= Math.PI/2)
		{
			rotate_forward = -rotate_forward;
			angle = Math.PI/2;
		}
		else if(angle <= 0)
		{
			rotate_forward = -rotate_forward;
			angle = 0;
		}
		
		
		//camera.camera_x += 0.005;
		
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
