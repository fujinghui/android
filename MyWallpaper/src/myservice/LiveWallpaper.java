package myservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import fu.mywallpaper.AutoScreenText;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class LiveWallpaper extends WallpaperService{

	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		Set.init();		//初始化全局静态变量
		return new MyEngine();
	}
	//Engine类
	class MyEngine extends Engine{
		private boolean start = true;
		//对象工具
		private Runnable run = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(start)
				{
					drawFrame();	//绘制一帧	
					handler.postDelayed(run, 20);
				}
			}
		};
		private Handler handler = new Handler();
		private SurfaceHolder holder;
		private Canvas canvas;
		
		//画笔工具
		private Paint paint;
		private Paint paintBackground;
		private Paint paintLine;
		private Paint paintTime;
		private Paint paintPoint;
		//coord
		private int x = 0, y = 0;
		//Screen Width and Height
		private int SCREEN_WIDTH;
		private int SCREEN_HEIGHT;
		private Rect rectBackground;
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			// TODO Auto-generated method stub
			super.onCreate(surfaceHolder);
			
			//set point color
			paint = new Paint();
			paint.setColor(Color.rgb(255, 125, 125));
			paintBackground = new Paint();
			paintBackground.setColor(Color.rgb(255, 255, 255));
			
			paintLine = new Paint();
			paintLine.setColor(Color.argb(125, 125, 125, 125));
			
			paintTime = new Paint();
			paintTime.setColor(Color.rgb(0, 0, 0));
			
			paintPoint = new Paint();
			paintPoint.setColor(Color.rgb(0, 0, 0));
			paintPoint.setShadowLayer(5, 5, 5, Color.rgb(30, 30, 250));
			//start run backgroun wallpaper program
			handler.post(run);
			//set screen ontouch
			setTouchEventsEnabled(true);	//设置允许手指触摸
			//get screen Width and Height
			SCREEN_WIDTH = getWallpaper().getMinimumWidth();	//获取的为屏幕的宽的2倍
			SCREEN_HEIGHT=  getWallpaper().getMinimumHeight();
			//set rect
			rectBackground = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			//初始化资源
			initResources();
			//初始化与重力感应器相关的变量
			sm = (SensorManager)getSystemService(SENSOR_SERVICE);
			sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorListener = new SensorListener();
			//
			text_top_left = new NumberText[SCREEN_WIDTH / 2 / NumberTextFontSize];
			for(int i = 0; i < text_top_left.length; i ++)
			{
				text_top_left[i] = new NumberText(i * NumberTextFontSize, NumberTextFontSize);
				text_top_left[i].RandomNextText();
			}
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			// TODO Auto-generated method stub
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
		}

		@Override
		public void onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			if(Set.IS_ONTOUCH)
			{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					int x, y;
					SX = SCREEN_WIDTH / 2; SY = SCREEN_HEIGHT / 2;
					x = (int)event.getX();
					y = (int)event.getY();
					if(x >= SX && x < SCREEN_WIDTH && y >= SY && y < SCREEN_HEIGHT)
					{
						mapRB[(y - SY) / (SCREEN_HEIGHT / 2 / mapRBH)][(x - SX) / (SCREEN_WIDTH / 2 / mapRBW)] = (char) ((mapRB[(y - SY) / (SCREEN_HEIGHT / 2 / mapRBH)][(x - SX) / (SCREEN_WIDTH / 2 /mapRBW)] == 1) ? 0 : 1) ; 
					}
				}
			}
			super.onTouchEvent(event);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			// TODO Auto-generated method stub
			super.onVisibilityChanged(visible);
			if(visible)
			{
				//壁纸可见
				start = true;
				handler.post(run);
				if(Set.IS_GRAVITY)
					sm.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);	//注册重力感应
			}
			else
			{
				//壁纸不可见
				start = false;
				handler.removeCallbacks(run);
				if(Set.IS_GRAVITY)
					sm.unregisterListener(sensorListener);				//取消重力注册
			}
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			handler.removeCallbacks(run);
		}
		
		private float tx, tnx, ty, tny;
		private int SIN_HEIGHT;
		private void drawFrame(){
			holder = getSurfaceHolder();
			canvas = holder.lockCanvas();
			//绘制背景
			canvas.drawRect(rectBackground, paintBackground);
//			canvas.save()
			SIN_HEIGHT = SCREEN_HEIGHT / 2;
			
			for(float i = 0; i < SCREEN_WIDTH; i += 1.0f)
			{
				tx = i;
				tnx = tx + 1;
				ty =  SIN_HEIGHT * (float)Math.sin((tx + x) * Math.PI * 2 / 100) + SIN_HEIGHT / 3 * (float)Math.cos(3 * (tx + x) * Math.PI * 2 / 100);
				tny = SIN_HEIGHT * (float)Math.sin((tnx + x) * Math.PI * 2 / 100) + SIN_HEIGHT / 3 * (float)Math.cos(3 * (tnx + x) * Math.PI * 2 / 100);
//				canvas.drawLine(i, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 2 * (float)Math.sin((i + x) * 2 * Math.PI / 200), i + 1.0f, SCREEN_HEIGHT / 2 + SCREEN_HEIGHT / 2 * (float)Math.sin((i + 1 + x) * 2 * Math.PI / 200), paint);
				canvas.drawLine(tx, ty, tnx, tny, paint);
			}
			
			x ++; y ++;
			drawCirclePath(canvas);	//画一个圆球
			drawBackground(canvas);
			drawScreenTopLeft(canvas);
			drawScreenTopRight(canvas);
			drawScreenBottomLeft(canvas);
			drawScreenBottomRight(canvas);
			holder.unlockCanvasAndPost(canvas);
		}
		
		
		private float angle = 0.0f;
