package myview;

import draw.object.CircleRound;
import draw.object.Diamond;
import staticdata.GlabolStaticData;
import tools.OperatorObject;
import tools.Paints;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	private SurfaceHolder holder;
	private int GameWidth,GameHeight;
	private boolean GameLoop;
	
	private int SCREEN_WIDTH = 0;
	private int SCREEN_HEIGHT = 0;
	private final int MAX_GAME_COUNT = 10;
	private Diamond GameDiamond[] = new Diamond[MAX_GAME_COUNT];
	private CircleRound circle = null;
	OperatorObject operator = new OperatorObject();
	private Context context;
	private Paints paints;
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		operator.context = context;
		holder = this.getHolder();
		holder.addCallback(this);
		this.context = context;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		GameWidth = width;
		GameHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
		GameWidth = getWidth();
		GameHeight = getHeight();
		SCREEN_WIDTH = GameWidth;
		SCREEN_HEIGHT = GameHeight;
		operator.x = 0;//(int) (Math.random() * GameWidth);
		operator.y = 0;//(int) (Math.random() * GameHeight);
		GameLoop = true;		//允许循环
		
		//初始化游戏中所有的变量
		init(GameWidth, GameHeight);
		new Thread(this).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.KEYCODE_BACK)
		{
			GameLoop = false;
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_UP)	//
		{
			int down_x = (int)event.getX();
			int down_y = (int)event.getY();
			for(int index = 0; index < MAX_GAME_COUNT; index ++)
				GameDiamond[index].isAcceleratedSpeed = true;		//用户松开了屏幕，方块才具有加速度
			circle.setCenter(down_x, down_y);
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int downX = (int)event.getX();
			int downY = (int)event.getY();
			for(int index = 0; index < MAX_GAME_COUNT; index ++)
			{
				if(GameDiamond[index].isContains(context, downX, downY))
				{
					GameDiamond[index].isMove = true;
					GameDiamond[index].isAcceleratedSpeed = false;	//用户点击屏幕直到用户松开期间物体一直不具备加速度
				//	Toast.makeText(context, "index:" + index, Toast.LENGTH_SHORT).show();
				}
				
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			int move_x = (int)event.getX();
			int move_y = (int)event.getY();
			for(int index = 0; index < MAX_GAME_COUNT; index ++)
				GameDiamond[index].setMoveSpeed(move_x, move_y);
		}
		return true;
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		GameLoop = false;		//退出循环
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Canvas canvas;
		Paint backgroundBack = new Paint();
		backgroundBack.setColor(Color.BLACK);
		
		long initTime = System.currentTimeMillis();
		int fps = 0;
		int frame = 0;
		while(GameLoop)
		{
			canvas = holder.lockCanvas();
			canvas.drawRect(new Rect(0, 0, GameWidth, GameHeight), backgroundBack);
			//在屏幕上绘制所有的物体
			//operator.drawSelf(canvas);
			for(int i = 0; i < MAX_GAME_COUNT; i ++)
				GameDiamond[i].draw(canvas);
			circle.draw(canvas);
			//绘制当前有多少帧
			canvas.drawText("fps:"+fps, 10, 30, paints.WHITE_PAINT);
			holder.unlockCanvasAndPost(canvas);
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			frame ++;
			if(System.currentTimeMillis() - initTime >= 1000)
			{
				fps = frame;
				frame = 0;
				
				initTime = System.currentTimeMillis();
			}
		}
		holder.removeCallback(this);
	}
	private void init(int ScreenWidth, int ScreenHeight){	//初始化变量
		paints = new Paints();
		for(int index = 0; index < MAX_GAME_COUNT; index ++)
		{
			GameDiamond[index] = new Diamond(ScreenWidth, ScreenHeight);
			GameDiamond[index].x = GlabolStaticData.MAP_WIDTH / MAX_GAME_COUNT * index;
			GameDiamond[index].y = GlabolStaticData.MAP_HEIGHT - GameDiamond[index].HEIGHT;
		}
		//初始化点击出现圆圈的对象
		circle = new CircleRound(ScreenWidth, ScreenHeight);
	}

}
