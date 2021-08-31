package com.sarveshhon.classmeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.sarveshhon.classmeet.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;

    public static SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);

        binding.btn1.setOnClickListener(v -> {

            prefs.edit().putString("batch", "1").apply();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

        binding.btn2.setOnClickListener(v -> {

            prefs.edit().putString("batch", "2").apply();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

        binding.btn3.setOnClickListener(v -> {

            prefs.edit().putString("batch", "3").apply();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

        binding.btn4.setOnClickListener(v -> {

            prefs.edit().putString("batch", "4").apply();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // This method will check if user has open this App for First time or Not
        if (prefs.getBoolean("isFirstRun", true)) {
            prefs.edit().putBoolean("isFirstRun", false).apply();
        } else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

}