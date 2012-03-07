package se.kth.ssvl.tslab.bytewalla.androiddtn.applications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PayloadReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		//Intent newIntent = new Intent(context, ProcessPayload.class);
		Intent newIntent = new Intent();
		newIntent.setAction("se.kth.ssvl.tslab.bytewalla.androiddtn.action.ProcessPayload");
		String payload = intent.getStringExtra("payload");
		Log.d("PayloadReceiver", "payload: "+payload);
		newIntent.putExtra("payload", payload);
		 context.startService(newIntent);
	}
}