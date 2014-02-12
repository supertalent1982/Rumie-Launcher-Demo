package com.jsdev.ruime;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.flurry.android.FlurryAgent;
import com.jsdev.ruime.adapters.LauncherAdapter;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    public static final String TIME = "time";
    private ViewPager viewPager;
    private LauncherAdapter launcherAdapter;

    private ProgressBar progressBar;

    private AppLoaderTask appLoader;

    private Handler mHandler = new Handler();

    private boolean isForeground = true;
    private boolean isReloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.tabPager);
        progressBar = (ProgressBar) findViewById(R.id.mainProgress);

        isReloading = true;

        if (appLoader != null) {
            appLoader.cancel(true);
            appLoader = null;
        }

        PrefsHelper.clearPackages(this);
        pollApps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        viewPager.setAdapter(launcherAdapter);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (!isForeground)
            super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        isForeground = false;

        if (appLoader != null) {
            appLoader.cancel(true);
            appLoader = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (appLoader != null) {
            appLoader.cancel(true);
            appLoader = null;
        }

        String pack = PrefsHelper.getRunningPackage(this);

        if (pack != null) {
            long startTime = PrefsHelper.getTime(this);

            if (startTime != 0) {
                Map<String, String> info = new HashMap<String, String>();
                info.put(TIME, String.valueOf(((System.currentTimeMillis() - startTime) / 1000)));
                FlurryAgent.logEvent(pack, info);

//                System.out.println("Interval: " + String.valueOf(((System.currentTimeMillis() - startTime) / 1000)));

                PrefsHelper.clearTime(this);
            }
        }

        isForeground = true;

        if (!isReloading) {
            if (launcherAdapter == null) {
                if (PrefsHelper.shouldReload(this))
                    pollApps();
                else if (PrefsHelper.getPackages(this).size() <= 0)
                    pollApps();
                else {
                    launcherAdapter = new LauncherAdapter(getSupportFragmentManager(), this);
                    viewPager.setAdapter(launcherAdapter);
                    viewPager.setCurrentItem(0);
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                viewPager.setAdapter(launcherAdapter);
                viewPager.setCurrentItem(0);
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, "NTPMMQCF85TZGMB4NTS9");
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void pollApps() {
        viewPager.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new AppLoaderTask(this, new AppListener() {

            @Override
            public void onComplete(Applications apps) {
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            launcherAdapter = new LauncherAdapter(getSupportFragmentManager(), MainActivity.this);
                            viewPager.setAdapter(launcherAdapter);
                            viewPager.setCurrentItem(0);
                            viewPager.setVisibility(View.VISIBLE);

                            progressBar.setVisibility(View.GONE);

                            PrefsHelper.updatedPackage(MainActivity.this);
                        }
                    });
                } catch (IllegalStateException e) {
                    Log.e(TAG,e.getMessage(),e);
                }
            }

        }).execute();
    }

    private class AppLoaderTask extends AsyncTask<Void, Void, Applications> {

        private Context mContext;
        private AppListener mListener;

        public AppLoaderTask(Context context, AppListener listener) {
            this.mContext = context;
            this.mListener = listener;
        }

        @Override
        protected Applications doInBackground(Void... params) {
            return new Applications(mContext, mContext.getPackageManager());
        }

        @Override
        protected void onPostExecute(Applications apps) {
            if (mListener != null)
                mListener.onComplete(apps);
        }

    }

    public interface AppListener {
        public void onComplete(Applications apps);
    }

}
