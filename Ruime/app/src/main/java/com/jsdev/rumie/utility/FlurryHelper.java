package com.jsdev.rumie.utility;

import android.content.Context;

import com.flurry.android.FlurryAgent;

public class FlurryHelper {

    public static void trackEvent(Context context, String pack) {
        FlurryAgent.logEvent(pack, true);
        PrefsHelper.setRunningPackage(context, pack);
    }
}
