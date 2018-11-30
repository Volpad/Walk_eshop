package com.example.andreas.walk;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import com.example.andreas.walk_eshop.R;


public class MainActivity extends AppCompatActivity {//implements NetworkStateReceiver.NetworkStateReceiverListener{

    boolean doubleBackToExitPressedOnce = false;
  //  private NetworkStateReceiver networkStateReceiver;
    public static Activity ma;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("getdatafromdrawer", "Key: " + key + " Value: " + value);
            }
        }

        ma = this;

        Intent intent = new Intent(MainActivity.this, Drawer.class);
        startActivity(intent);
        finish();

     //   networkStateReceiver = new NetworkStateReceiver();
    //    networkStateReceiver.addListener(MainActivity.this);
   //     this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));





    }

    public void onDestroy() {
        super.onDestroy();
    //    networkStateReceiver.removeListener(this);
  //      this.unregisterReceiver(networkStateReceiver);
    }


    @Override
    public void onBackPressed() {
                super.onBackPressed();
    }


/*

    @Override
    public void networkAvailable() {

    }

    @Override
    public void networkUnavailable() {

        launchNoConnectionActivity();



        */
/* TODO: Your disconnection-oriented stuff here *//*

        Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();

    }

    private void launchNoConnectionActivity() {

        Intent intent = new Intent(this, NoConnectionActivity.class);
        startActivity(intent);
        finish();
    }


    public void testactivity(View view){
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
        finish();

    }

*/

}





