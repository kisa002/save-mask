package com.haeyum.savemask;

import android.app.Activity;
import android.content.Intent;

public class NoticeManager {
    public static void createNotice(Activity activity, String title, String context) {
        Intent intent = new Intent(activity, NoticeActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("context", context);
        activity.startActivity(intent);
    }
//
//    public static void createNotice(Activity activity, String title, String context, IFNotice ifNotice) {
//        Intent intent = new Intent(activity, NoticeActivity.class);
//        intent.putExtra("title", title);
//        intent.putExtra("context", context);
//        activity.startActivity(intent);
//    }
}
