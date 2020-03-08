package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivTitle, ivLogo;
    private View vFade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivTitle = findViewById(R.id.iv_splash_title);
        ivLogo = findViewById(R.id.iv_splash_logo);

        vFade = findViewById(R.id.v_splash_fade);

        new Handler().postDelayed(() -> {
            ivTitle.animate().translationYBy(-ivTitle.getHeight() * 1.5f).alpha(0).setDuration(0);
            ivLogo.animate().translationYBy(ivLogo.getHeight()).alpha(0).setDuration(0).withEndAction(()-> {
                ivTitle.animate().translationYBy(ivTitle.getHeight() * 1.5f).alpha(1).setDuration(600).setStartDelay(400);
                ivLogo.animate().translationYBy(-ivLogo.getHeight()).alpha(1).setDuration(1000).withEndAction(() -> {
                   vFade.animate().alpha(0).setStartDelay(600).withEndAction(() -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();
                   });
                });
            });

        }, 100);




//        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//
//            finish();
//        }, 2000);
    }
}
