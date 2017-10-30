package myservice;

import android.content.Context;

public class Set {
	public static boolean IS_GRAVITY;
	public static boolean IS_ONTOUCH;
	public static boolean IS_READ_FROM_SDCARD;
	public static String READ_PATH;
	public static int LineUpdateTime = 10;
	public static void init(){
		IS_GRAVITY = false;
		IS_ONTOUCH = false;
		IS_READ_FROM_SDCARD = false;
		READ_PATH = "/sdcard/show_code.txt";
	}
}
