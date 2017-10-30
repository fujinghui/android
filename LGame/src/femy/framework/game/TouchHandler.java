package femy.framework.game;

import java.util.List;

import femy.framework.base.Input;
import femy.framework.base.Input.TouchEvent;

import android.view.View.OnTouchListener;

public interface TouchHandler extends OnTouchListener{
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List <TouchEvent> getTouchEvetns();

}
