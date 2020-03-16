package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_developer_back:
                finish();
                break;
        }
    }
}
