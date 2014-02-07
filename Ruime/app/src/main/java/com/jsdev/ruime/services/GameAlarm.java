package com.jsdev.ruime.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jsdev.ruime.DialogActivity;
import com.jsdev.rumie.utility.PrefsHelper;


public class GameAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        PrefsHelper.isActive = false;

        Intent timerIntent = new Intent(context, DialogActivity.class);
        timerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(timerIntent);
    }

}
