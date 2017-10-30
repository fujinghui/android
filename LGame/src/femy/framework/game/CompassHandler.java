package femy.framework.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassHandler implements SensorEventListener{
	float yaw;
	float pitch;
	float roll;
	
	public CompassHandler(Context context){
		SensorManager manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		if(manager.getSensorList(Sensor.TYPE_ORIENTATION).size() != 0)
		{
			Sensor compass = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
			manager.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		yaw = event.values[0];
		pitch = event.values[1];
		roll = event.values[2];
	}
	public float getYaw(){
		return yaw;
	}
	public float getPitch(){
		return pitch;
	}
	public float getRoll(){
		return roll;
	}

}
