package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.iv_setting_back:
                finish();
                break;

            case R.id.cl_setting_birth:
                intent = new Intent(getApplicationContext(), BirthActivity.class);
                startActivity(intent);

                finish();
                MainActivity.activity.finish();
                break;

            case R.id.cl_setting_developer:
                intent = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent);
                break;
        }
    }
}
