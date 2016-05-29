package net.ridhoperdana.siduya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by RIDHO on 4/30/2016.
 */
public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        try{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(Color.TRANSPARENT);
        }catch (Exception e)
        {
            Log.d("Version", "kurang");
        }
        setContentView(R.layout.splash_screen);

//        Thread timerThread = new Thread(){
//            public void run(){
//                try{
//                    sleep(2000);
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }finally{
//                    Intent intent = new Intent(SplashScreen.this,HalamanDepan.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//        timerThread.start();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, HalamanDepan.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
}
