package com.scu.timetable.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, AlarmService.class);
//        context.startService(service);
        AlarmService.start(context);
    }

}
