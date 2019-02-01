package com.unacceptable.unacceptablelibrary.Tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by zak on 11/16/2016.
 */

public class Tools {

    public static SharedPreferences m_sharedPrefs;
    public static String m_sAPISubName; //TODO: Should i save this elsewhere? In shared prefs? probably?

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

    public static String GetAPIToken() {
        String APIToken = m_sharedPrefs.getString("APIToken", "");
        return APIToken;
    }

    public static boolean LoadSharedPrefs(Context ctx, String sAPISubName) {
        m_sharedPrefs = ctx.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        //return m_sharedPrefs != null;
        if (m_sharedPrefs == null) {
            Tools.ShowToast(ctx, "Failed to load preferences", Toast.LENGTH_SHORT);
            return false;
        }

        m_sAPISubName = sAPISubName;

        return true;
    }


    public static DatabaseServer Server = DatabaseServer.Desktop;

    enum DatabaseServer {
        Desktop {
            public String toString() {
                return "http://192.168.1.13:50421/";
            }
        },

        Deployd {
            public String toString() {
                return "http://rest.unacceptable.beer:2403";
            }
        },

        BeerNet {
            public String toString() {
                return "http://rest.unacceptable.beer:5123/";
            }
        }

    }

    //TODO: I don't really like having these here but they work for now...
    public static String RestAPIURL() {
        /*if (UseTestServer)
            return "http://192.168.1.11:50421/beernet";

        return "http://rest.unacceptable.beer:2403";*/
        return Server.toString() + m_sAPISubName;
    }

    public static String HealthAPIURL() {
        return Server.toString() + "health";
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

    public static Boolean LoginTokenExists(Activity ctx) {
        if (Tools.GetAPIToken().length() > 0) return true;

        LaunchSignInScreen(ctx);
        return false;
    }

    public static void LaunchSignInScreen(Activity ctx) {
        Intent i = new Intent(ctx, com.unacceptable.unacceptablelibrary.Screens.LoginActivity.class);
        ctx.startActivity(i);
    }
}
