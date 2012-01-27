package me.tahnok.nagme;

import java.util.Calendar;
import java.util.Random;
import com.tahnok.nagme.R;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Popup extends Activity {
    /** Called when the activity is first created. */
	
	public static final String TAG = "nagme";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Bundle bundle = getIntent().getExtras();
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);
		 final String msg = bundle.getString("message");
		 final int min = bundle.getInt("min");
		 final int max = bundle.getInt("max");
		
		final Button testButton = (Button) findViewById(R.id.DismissButton);
		
		testButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	Vibrator h = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		    	h.cancel();
		    	schedule(min,max,msg);
				finish();
			}
		});
        vibrate();
    }
    
    public void vibrate(){
    	Log.d(TAG, "now here");
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	long[] pattern = {0,200, 200, 200, 200, 200, 1000};
    	v.vibrate(pattern, -1);
    
    }
    
    public void schedule(int min, int max, String message){
		AlarmManager mgr= (AlarmManager) getSystemService(Context.ALARM_SERVICE);		
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
    
}