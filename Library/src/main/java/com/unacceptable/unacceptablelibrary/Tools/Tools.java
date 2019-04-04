package com.unacceptable.unacceptablelibrary.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unacceptable.unacceptablelibrary.Adapters.IAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zak on 11/16/2016.
 */

public class Tools {

    public static int SAMSUNG_CURVED_SCREEN_PADDING = 25;

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
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
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

    public static Date setTimeToMidnightUTC(Date date) {
        Date dt = setTimeToMidnight(date);
        return  dateToUTC(dt);
    }

    public static Date dateFromUTC(Date date){
        return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
    }

    public static Date dateToUTC(Date date){
        return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
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

    //TODO: Need to handle this returning a Response object
    public static <T> T convertJsonResponseToObject(String json, Class<T> c) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, c);
    }


    public static NewAdapter setupRecyclerView(RecyclerView recyclerView, Context context, int iItemLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl adapterViewControl) {
        return setupRecyclerView(recyclerView, context, iItemLayout, iDialogLayout, bAddEmpty, adapterViewControl, false);
    }

    public static NewAdapter setupRecyclerView(RecyclerView recyclerView, Context context, int iItemLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl adapterViewControl, boolean bAddDividerLine) {
        return setupRecyclerView(recyclerView, context, iItemLayout, iDialogLayout, bAddEmpty, adapterViewControl, bAddDividerLine, false);
    }

    public static NewAdapter setupRecyclerView(RecyclerView recyclerView, Context context, int iItemLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl adapterViewControl, boolean bAddDividerLine, boolean bAddHorizontalSpacing) {
        recyclerView.setHasFixedSize(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);


        NewAdapter adapter = new NewAdapter(iItemLayout, iDialogLayout, bAddEmpty, adapterViewControl);

        recyclerView.setAdapter(adapter);

        if (bAddDividerLine) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        if (bAddHorizontalSpacing) {
            recyclerView.addItemDecoration(new HoriztonalSpaceItemDecoration(Tools.SAMSUNG_CURVED_SCREEN_PADDING));
        }

        return adapter;
    }

    public static Calendar createDate(int month, int day, int year, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String ParseVolleyError(VolleyError error) {
        String sMessage;

        int iStatusCode = error.networkResponse.statusCode;
        if (iStatusCode != 200) {
            sMessage = "Error: Response Code: " + String.valueOf(iStatusCode);
        } else {
            //error.networkResponse.
            sMessage = "Error: " + error.getMessage();
        }

        return sMessage;
    }
}
