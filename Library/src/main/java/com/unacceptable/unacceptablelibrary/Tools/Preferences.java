package com.unacceptable.unacceptablelibrary.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import static com.unacceptable.unacceptablelibrary.Tools.Tools.ParseInt;

public class Preferences {
    private static SharedPreferences m_sharedPrefs;
    private static SharedPreferences m_Settings;
    private static Preferences mInstance;
    public static String m_sAPISubName; //TODO: Should i save this elsewhere? In shared prefs? probably?
    //private static Context mCtx;

    private Preferences(Context context, String sAPISubName) {
        //I didn't save context because AndroidStudio yelled at me about having android objects as static variables
        //but maybe this will need to change as you change preferences, maybe it needs to recreate the preferences stuff? dont know
        //mCtx = context;
        m_sharedPrefs = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        m_Settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (m_sharedPrefs == null) {
            Tools.ShowToast(context, "Failed to load preferences", Toast.LENGTH_SHORT);
            //return false;
        }
        m_sAPISubName = sAPISubName;
    }

    public static synchronized Preferences getInstance(Context context, String sAPISubName) {
        if (mInstance == null) {
            mInstance = new Preferences(context, sAPISubName);
        }
        return mInstance;
    }

    public static String GetAPIToken() {
        String APIToken = m_sharedPrefs.getString("APIToken", "");
        return APIToken;
    }

    public static boolean ServerSettingExists() {
        return m_Settings.contains("apiServer");
    }

    public static DatabaseServer Server() {
        int iServer;
        try {
            iServer = m_Settings.getInt("apiServer", 0);
        } catch (Exception ex) {
            String sServer = m_Settings.getString("apiServer", "0");
            iServer = ParseInt(sServer);
        }

        switch (iServer) {
            case 0:
                return DatabaseServer.Desktop;
            case 1:
                return DatabaseServer.BeerNet;
        }

        return DatabaseServer.Desktop;
    }

    //TODO: I don't really like having these here but they work for now...
    public static String RestAPIURL() {
        /*if (UseTestServer)
            return "http://192.168.1.11:50421/beernet";

        return "http://rest.unacceptable.beer:2403";*/
        return Server().toString() + m_sAPISubName;
    }

    public static String HealthAPIURL() {
        return Server().toString() + "health";
    }

    public static String BeerNetAPIURL() {return Server().toString() + "beernet"; }


    enum DatabaseServer {
        Desktop {
            public String toString() {
                return "http://192.168.1.10:50421/";
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

}
