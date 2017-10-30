package femy.framework.game;

import femy.firework.love.MainScene;
import femy.framework.base.Audio;
import femy.framework.base.FileIO;
import femy.framework.base.Game;
import femy.framework.base.Graphics;
import femy.framework.base.Graphics.PixmapFormat;
import femy.framework.base.Input;
import femy.framework.base.Pixmap;
import femy.framework.base.Screen;
import fu.jpf.lgame.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;

public class AndroidGame extends Activity implements Game{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	
	public int game_width = 480;
	public int game_height = 640;
	
	public int screen_width = 0;
	public int screen_height = 0;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){  
		      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
		}
		int sw = getWindowManager().getDefaultDisplay().getWidth();
		int sh = getWindowManager().getDefaultDisplay().getHeight();
		
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		game_width = isLandscape ? sw : sh;
		game_height = isLandscape ? sh : sw;
		int frameBufferWidth = game_width;
		int frameBufferHeight = game_height;
		//获取屏幕宽高
		//创建了一个显示缓冲区
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		
		float scaleX = (float)frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float)frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();

		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		renderView = new AndroidFastRenderView(this, frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		
		Assets.logo = graphics.newPixmap("loading.png", PixmapFormat.RGB565);
		Assets.sparks = new Pixmap[10];
		for(int i= 1; i <= 10; i ++)
		{
			Assets.sparks[i-1] = graphics.newPixmap("spark"+i+".png", PixmapFormat.RGB565);
		}
		screen = getStartScreen();
		
		setContentView(renderView);
		//电源管理
		//PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		//wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == event.KEYCODE_BACK)
		{
			renderView.exit();
			while(!renderView.isExit) ;			//等待线程退出程序！
			return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onResume(){
		super.onResume();
		//wakeLock.acquire();
		screen.resume(); 
		renderView.resume();
	}
	public void onPause(){
		super.onPause();
		//wakeLock.release();
		
		renderView.pause();
		screen.pause();
		if(isFinishing()){
			screen.dispose();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("myDebug", "destroy activity");
		renderView.exit();
	}
	@Override
	public Input getInput() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public FileIO getFileIO() {
		// TODO Auto-generated method stub
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		// TODO Auto-generated method stub
		return graphics;
	}

	@Override
	public Audio getAudio() {
		// TODO Auto-generated method stub
		return audio;
	}

	@Override
	public void setScreen(Screen Screen) {
		// TODO Auto-generated method stub
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		// TODO Auto-generated method stub
		return screen;
	}

	@Override
	public Screen getStartScreen() {
		// TODO Auto-generated method stub
		return new MainScene(this);//new LoadingScreen(this);
	}

}
