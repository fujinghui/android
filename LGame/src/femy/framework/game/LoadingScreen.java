package femy.framework.game;

import android.graphics.Color;
import android.util.Log;
import femy.framework.base.Game;
import femy.framework.base.Screen;

public class LoadingScreen extends Screen{
	int x, y;
	public LoadingScreen(Game game) {
		super(game);			//这个地方又将game传递到父类中了
	}
	
	@Override
	public void update(float deltaTime) {
		game.getGraphics().clear(Color.argb(1, 255, 255, 255));
		//game.getGraphics().drawLine(0, 0, 480, 640, Color.rgb(0, 125, 0));
		game.getGraphics().drawRect(x-20, y-20, 40, 40, Color.rgb(255, 30, 90));
		//game.getGraphics().drawPixmap(Assets.logo, x, y, 0, 0, 100, 100);
		if(game.getInput().isTouchDown(0))
		{
			x = game.getInput().getTouchX(0);
			y = game.getInput().getTouchY(0);
		}
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
