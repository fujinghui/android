package fu.jpf.lgame;

import femy.framework.base.Screen;
import femy.framework.game.AndroidGame;
import femy.framework.game.LoadingScreen;

public class MrNomGame extends AndroidGame{
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}
}
