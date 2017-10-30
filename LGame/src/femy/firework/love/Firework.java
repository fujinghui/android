package femy.firework.love;

public class Firework {
	private int count = 50;					//烟花粒子的数量
	public Square s[] = new Square[count];
	
	public double x, y, z;
	public double vx, vy, vz;
	
	public double fx, fy, fz;
	public boolean isFire = false;
	public Firework(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.vx = 0;
		this.vy = 0;
		this.vz = 0;
		this.fx = 0; this.fy = 0; this.fz = 0;
	}
	public Firework(double x, double y, double z){
		this.x = x; this.y = y; this.z = 0;
		this.vx = 0; this.vy = 0; this.vz = 0;
	}
	public void init(){
		for(int i = 0; i < count; i ++)
		{
			this.s[i] = new Square(0, 0, 0, 40);
		}
	}
	public void setPosition(double x, double y, double z){
		this.x = 0; this.y = 0; this.z = 0;
	}
	public void setFirePosition(double x, double y, double z){
		this.fx = x; this.fy = y; this.fz = z;
	}
	public void setSpeed(double vx, double vy, double vz){
		this.vx = vx; this.vy = vy; this.vz = vz;
	}
	public void doLogic(){
		if(this.isFire == false)
		{
			//暂时只处理y轴
			this.y += this.vy;
			if(this.y > this.fy)
				this.isFire = true;
		}
		else
		{
			for(int i = 0; i < count; i ++)
			{
				this.doLogic();
			}
		}
	}
	public void draw(){
		
	}
}
