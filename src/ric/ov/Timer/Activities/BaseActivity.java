package ric.ov.Timer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class BaseActivity extends Activity
{
    //========================================================================= CONSTANTS
    private static final String SAVENAME_PREFERENCES = "settings";
    private static final String SAVENAME_COUNTDOWN = "saveCountdown";

    protected static final int REFRESH_RATE = 37;

    //========================================================================= FUNCTIONS
    protected final int loadCountdown()
    {
        SharedPreferences prefs = getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(SAVENAME_COUNTDOWN, 10);
    }
    protected final void saveCountdown(int value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(SAVENAME_COUNTDOWN, value);
        editor.commit();
    }
}
