package com.xcrino.mcqproject.sliderscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.xcrino.mcqproject.R;
import com.xcrino.mcqproject.activity.HomeActivity;
import com.xcrino.mcqproject.activity.LoginActivity;
import com.xcrino.mcqproject.utils.AppPreferences;

public class SplashScreenActivity extends AppCompatActivity {

    String mAccessTokenString;
    Handler mDelayHandler;
    int SPLASH_DELAY = 3000;
    AppPreferences mAppPreferences;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mContext = this;
        mAppPreferences = new AppPreferences(mContext);
        mDelayHandler = new Handler();
        mDelayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAppPreferences.getIsFirstTimeLaunch()){
                    mAppPreferences.setIsFirstTimeLaunch(true);
                    Intent intent = new Intent(mContext, SliderScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    mAppPreferences.setIsFirstTimeLaunch(false);
                    if (mAccessTokenString.isEmpty()){
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, SPLASH_DELAY);

        mAccessTokenString = mAppPreferences.getAccessTokenString();
    }

}
