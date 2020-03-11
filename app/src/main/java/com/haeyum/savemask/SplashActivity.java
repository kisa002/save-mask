package com.haeyum.savemask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivTitle, ivLogo;
    private View vFade;

    private AppPref appPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appPref = new AppPref(this);

        initUI();
//        initFirebase();
    }

    private void initUI() {
        ivTitle = findViewById(R.id.iv_splash_title);
        ivLogo = findViewById(R.id.iv_splash_logo);

        vFade = findViewById(R.id.v_splash_fade);
        findViewById(R.id.layout_splash).post(()-> {
            ivTitle.animate().translationYBy(-ivTitle.getHeight() * 1.5f).alpha(0).setDuration(0);
            ivLogo.animate().translationYBy(ivLogo.getHeight()).alpha(0).setDuration(0).withEndAction(()-> {
                ivTitle.animate().translationYBy(ivTitle.getHeight() * 1.5f).alpha(1).setDuration(600).setStartDelay(400);
                ivLogo.animate().translationYBy(-ivLogo.getHeight()).alpha(1).setDuration(1000).withEndAction(() -> {
                   vFade.animate().alpha(0).setStartDelay(600).withEndAction(() -> {
                        Intent intent;

                        if(appPref.getBirth() == -1)
                            intent = new Intent(getApplicationContext(), BirthActivity.class);
                        else
                            intent = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(intent);
                        finish();
                   });
                });
            });
        });
    }

    private void initFirebase() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰: " + token);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
