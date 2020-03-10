package com.haeyum.savemask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

public class BirthActivity extends AppCompatActivity {

    private ImageView ivBirthGif;

    private ConstraintLayout clBirthMain;

    private ArrayList<Button> btnNumbers = new ArrayList();

    private boolean isAnimate;

    private int selectedNumber;

    private AppPref appPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth);

        initUI();
        appPref = new AppPref(this);
    }

    private void initUI() {
        ivBirthGif = findViewById(R.id.iv_birth_gif);

        // NEXT
        clBirthMain = findViewById(R.id.cl_birth_main);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(ivBirthGif);
        Glide.with(this).load(R.drawable.calendar2).into(gifImage);

        clBirthMain.post(()-> {
            clBirthMain.animate().xBy(clBirthMain.getWidth()).setDuration(0);
        });

        btnNumbers.add(findViewById(R.id.btn_birth_number0));
        btnNumbers.add(findViewById(R.id.btn_birth_number1));
        btnNumbers.add(findViewById(R.id.btn_birth_number2));
        btnNumbers.add(findViewById(R.id.btn_birth_number3));
        btnNumbers.add(findViewById(R.id.btn_birth_number4));
        btnNumbers.add(findViewById(R.id.btn_birth_number5));
        btnNumbers.add(findViewById(R.id.btn_birth_number6));
        btnNumbers.add(findViewById(R.id.btn_birth_number7));
        btnNumbers.add(findViewById(R.id.btn_birth_number8));
        btnNumbers.add(findViewById(R.id.btn_birth_number9));

        int i = 0;
        for(Button btn : btnNumbers) {
            int finalI = i;
            btn.setOnClickListener(v -> {
                for(Button t : btnNumbers) {
                    t.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                selectedNumber = Integer.parseInt(btn.getText().toString());

//                Toast.makeText(this, "N:" + selectedNumber, Toast.LENGTH_SHORT).show();
                btnNumbers.get(finalI).setBackgroundColor(Color.parseColor("#e1e1e1"));
            });

            i++;
        }
    }

    private void showNext() {
        if(!isAnimate) {
            isAnimate = true;

            clBirthMain.animate().xBy(-clBirthMain.getWidth()).setDuration(400).withEndAction(() -> {
                isAnimate = false;
            });
        }
    }

    private void hideNext() {
        if(!isAnimate) {
            isAnimate = true;

            clBirthMain.animate().xBy(clBirthMain.getWidth()).setDuration(400).withEndAction(() -> {
                isAnimate = false;

                for(Button btn : btnNumbers) {
                    btn.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            });
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_birth_next:
                showNext();
                break;

            case R.id.tv_birth_skip:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;

            case R.id.btn_birth_save:
                appPref.setBirth(selectedNumber);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;

            case R.id.tv_birth_back:
                hideNext();
                break;
        }
    }
}
