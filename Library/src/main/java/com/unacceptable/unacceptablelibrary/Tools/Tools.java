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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unacceptable.unacceptablelibrary.Adapters.IAdapterViewControl;
import com.unacceptable.unacceptablelibrary.Adapters.NewAdapter;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe.SimpleItemTouchHelperCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    public static int ParseInt(EditText et) {
        return Tools.ParseInt(et.getText().toString());
    }

    public static double ParseDouble(EditText et) {
        return Tools.ParseDouble(et.getText().toString());
    }

    public static void SetText(TextView textView, Object value) {
        if (value == null) value = "";
        textView.setText(String.valueOf(value));
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
        return setupRecyclerView(recyclerView, context, iItemLayout, iDialogLayout, bAddEmpty, adapterViewControl, bAddDividerLine, bAddHorizontalSpacing, false);
    }

    public static NewAdapter setupRecyclerView(RecyclerView recyclerView, Context context, int iItemLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl adapterViewControl, boolean bAddDividerLine, boolean bAddHorizontalSpacing, boolean bAddSwipeGestures) {
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

        if (bAddSwipeGestures) {
            SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            adapter.attachTouchCallback(touchHelper, callback);
            touchHelper.attachToRecyclerView(recyclerView);
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

        if (error == null) // || error.networkResponse == null)
            return "Null volley error";

        int iStatusCode = -1;
        try {
            iStatusCode = error.networkResponse.statusCode;
        } catch (Exception e) {

        }

        if (iStatusCode != 200 && iStatusCode != -1) {
            sMessage = "Error: Response Code: " + String.valueOf(iStatusCode);
        } else {
            //error.networkResponse.
            sMessage = "Error: " + error.getMessage();
        }

        return sMessage;
    }

    public static <T> void PopulateAdapter(NewAdapter adapter, ArrayList<T> objects) {
        for(T l : objects) {
            ListableObject o = (ListableObject)l;
            adapter.add(o);
        }
    }

    public static <T> void PopulateDropDown(Spinner spinner, Context context, T[] data) {
        ArrayAdapter<T> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(aa);
    }


    public static <T> void SetDropDownSelection(Spinner spinner, T[] dataset, T selectedObject) {
        int position = FindPositionInArray(dataset, selectedObject);

        spinner.setSelection(position);
    }

    public static <T> int FindPositionInArray(T[] dataset, T selectedObject) {
        /*int position = 0;
        for (T o : dataset) {
            if (o.equals(selectedObject)) break;
            position++;
        }

        return position;*/
        return Arrays.asList(dataset).indexOf(selectedObject);
    }

    public static <T> ArrayList<T> ConvertToStrongTypedArrayList(ArrayList<ListableObject> dataset) {
        ArrayList<T> list = new ArrayList<>();
        for (ListableObject l : dataset) {
            T exercisePlan = (T)l;
            list.add(exercisePlan);
        }

        return list;
    }


    public static String RoundString(double num, int decimal) {
        DecimalFormat f = new DecimalFormat("##." + StringRepeat("0", decimal));
        return f.format(num);
    }

    public static String StringRepeat(String s, int num) {
        String r = "";
        for (int i = 0; i < num; i++) {
            r += s;
        }

        return r;
    }

    public static double DecimalPart(double value) {
        String valueAsString = String.valueOf(value);
        int decimalPos = valueAsString.indexOf(".");
        String sDecimalPart = valueAsString.substring(decimalPos + 1);
        return Tools.ParseDouble(sDecimalPart);
    }

    public static void hideKeyboard(Activity a) {
        InputMethodManager imm = (InputMethodManager) a.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = a.getCurrentFocus();
        if (v == null)
            v = new View(a);

        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
