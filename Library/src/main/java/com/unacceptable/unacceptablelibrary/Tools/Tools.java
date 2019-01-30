package com.unacceptable.unacceptablelibrary.Tools;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static boolean LoadSharedPrefs(Context ctx) {
        m_sharedPrefs = ctx.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        return m_sharedPrefs != null;
    }


    public static DatabaseServer Server = DatabaseServer.BeerNet;

    enum DatabaseServer {
        Desktop {
            public String toString() {
                return "http://localhost:50421/beernet";
            }
        },

        Deployd {
            public String toString() {
                return "http://rest.unacceptable.beer:2403";
            }
        },

        BeerNet {
            public String toString() {
                return "http://rest.unacceptable.beer:5123/beernet";
            }
        }

    }

    //TODO: I don't really like having these here but they work for now...
    public static String RestAPIURL() {
        /*if (UseTestServer)
            return "http://192.168.1.11:50421/beernet";

        return "http://rest.unacceptable.beer:2403";*/
        return Server.toString();
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
}
