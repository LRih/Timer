package ric.ov.Timer.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public abstract class BaseActivity extends Activity
{
    //========================================================================= CONSTANTS
    private static final String SAVENAME_PREFERENCES = "settings";
    private static final String SAVENAME_COUNTDOWN = "saveCountdown";

    //========================================================================= FUNCTIONS
    protected final int GetCountdown()
    {
        SharedPreferences prefs = getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(SAVENAME_COUNTDOWN, 10);
    }
    protected final void SaveCountdown(int value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(SAVENAME_PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(SAVENAME_COUNTDOWN, value);
        editor.commit();
    }
}
