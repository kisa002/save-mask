package com.haeyum.savemask;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class FirebaseManager {
    public static FirebaseManager instance;

    public static FirebaseManager getInstance() {
        if(instance == null)
            instance = new FirebaseManager();

        return instance;
    }

    public void sign(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference drInfo = database.getReference("info");
        drInfo.child("logCount").setValue(111554);
//        DatabaseReference myRef = database.getReference("users").child(String.valueOf(drInfo.child("logCount")));

//        Log.d("asdasd", "LogCount: " + drInfo.child("logCount"));

//        String date = Calendar.getInstance().get(Calendar.YEAR) + "." + Calendar.getInstance().get(Calendar.MONTH) + "." + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
//
//        myRef.child("id").setValue(id);
//        myRef.child("date").setValue(date);
    }

    public void log(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("logs").child(id);

        String date = Calendar.getInstance().get(Calendar.YEAR) + "." + Calendar.getInstance().get(Calendar.MONTH) + "." + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
        myRef.setValue(date);
    }
}
