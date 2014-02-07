package com.jsdev.rumie.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jsdev.ruime.structures.Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CodeHelper {

    private static List<Code> codeList;

    public static void addCodes(Context context, List<Code> newList) {
        if (codeList == null || codeList.size() < 1)
            fetchCodes(context);

        for (int i = 0; i < newList.size(); i++)
            codeList.add(newList.get(i));

        saveCodes(context);
    }

    public static void clearList(Context context) {
        codeList = new ArrayList<Code>();
        saveCodes(context);
    }

    public static List<Code> getCodes(Context context) {
        if (codeList == null || codeList.size() < 1)
            fetchCodes(context);

        return codeList;
    }

    public static Code isValid(Context context, String code) {
        if (codeList == null || codeList.size() < 1)
            fetchCodes(context);

        System.out.println("Test: " + code);

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
        codeList = new ArrayList<Code>();

        try {
            System.out.println("Codes: " + prefs.getString("codes", null));
            JSONArray data = new JSONArray(prefs.getString("codes", null));

            for (int i = 0; i < data.length(); i++) {
                JSONObject code = data.getJSONObject(i);
                codeList.add(new Code(code.getString("key"), code.getInt("duration")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("Code Data", Context.MODE_PRIVATE);
    }

    private static void saveCodes(Context context) {
        JSONArray array = new JSONArray();

        for (int i = 0; i < codeList.size(); i++) {
            try {
                JSONObject code = new JSONObject();
                code.put("key", codeList.get(i).validCode);
                code.put("duration", codeList.get(i).validDuration);
                array.put(code);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Editor edit = getPreferences(context).edit();
        edit.putString("codes", array.toString());
        edit.commit();
    }
}