//		private Rect rectCircelPath = new Rect(, 0, 10, 10);
		private void drawCirclePath(Canvas canvas){
			
			//paint.setShadowLayer(5, 5, 5, Color.rgb(0, 255, 0));
			canvas.drawCircle(SCREEN_WIDTH / 2 + 100 * (float)Math.cos(angle), SCREEN_HEIGHT / 2 + 100 * (float)Math.sin(angle), 5, paint);
			angle = angle + 0.1f;
//			Log.d("myDebug", "width:" + SCREEN_WIDTH + " height:" + SCREEN_HEIGHT);
		}
		
		
		private int SX, SY, TH, TW;
		
		private String textTL = "乾三连 坤三断 震仰盂 艮覆碗离中虚 坎中满 兑上缺 巽下断";
		private String textTR = "hello world";
		private int tlIndex = 0;
		private Paint paintBack = new Paint();
		private Paint paintTR = new Paint();
		private Paint paintTL = new Paint();
		private Paint paintBL = new Paint();
		private Paint paintBR = new Paint();
		private Paint paintBRthis = new Paint();
		
		private int mapRBW = 15, mapRBH = 15;
		private int RBW, RBH;
//		private char mapRB[][] = new char[mapRBH][mapRBW];
		private char mapRB[][] = {
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
				{1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
				{1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
				{0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
				{0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
				{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
				{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
				{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0}
		};
//		private 
		private char mapVisited[][] = new char[mapRBH][mapRBW];	//用来标记当前地图位置是否遍历过
		private int RBthisX, RBthisY;
		private int preMapPosition[][] = new int[500][2];		//保存上一次转弯可走的路的坐标
		private int newPrePosition = -1;
		private int runResult = -1;
		void initResources(){
			//draw top left
			tlIndex = 1;
			//设置画笔颜色
			paintTL.setColor(Color.RED);
			paintTR.setColor(Color.GREEN);
			paintBR.setColor(Color.argb(125, 0, 0, 125));
			paintBL.setColor(Color.argb(125, 255, 0, 0));
			paintBack.setColor(Color.argb(125, 0, 0, 0));
			paintBRthis.setColor(Color.argb(125, 255, 0, 0));
			//设置各个字体的大小
			paintTR.setTextSize(20);
			paintTL.setTextSize(30);
			//初始化地图资源
			initPosition();
		}
		private void drawBackground(Canvas canvas){
			paintBack.setColor(Color.GREEN);
			canvas.drawLine(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH, SCREEN_HEIGHT / 2, paintBack);
			canvas.drawLine(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT, paintBack);
		}
		
		private class NumberText{
			private int text_length = 0;
			private char text[] = new char[19];
			private int x, y;
			private Paint paint = new Paint();
			private int start_time = 0;
			private int end_time = 0;
			private char t;
			private int r, g, b;
			public NumberText(int x, int size){
				this.x = x;
				this.paint.setTextSize(size);
				y = 0;
			}
			public void RandomNextText(){
				start_time = 0;
				end_time = (int)(Math.round(Math.random() * 5) + 2);
				text_length = (int) (Math.round((Math.random() * 10)) + 8);
				for(int i = 0; i < text_length; i ++)
				{
					text[i] = (char)Math.round(Math.random() * (126 - 33) + 33);
				}
				r = (int)Math.round(Math.random() * 255);
				g = (int)Math.round(Math.random() * 125) + 100;
				b = (int)Math.round(Math.random() * 255);
				this.paint.setColor(Color.rgb(r, g, b));
			}
			public void draw(Canvas canvas, int start_x, int start_y, int width, int height){
				//canvas.drawText(text[0]+"", x, 100, this.paint);
				if(y - paint.getTextSize() * text_length >= start_y + height)
				{
					RandomNextText();
					y = (int)(Math.random() * height) / 2;
					return;
				}
				for(int i = 0; i < text_length; i ++)
				{
					if(y - this.paint.getTextSize() * (i - 1) >= height)
						continue;
					paint.setColor(Color.argb(255 - 255 / text_length * i, r, g, b));//r - r / text_length * i, g - g / text_length * i, b - b / text_length * i));
					canvas.drawText(String.valueOf(text[i]), start_x + x,start_y + y - paint.getTextSize() * i, paint);
				}
				if(start_time >= end_time)
				{
					y += paint.getTextSize();
					start_time = 0;
					
					t = text[text_length - 1];
					for(int i = text_length - 1; i > 0; i --)
					{
						text[i] = text[i - 1];
					}
					text[0] = t;
				}
				else
				{
					start_time ++;
				}
			}
		}
		
		private int NumberTextFontSize = 25;
		private NumberText text_top_left[] = null;
		private void drawScreenTopLeft(Canvas canvas){
			SX = 0; SY = 0;
			TH = SCREEN_HEIGHT / 2;
			TW = SCREEN_WIDTH / 2;
			paintBack.setColor(Color.argb(170, 0, 0, 0));
			canvas.drawRect((int)SX, (int)SY, (int)(SX + TW), (int)(SY + TH), paintBack);
			for(int i = 0; i < text_top_left.length; i ++)
			{
				text_top_left[i].draw(canvas, SX, SY, TW, TH);
			}
			/*
			if(tlIndex <= textTL.length() / 2)
				canvas.drawText(textTL.substring(0, tlIndex), SX, SY + 60, paintTL);
			else
			{
				canvas.drawText(textTL.substring(0, textTL.length() / 2), SX, SY + 60, paintTL);
				canvas.drawText(textTL.substring(textTL.length() / 2, tlIndex).toString(), SX, SY + 100, paintTL);
			}
			if(tlIndex >= textTL.length())
			{
				tlIndex = 1;
			}
			else
				tlIndex ++;
				*/
		}
		
		
		AutoScreenText trText = new AutoScreenText();
		String trString= "#include <reg.h>-" +
						 "#define TASK_MAX 8-"+
						 "#define TASK_STACK_SIZE 12-"+
						 "#define OS_START(id) {TASK_ID=id,SP=TASK_ID}-"+
						 "void TaskSwitch(){-"+
						 "TASK_SP[TASK_ID]=SP;-"+
						 "TASK_ID ++;-"+
						 "if(TASK_ID == TASK_MAX)-"+
						 "   TASK_ID = 0;-"+
						 "SP = TASK_SP[TASK_ID];-"+
						 "int main(void){-"+
						 "TaskLoad(task1, 0);-"+
						 "TaskLoad(task2, 1);-"+
						 "return 0;-"+
						 "}";
		int trLength = trString.length();
		int trIndex = 0;
		int delayIndex = 0;
		private void drawScreenTopRight(Canvas canvas){
			SX = SCREEN_WIDTH / 2; SY = 0;
			TH = SCREEN_HEIGHT / 2;
			TW = SCREEN_WIDTH / 2;
			trText.setScreenWH(TW, TH);
			trText.setScreenXY(SX, SY + 40);
			trIndex ++;
			if(trIndex > trLength)
			{
				trIndex = trLength;
				delayIndex ++;
				if(delayIndex > 100)
				{
					trIndex = 0;
					delayIndex = 0;
				}
			}
			paintBack.setColor(Color.argb(125, 125, 125, 125));
			canvas.drawRect(SX, SY, SX + TW, SY + TH, paintBack);
			trText.showText(canvas, trString, 0, trIndex);
			if(Set.IS_READ_FROM_SDCARD)
			{
				String temp = ReadFromSdcard(Set.READ_PATH);
				if(temp != null)
					trString = temp;
				trLength = trString.length();
				Set.IS_READ_FROM_SDCARD = false;	//不读取
			}
			
			//canvas.drawText(textTR, SX, SY + 40, paintTR);
			//canvas.drawText(Html.fromHtml("<b color=red>" + textTR + "</b>"), 0, textTR.length(), SX, SY + 40, paintTR);
		}
		private String ReadFromSdcard(String path){
			StringBuffer result = new StringBuffer();
			File file = new File(path);
			if(!file.exists())		//file is not exist
			{
				return null;
			}
			else if(file.isFile())
			{
				BufferedReader in = null;
				try {
					in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
					String buffer;
					while((buffer = in.readLine()) != null)
						result.append(buffer+"-");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return result.toString();
		}	
		
		private void addPoint(int y){
			if(addCount < PointCount)
			{
				points[addCount] = y;
				dates[addCount] = new Date();
				addCount ++;
			}
			else
			{
				for(int i = 0; i < addCount - 1; i ++)
				{
					points[i] = points[i + 1];
					dates[i] = dates[i + 1];
				}
				points[PointCount - 1] = y;
				dates[PointCount - 1] = new Date();
			}
		}	
		//折线图画在左下角
		private final int PointCount = 20;					//折线点的数量
		private final int YCount = 10;						//y轴要显示的行数
		private final int YMax = 100;						//Y轴最大值
		private int points[] = new int[PointCount];		//
		private Date dates[] = new Date[PointCount];
		private int addCount = 0;							//已添加进数量
		///////////////////////////////////////////////////
		private int CountWidth = 0, CountHeight = 0;	//每个格子的宽度
		//在该处处理重力引力问题
		private SensorManager sm;
		private Sensor sensor;
		private SensorListener sensorListener;
		private int BLMX, BLMY;
		private int BLX, BLY;
		
		private int loop = 0;
		private void drawScreenBottomLeft(Canvas canvas){
			SX = 0; SY = SCREEN_HEIGHT / 2;
			TH = SCREEN_HEIGHT / 2;
			TW = SCREEN_WIDTH / 2;
			paintBack.setColor(Color.argb(125, 80, 125, 255));
			BLX += BLMX;	BLY += BLMY;
			canvas.drawRect(SX, SY, SX + TW, SY + TH, paintBack);
			if(BLX < SX || BLX > SX + TW)
				BLX = SX;
			if(BLY < SY || BLY > SY + TH)
				BLY = SY;
			canvas.drawRect(BLX, BLY, BLX + 10, BLY + 10, paintBL);
			
			
			
			//paintBL.setShadowLayer(5, 6, 6, Color.rgb(30, 30, 200));
			paintBL.setColor(Color.argb(200, 0, 0, 0));
			if(loop >= Set.LineUpdateTime)
			{
				addPoint((int)(Math.random()*YMax));
				loop = 0;
			}
			else
				loop ++;
			//用点来显示折线图
			CountWidth = TW / PointCount;
			CountHeight = TH / YCount;
			for(int i = 0; i <= PointCount; i ++)
			{
				canvas.drawLine(SX + i * CountWidth, SY + 0, SX + i * CountWidth, SY + TH, paintLine);
			}
			for(int j = 0; j <= YCount; j ++)
			{
				canvas.drawLine(SX + 0, SY + j * CountHeight, SX + TW, SY + j * CountHeight, paintLine);
				canvas.drawText((j * YMax / YCount)+"", 1, SY + TH - j * CountHeight, paintTime);
			}
			
			canvas.drawCircle(SX + 0 * CountWidth, SY + points[0] * TH / YMax, 5, paintPoint);
			for(int i = 1; i < addCount; i ++)
			{
				String time = dates[i].getHours() + ":" + dates[i].getMinutes() + ":" + dates[i].getSeconds();
				canvas.drawText(time, SX + i * CountWidth-20, SY + points[i] * TH / YMax - 16, paintTime);
				
				canvas.drawCircle(SX + i * CountWidth, SY + points[i] * TH / YMax, 5, paintPoint);
				canvas.drawLine(SX + (i-1) * CountWidth, SY + points[i-1] * TH / YMax, SX + i * CountWidth, SY + points[i] * TH / YMax, paintBL);
			}
		}
		class SensorListener implements SensorEventListener{
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSensorChanged(SensorEvent event) {
				// TODO Auto-generated method stub
				int x, y, z;
				x = (int)event.values[0];
				y = (int)event.values[1];
//				z = (int)event.values[2];
				BLMX = -x; BLMY = y;
//				Log.d("myDebug", "x:"+x+" y:"+y+" z:"+z);
			}
		}
		private void drawScreenBottomRight(Canvas canvas){
			SX = SCREEN_WIDTH / 2; SY = SCREEN_HEIGHT / 2;
			TH = SCREEN_HEIGHT / 2;
			TW = SCREEN_WIDTH / 2;
			RBW = TW / mapRBW; RBH = TH / mapRBH;
			paintBack.setColor(Color.argb(125, 125, 125, 250));
			canvas.drawRect(SX, SY, SX + TW, SY + TH, paintBack);
			for(int i = 0; i < mapRBH; i ++)
			{
				for(int j = 0; j < mapRBW; j ++)
				{
					if(mapRB[i][j] == 0&& i == RBthisY&&j == RBthisX)
					{
						canvas.drawRect(SX + j * RBW, SY + i * RBH, SX + j * RBW + RBW, SY + i * RBH + RBH, paintBRthis);					
					}
					else if(mapRB[i][j] == 1)
					{
						canvas.drawRect(SX + j * RBW, SY + i * RBH, SX + j * RBW + RBW, SY + i * RBH + RBH, paintBR);
					}
				}
			}
			solveBR();
		}
		private void initPosition(){
			//回归初始化
			RBthisX = 0; RBthisY = 0;
			newPrePosition = -1;
			//重新初始化所有的左边都没有被遍历
			for(int i = 0; i < mapRBH; i ++)
			{
				for(int j = 0; j < mapRBW; j ++)
				{
					mapVisited[i][j] = 0;
				}
			}
			mapVisited[RBthisY][RBthisX] = 1;	//起始点已被遍历
		}
		private void solveBR(){
			int forward[][] = {
								{1, 0},		//右
								{-1, 0},		//左
								{0, -1},		//上
								{0, 1}		//下
								};
			int curState = 0;
			int tx, ty;
			for(int forwardIndex = 0; forwardIndex < 4; forwardIndex ++)
			{
				tx = RBthisX + forward[forwardIndex][0];
				ty = RBthisY + forward[forwardIndex][1];
				if(tx>=0 && tx<mapRBW && ty>=0 && ty<mapRBH && mapRB[ty][tx] == 0 && mapVisited[ty][tx] == 0)	//该点可以走
				{
					curState = 1;		//已经要走了
					newPrePosition ++;
					preMapPosition[newPrePosition][0] = tx;
					preMapPosition[newPrePosition][1] = ty;
				}
			}
			if(newPrePosition < 0)	//没有路可走了
			{
				initPosition();
			}
			else	//有路可走
			{
				tx = preMapPosition[newPrePosition][0];
				ty = preMapPosition[newPrePosition][1];
				if(tx == mapRBW - 1 && ty == mapRBH - 1)	//走到了终点
				{
					initPosition();
				}
				else
				{
					if(curState == 0)
					{
						if(mapRB[ty][tx] == 0)
						{						
							RBthisX = preMapPosition[newPrePosition][0]; RBthisY = preMapPosition[newPrePosition][1];
							newPrePosition --;
						}
					}
					else
					{

						RBthisX = tx; RBthisY = ty;		//移动一步
						mapVisited[ty][tx] = 1;
//						newPrePosition --;
					}
				}
			}
		}
	}
}
