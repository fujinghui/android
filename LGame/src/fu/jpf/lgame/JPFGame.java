package fu.jpf.lgame;

import femy.framework.base.Screen;
import femy.framework.game.AndroidGame;
import femy.framework.game.LoadingScreen;

public class JPFGame extends AndroidGame{
	@Override
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}
}
