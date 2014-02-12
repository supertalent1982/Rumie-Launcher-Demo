package com.jsdev.ruime.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.jsdev.ruime.PlayerActivity;

public class UnlockScreenReceiver extends BroadcastReceiver {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent newIntent = new Intent(context, PlayerActivity.class);
        newIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(newIntent);
    }
}
