package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;

public class TermsActivity extends AppCompatActivity {

    private ConstraintLayout root;
    private AppPref appPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        root = findViewById(R.id.layout_terms);
        appPref = new AppPref(this);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_terms:
                appPref.setTermsAgree(true);
                root.animate().alpha(0).setDuration(150).withEndAction(() -> {
                    finish();
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
