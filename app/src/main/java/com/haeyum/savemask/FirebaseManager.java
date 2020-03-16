package com.haeyum.savemask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;

import dagger.multibindings.ElementsIntoSet;

import static com.haeyum.savemask.NoticeManager.createNotice;

public class FirebaseManager {
    public static FirebaseManager instance;

    private int userCount = -1;
    private int checkSigned = -1;

    private String version, updateLog;

    public static FirebaseManager getInstance() {
        if(instance == null)
            instance = new FirebaseManager();

        return instance;
    }

    public void sign(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("info").child("userCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCount = Integer.parseInt(dataSnapshot.getValue().toString()) + 1;

                if(checkSigned == 0) {
                    String date = Calendar.getInstance().get(Calendar.YEAR) + "." + Calendar.getInstance().get(Calendar.MONTH) + "." + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);

                    database.getReference("users").child(id).child("id").setValue(id);
                    database.getReference("users").child(id).child("userCount").setValue(userCount);
                    database.getReference("users").child(id).child("signedTimestamp").setValue(date);

                    database.getReference("info").child("userCount").setValue(userCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference("users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    if(userCount != -1) {
                        String timestamp = Calendar.getInstance().get(Calendar.YEAR) + "." + Calendar.getInstance().get(Calendar.MONTH) + "." + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);

                        database.getReference("users").child(id).child("id").setValue(id);
                        database.getReference("users").child(id).child("userCount").setValue(userCount);
                        database.getReference("users").child(id).child("signedTimestamp").setValue(timestamp);

                        database.getReference("info").child("userCount").setValue(userCount);
                    }

                    checkSigned = 0;
                } else
                    checkSigned = -1000;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkVersion(Activity act) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info").child("update");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap = (HashMap<String, String>) dataSnapshot.getValue();

                if(!hashMap.get("version").equals("1.0.2")) {
                    createNotice(act, "업데이트 안내", hashMap.get("log")); //새로운 버전이 출시되었습니다. 플레이스토어에서 최신 버전으로 업데이트 바랍니다 :D

                    final String appPackageName = act.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addLog(String id, String reason) {
        Calendar calendar = Calendar.getInstance();
        String cal = calendar.get(Calendar.YEAR) + "" + ((calendar.get(Calendar.MONTH) < 10) ? "0" + calendar.get(Calendar.MONTH) : calendar.get(Calendar.MONTH)) + "" + calendar.get(Calendar.DAY_OF_MONTH);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("info").child("log").child(cal);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int logCount = 1;
                if(dataSnapshot.getValue() != null)
                    logCount = Integer.parseInt(dataSnapshot.getValue().toString()) + 1;

                String timestamp = calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

                database.getReference("info").child("log").child(cal).setValue(logCount);

                database.getReference("log").child(cal).child(String.valueOf(logCount)).child("id").setValue(id);
                database.getReference("log").child(cal).child(String.valueOf(logCount)).child("reason").setValue(reason);
                database.getReference("log").child(cal).child(String.valueOf(logCount)).child("timestamp").setValue(timestamp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
