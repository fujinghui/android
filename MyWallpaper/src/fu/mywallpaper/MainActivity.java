package fu.mywallpaper;

import myservice.Set;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

 
public class MainActivity extends Activity {
	private Button bReset = null;
	private ToggleButton tbGravity = null;
	private ToggleButton tbOnTouch = null;
	private Button bAgainReadCode = null;
	private EditText etLineUpdateTime = null;
	private Button bLineUpdateTime = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bReset = (Button)findViewById(R.id.Button_Reset);
		tbGravity = (ToggleButton)findViewById(R.id.ToggleButton_Gravity);
		tbOnTouch = (ToggleButton)findViewById(R.id.ToggleButton_OnTouch);
		bAgainReadCode = (Button)findViewById(R.id.Button_AgainReadCode);
		etLineUpdateTime = (EditText)findViewById(R.id.EditText_LineUpdateTime);
		bLineUpdateTime = (Button)findViewById(R.id.Button_LineUpdateTime);
//		getResources().getDrawable(id);
		tbGravity.setChecked(Set.IS_GRAVITY);
		tbOnTouch.setChecked(Set.IS_ONTOUCH);
		etLineUpdateTime.setText(""+Set.LineUpdateTime);
		//Set Button Listener
		bReset.setOnClickListener(new ButtonClick());
		tbGravity.setOnClickListener(new ButtonClick());
		tbOnTouch.setOnClickListener(new ButtonClick());
		bAgainReadCode.setOnClickListener(new ButtonClick());
		bLineUpdateTime.setOnClickListener(new ButtonClick());
	}
	
	class ButtonClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId() == bReset.getId())		//Reset State
			{
				
			}
			else if(v.getId() == tbGravity.getId())		//Change ToggleButton State
			{
				Set.IS_GRAVITY = !Set.IS_GRAVITY;
				tbGravity.setChecked(Set.IS_GRAVITY);
			}
			else if(v.getId() == tbOnTouch.getId())
			{
				Set.IS_ONTOUCH =!Set.IS_ONTOUCH;
				tbOnTouch.setChecked(Set.IS_ONTOUCH);
			}
			else if(v.getId() == bLineUpdateTime.getId())
			{
				if(!etLineUpdateTime.getText().equals(""))
				{
					int dit = Integer.parseInt(etLineUpdateTime.getText().toString());
					Set.LineUpdateTime = dit;
				}
			}
			else if(v.getId() == bAgainReadCode.getId())
			{
				Set.IS_READ_FROM_SDCARD = true;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
