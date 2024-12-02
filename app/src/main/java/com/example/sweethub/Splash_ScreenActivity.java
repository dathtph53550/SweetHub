package com.example.sweethub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_ScreenActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String FIRST_TIME_KEY = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean(FIRST_TIME_KEY, true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isFirstTime) {
                    intent = new Intent(Splash_ScreenActivity.this, IntroductionActivity.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(FIRST_TIME_KEY, false);
                    editor.apply();
                } else {
                    intent = new Intent(Splash_ScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
