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

    protected Preferences(Context context, String sAPISubName) {
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
        /*int iServer;
        try {
            iServer = m_Settings.getInt("apiServer", 0);
        } catch (Exception ex) {
            String sServer = m_Settings.getString("apiServer", "0");
            iServer = ParseInt(sServer);
        }*/

        String sServer = "";
        try {
            sServer = m_Settings.getString("apiServer", "Desktop");
        } catch (Exception ex) {

        }

        return GetServerEnum(sServer);
    }

    protected static DatabaseServer GetServerEnum(String sServer) {
        /*switch (iServer) {
            case 0:
                return DatabaseServer.Desktop;
            case 1:
                return DatabaseServer.BeerNet;
            case 2:
                return DatabaseServer.LinuxDestop;
            case 3:
                return DatabaseServer.BeerNetDev;
        }

        return DatabaseServer.Desktop;*/

        switch (sServer) {
            case "HealthNet":
                return DatabaseServer.HealthNet;
            case "HealthNet_Desktop":
                return DatabaseServer.HealthNet_Desktop;
            case "BeerNet":
                return DatabaseServer.BeerNet;
            case "BeerNet_Desktop":
                return DatabaseServer.Desktop;
            case "BeerNet_Dev":
                return DatabaseServer.BeerNetDev;
        }

        return DatabaseServer.Desktop;
    }

    //TODO: I don't really like having these here but they work for now...
    public static String RestAPIURL() {
        //if (UseTestServer)
        //    return "http://192.168.1.11:50421/beernet";

        //return "http://rest.unacceptable.beer:2403";
        return Server().toString() + m_sAPISubName;
    }

    public static String HealthAPIURL() {
        return Server().toString() + "health";
    }

    public static String BeerNetAPIURL() {
        if (Server() == DatabaseServer.HealthNet)
            return DatabaseServer.BeerNet.toString() + "beernet";
        if (Server() == DatabaseServer.HealthNet_Desktop)
            return DatabaseServer.Desktop.toString() + "beernet";

        return Server().toString() + "beernet";
    }

    /*
        Note to future self: If you're here, you're probably wondering why you can't get the emulator to connect to the desktop API.
        The reason is because the emulator is it's own computer, so it cannot connect through http://localhost. It needs to use the IP address,
        but the API doesn't allow that by default.
        To fix it...

        The reason the port changes is because of the IIS Express or BeerNet solution profiles. Normally I use IIS because its the default,
        but sometimes it says it cannot connect to IIS. To fix this, Go to the profile properties and set the URL to http://localhost:50421.
        Then exit VS and delete the (project).vs\applicationhost.config file. Restart VS. It should start working.

        To fix not being able to connect, use the "BeerNet" profile. Open the following file:

        C:\Users\Zak\source\repos\BeerNet\BeerNet\Properties\launchSettings.json

        Change the BeerNet applicationUrl to http://192.168.1.6:50421

        Changing it in the VS UI DOES NOT WORK! I don't know why, but it doesn't keep.

     */
    protected enum DatabaseServer {
        Desktop {
            public String toString() {
                return "http://192.168.1.69:50421/";
            }
        },

        LinuxDestop {
            public String toString() { return "http://192.168.1.12:50422/"; }
        },

        BeerNet {
            public String toString() {
                return "https://rest.unacceptable.beer/";
            }
        },

        BeerNetDev {
            public String toString() {
                return "https://dev.unacceptable.beer/";
            }
        },

        HealthNet_Desktop {
            public String toString() { return "http://192.168.1.248:50422/"; }
        },

        HealthNet {
            public String toString() { return "http://rest.unacceptable.beer:999/"; }
        }

    }

}
