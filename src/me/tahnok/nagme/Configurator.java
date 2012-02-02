package me.tahnok.nagme;

import java.util.Calendar;
import java.util.Random;
import me.tahnok.nagme.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class Configurator extends Activity {
	public static final String TAG = "nagme";

	//	PendingIntent toSend = null;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//		toSend = (PendingIntent) getLastNonConfigurationInstance();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.configurator);
		final Button launcher = (Button) findViewById(R.id.startbutton);
		final EditText min = (EditText) findViewById(R.id.min);
		final EditText max = (EditText) findViewById(R.id.max);
		final ToggleButton toggle = (ToggleButton) findViewById(R.id.serviceState); 
		
		toggle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(toggle.isChecked()){
					schedule(min, max, "OK");
				}
				else{
					cancelPending();
				}
				
			}
		});
		
		if(isRunning()){
			toggle.setChecked(true);
		}
		else{
			toggle.setChecked(false);
		}
		
		launcher.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				schedule(min, max, "OK");
			}
		});
		final Button cancel = (Button) findViewById(R.id.stopbutton);
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelPending();
				toggle.setChecked(false);
			}
		});
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		final ToggleButton toggle = (ToggleButton) findViewById(R.id.serviceState); 
		toggle.setChecked(isRunning());
		Log.d(TAG, "" + isRunning());

	}
		
	private boolean isRunning() {
		//Alarm manager lets you schedule events for the future
				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				//make an intent to just cancel it
				Intent i = new Intent(getApplicationContext(), StartReciever.class);
				PendingIntent running = PendingIntent.getBroadcast(getApplicationContext(), 1123498, i, PendingIntent.FLAG_NO_CREATE);
				if(running == null){
					return false;
				}
				else{
					return true;
				}
	}

	/**
	 * This 
	 * @param min
	 * 		minimum time between nags
	 * @param max
	 * 		maximum time between nags
	 * @param message
	 * 		message to show when a nag happens
	 */
	public void schedule(EditText minBox, EditText maxBox, String message){
		int min;
		int max;
		
		try{
			min = Integer.parseInt(minBox.getText().toString());
			max = Integer.parseInt(maxBox.getText().toString());
		}
		catch(NumberFormatException e){
			//defaults
			min = 10;
			max = 60;
		}
		
		//clear existing ones
		cancelPending();
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		Intent i = new Intent(getApplicationContext(), StartReciever.class);
		i.putExtra("min", min);
		i.putExtra("max", max);
		i.putExtra("message", message);
		PendingIntent toSend = PendingIntent.getBroadcast(getApplicationContext(), 1123498, i, PendingIntent.FLAG_ONE_SHOT);

		Calendar cal = Calendar.getInstance();
		Random rand = new Random();
		int next = rand.nextInt(max-min) + min;
		cal.add(Calendar.MINUTE, next);
		mgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), toSend);
		Log.d(TAG, "Event scheduled");
		
	}

	/**
	 * cancels all pending alarms to nag you
	 */
	public void cancelPending(){
		//Alarm manager lets you schedule events for the future
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		//make an intent to just cancel it
		Intent i = new Intent(getApplicationContext(), StartReciever.class);
		PendingIntent toSend = PendingIntent.getBroadcast(getApplicationContext(), 1123498, i, PendingIntent.FLAG_NO_CREATE);
		if(toSend != null) mgr.cancel(toSend);
		Log.d(TAG, "Event canceled");
	}



}
