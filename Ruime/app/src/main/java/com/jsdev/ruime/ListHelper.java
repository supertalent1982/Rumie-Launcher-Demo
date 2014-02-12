package com.jsdev.ruime;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ListHelper {

    public static final int LIST_EDUCATION = 1;
    private static final String TAG = "ListHelper";
    public static final int LIST_GAMES = 2;
    public static final int LIST_ADMIN = 3;

    private static HashMap<String, Boolean> adminMap;
    private static HashMap<String, Boolean> gameMap;
    private static HashMap<String, Boolean> learnMap;

    public static boolean containsPackage(Context context, String pack, int list) {
        switch (list) {
            case LIST_EDUCATION:
                if (learnMap == null)
                    getLearnList(context);

                return learnMap.containsKey(pack);

            case LIST_GAMES:
                if (gameMap == null)
                    getGameList(context);

                return gameMap.containsKey(pack);

            case LIST_ADMIN:
                if (adminMap == null)
                    getAdminList();

                return adminMap.containsKey(pack);

        }

        return false;
    }

    private static void getAdminList() {
        adminMap = new HashMap<String, Boolean>();

        adminMap.put("com.android.settings", true);
        adminMap.put("com.adobe.flashplayer", true);
        adminMap.put("com.android.vending", true);
    }

    private static void getGameList(Context context) {
        gameMap = new HashMap<String, Boolean>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.play);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                gameMap.put(line, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getLearnList(Context context) {
        learnMap = new HashMap<String, Boolean>();

        InputStream inputStream = context.getResources().openRawResource(R.raw.education);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                learnMap.put(line, true);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
