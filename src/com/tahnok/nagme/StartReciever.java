package com.tahnok.nagme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class StartReciever extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
	    Bundle bundle = intent.getExtras();
	    String msg = bundle.getString("message");
	    int min = bundle.getInt("min");
	    int max = bundle.getInt("max");
	    	    
		Intent i = new Intent(context,Popup.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
	    i.putExtra("message", msg);
	    i.putExtra("min", min);
	    i.putExtra("max", max);
	    
	    
	    context.startActivity(i); 	
	}
}
