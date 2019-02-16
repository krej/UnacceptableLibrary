package com.unacceptable.unacceptablelibrary.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zak on 11/16/2016.
 */

public class Tools {



    public static double ParseDouble(String d) {
        if (d.length() == 0 ) return 0;
        return Double.parseDouble(d);
    }
    public static int ParseInt(String d) {
        if (d.length() == 0 ) return 0;
        return Integer.parseInt(d);
    }

    public static void ShowToast(Context c, CharSequence text, int length) {
        Toast t = Toast.makeText(c, text, length);
        t.show();
    }


    public static String SanitizeDeploydJSON(String response) {
        String json = response;
        json = json.replace("[", "[\n");
        json = json.replace("]", "]\n");
        json = json.replace("},", "}\n");
        return json;
    }

    public static ArrayList<JSONObject> GetJSONObjects(String json) {
        ArrayList<JSONObject> objs = new ArrayList<JSONObject>();
        json = Tools.SanitizeDeploydJSON(json);
        try (StringReader sr = new StringReader(json); BufferedReader in = new BufferedReader(sr)) {
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    if (line.equals("[") || line.equals("]")) continue;
                    JSONObject object = new JSONObject(line);
                    objs.add(object);
                }
            } catch (JSONException ex) {

            }
        } catch(IOException e) {

        }

        return objs;
    }

    public static Boolean LoginTokenExists(Activity ctx, Class<?> cNextActivity) {
        if (Preferences.GetAPIToken().length() > 0) return true;

        LaunchSignInScreen(ctx, cNextActivity);
        return false;
    }

    public static void LaunchSignInScreen(Activity ctx, Class<?> cNextActivity) {
        Intent i = new Intent(ctx, com.unacceptable.unacceptablelibrary.Screens.LoginActivity.class);
        i.putExtra("NextActivity", cNextActivity);
        ctx.startActivity(i);
    }

    public static String FormatDate(Date dt, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(dt);
    }

    public static boolean IsEmptyString(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean CompareDatesWithoutTime(Date d1, Date d2) {
        d1 = setTimeToMidnight(d1);
        d2 = setTimeToMidnight(d2);
        boolean result = d2.compareTo(d1) == 0;
        return result;
    }

    public static Date setTimeToMidnight(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( date );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
