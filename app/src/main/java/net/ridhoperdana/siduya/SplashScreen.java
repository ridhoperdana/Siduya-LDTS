package net.ridhoperdana.siduya;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,HalamanDepan.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
}
