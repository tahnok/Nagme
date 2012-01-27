package me.tahnok.nagme;

//TODO make the popup using a custom nagging reminder
//TODO make it remember that custom string
//TODO remember min, max times
//TODO make sure min is < max


import java.util.Calendar;
import java.util.Random;

import com.tahnok.nagme.R;

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

		launcher.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int min_time = Integer.parseInt(min.getText().toString());
				int max_time = Integer.parseInt(max.getText().toString());
				schedule(min_time, max_time, "Ok fine...");
			}
		});
		final Button cancel = (Button) findViewById(R.id.stopbutton);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancelPending();
				
			}
		});
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
	public void schedule(int min, int max, String message){
		//clear existing ones
		cancelPending();
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	
		Intent i = new Intent(getApplicationContext(), StartReciever.class);
		i.putExtra("min", min);
		i.putExtra("max", max);
		i.putExtra("message", message);
		PendingIntent toSend = PendingIntent.getBroadcast(getApplicationContext(), 1123498, i, PendingIntent.FLAG_CANCEL_CURRENT);
		
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
		PendingIntent toSend = PendingIntent.getBroadcast(getApplicationContext(), 1123498, i, PendingIntent.FLAG_CANCEL_CURRENT);
		mgr.cancel(toSend);
	}
	
	
	
}
