package femy.framework.game;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import femy.framework.base.Input;
import femy.framework.base.Input.TouchEvent;
import femy.framework.tools.Pool;
import femy.framework.tools.Pool.PoolObjectFactory;

public class SingleTouchHandler implements TouchHandler{
	
	boolean isTouched;
	int touchX;
	int touchY;;
	Pool <TouchEvent> touchEventPool;
	List <TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List <TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;
	
	public SingleTouchHandler(View view, float scaleX, float scaleY){
		PoolObjectFactory <TouchEvent> factory = new PoolObjectFactory<TouchEvent>(){
			@Override
			public TouchEvent createObject() {
				// TODO Auto-generated method stub
				return new TouchEvent();
			}
			
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		synchronized(this){
			TouchEvent touchEvent = touchEventPool.newObject();
			switch(event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				touchEvent.type  = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				touchX = (int) (event.getX()*this.scaleX);
				touchY = (int) (event.getY()*this.scaleX);
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				touchX = (int) (event.getX()*this.scaleX);
				touchY = (int) (event.getY()*this.scaleY);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}
		}
		return true;
	}

	@Override
	public boolean isTouchDown(int pointer) {
		// TODO Auto-generated method stub
		synchronized(this){
			if(pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	@Override
	public int getTouchX(int pointer) {
		// TODO Auto-generated method stub
		synchronized(this){
			return touchX;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		// TODO Auto-generated method stub
		synchronized(this){
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvetns() {
		// TODO Auto-generated method stub
		synchronized(this){
			int len = touchEvents.size();
			for(int i = 0; i < len; i ++)
			{
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}