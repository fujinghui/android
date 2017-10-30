package femy.framework.game;

import femy.framework.base.Graphics;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class AndroidFastRenderView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	Graphics graphics;
	boolean isExit = false;
	
	public AndroidFastRenderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = this.getHolder();
		holder.addCallback(this);
	}
	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer){
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}
	public AndroidFastRenderView(AndroidGame game, Graphics graphics, Bitmap frameBuffer){
		super(game);
		this.graphics = graphics;
		this.framebuffer = frameBuffer;
		this.holder = getHolder();
		
	}
	
	
	public void resume(){
        if(renderThread!=null && !renderThread.isAlive())  
        {         
            try  
            {   
                renderThread.start();  
            }   
            catch(IllegalThreadStateException itse)   
            {   
                renderThread.resume();      
            }   
        }
        else
        {
    		running = true;
    		renderThread = new Thread(this);
    		renderThread.start();
        }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		
		long time = 0;
		int fps = 0;
		int real_fps = 0;
		while(running){
			if(!holder.getSurface().isValid())
			{
				continue;
			}
			
			
			
			fps ++;
			if(System.currentTimeMillis() - time >= 1000)
			{
				real_fps = fps;
				fps = 0;
				time = System.currentTimeMillis();
			}
			
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			//调用当前场景的update
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);
			//if(running)
			{
				Canvas canvas = holder.lockCanvas();
				canvas.getClipBounds(dstRect);
	
				game.getGraphics().drawText("fps:"+real_fps, 0, 30, Color.WHITE);
				
				canvas.drawBitmap(framebuffer, null, dstRect, null);
				//((AndroidGraphics)(game.getGraphics())).drawPixmap(canvas, Assets.logo, 0, 0);
				holder.unlockCanvasAndPost(canvas);
				}
		}
		isExit = true;
	}
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	public void exit(){
		running = false;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//new Thread(this).start();
		Log.d("myDebug", "surface create!");
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
