package com.jsdev.rumie.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.jsdev.ruime.structures.Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CodeHelper {
    public static final String TAG = "CodeHelper";
    public static final String KEY = "key";
    public static final String DURATION = "duration";
    public static final String CODES = "codes";
    private static final List<Code> codeList = new ArrayList<Code>();
    public static final String CODE_DATA = "Code Data";

    public static void addCodes(Context context, List<Code> newList) {
        if (codeList.size() <= 0)
            fetchCodes(context);

        for (Code aNewList : newList) {
            codeList.add(aNewList);
        }

        saveCodes(context);
    }

    public static void clearList(Context context) {
        codeList.clear();
        saveCodes(context);
    }

    public static List<Code> getCodes(Context context) {
        if (codeList.size() <= 0)
            fetchCodes(context);

        return codeList;
    }

    public static Code isValid(Context context, String code) {
        if (codeList.size() <= 0)
            fetchCodes(context);


        for (int i = 0; i < codeList.size(); i++) {
            if (codeList.get(i).validCode.equals(code)) {
                Code c = codeList.remove(i);
                saveCodes(context);
                return c;
            }
        }

        return null;
    }

    private static void fetchCodes(Context context) {
        SharedPreferences prefs = getPreferences(context);
        codeList.clear();

        try {

            JSONArray data = new JSONArray(prefs.getString(CODES, null));

            for (int i = 0; i < data.length(); i++) {
                JSONObject code = data.getJSONObject(i);
                codeList.add(new Code(code.getString(KEY), code.getInt(DURATION)));
            }
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage(),e);
        } catch (RuntimeException e) {
            Log.e(TAG,e.getMessage(),e);
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(CODE_DATA, Context.MODE_PRIVATE);
    }

    private static void saveCodes(Context context) {
        JSONArray array = new JSONArray();

        for (Code aCodeList : codeList) {
            try {
                JSONObject code = new JSONObject();
                code.put(KEY, aCodeList.validCode);
                code.put(DURATION, aCodeList.validDuration);
                array.put(code);
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage(),e);
            }
        }

        Editor edit = getPreferences(context).edit();
        edit.putString(CODES, array.toString());
        edit.commit();
    }
}
