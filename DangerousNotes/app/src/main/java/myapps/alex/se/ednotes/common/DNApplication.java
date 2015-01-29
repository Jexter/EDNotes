package myapps.alex.se.ednotes.common;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by atkinson on 19/12/14.
 */
public class DNApplication extends Application {

    private static String CLASS_NAME = "DNApplication";
    private static Context context;

    @Override
    public void onCreate() {
        Log.d(CLASS_NAME, "onCreate()");
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }


}
