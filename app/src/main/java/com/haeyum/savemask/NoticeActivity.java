package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {

    private ConstraintLayout root;
    private TextView tvTitle, tvContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        tvTitle = findViewById(R.id.tv_notice_title);
        tvContext = findViewById(R.id.tv_notice_context);

        tvTitle.setText(getIntent().getStringExtra("title"));
        tvContext.setText(getIntent().getStringExtra("context"));

        root = findViewById(R.id.layout_notice);
        root.setAlpha(0);

        root.animate().alpha(1).setDuration(150);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notice_ok:
                root.animate().alpha(0).setDuration(150).withEndAction(() -> {
                    finish();
                });
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
