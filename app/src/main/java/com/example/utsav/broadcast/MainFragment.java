package com.example.utsav.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    TextView logger;
    MyReceiver mReceiver = new MyReceiver();
    String TAG = "MainFragment";

    public MainFragment() {
        // Required empty public constructor
    }

    /* register the receiver (declared below) to get the information charging and batter */
    @Override
    public void onResume() {
        super.onResume();
        //the one below registers a global receiver.
        IntentFilter myIF = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        myIF.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        myIF.addAction("android.intent.action.BATTERY_LOW");
        myIF.addAction("android.intent.action.BATTERY_OKAY");
        getActivity().registerReceiver(mReceiver, myIF);
        Log.v(TAG, "receiver should be registered");
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
       getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {  //or onDestory()
        // Unregister since the activity is not visible.

        getActivity().unregisterReceiver(mReceiver);
        Log.v(TAG, "receiver should be unregistered");
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_main, container, false);
        logger = myView.findViewById(R.id.textView1);
        return myView;
    }

    public class MyReceiver extends BroadcastReceiver {
        public MyReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.v("myReceiver", "received an intent");
            String info = "\n something wrong.";
            int mStatus = 0;
            if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {  //battery is low!
                info = "\n batter low. should shut down things.";
                mStatus = 1;
                Log.v("myReceiver", "battery low");
            } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {  //battery is now ok!
                info = "\n battery Okay.  ";
                mStatus = 2;
                Log.v("myReceiver", "battery okay");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {  //battery is charging.
                info = "\n Power connected, so phone is charging.";
                mStatus = 3;
                Log.v("myReceiver", "ac on");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {  //power has been disconnected.
                info = "\n Power disconnected.";
                mStatus = 4;
                Log.v("myReceiver", "ac off");
            }
            else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)){
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        info = "\n Headset PluggedOUT";
                        mStatus = 5;
                        Log.d(TAG, "Headset is unplugged");
                        break;
                    case 1:
                        info = "\n Headset PluggedIN";
                        mStatus = 6;
                        Log.d(TAG, "Headset is plugged");
                        break;
                }
            }
            logger.setText("status is " + mStatus + info);
        }
    }
}
