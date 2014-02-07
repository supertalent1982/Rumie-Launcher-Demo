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
/*
        try {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN);
            mainIntent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
            String currentHomePackage = resolveInfo.activityInfo.packageName;

        } catch (RuntimeException e) {
            Log.e("Receiver", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("Receiver", e.getMessage(), e);
        }
*/
        Intent newIntent = new Intent(context, PlayerActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}
