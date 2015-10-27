package ric.ov.Timer.Activities;

import android.app.Activity;
import ric.ov.OneVisual.Utils.PreferencesUtils;

public abstract class BaseActivity extends Activity
{
    //========================================================================= CONSTANTS
    protected static final String SAVENAME_COUNTDOWN = "countdown";

    //========================================================================= FUNCTIONS
    protected final int GetCountdown()
    {
        return PreferencesUtils.GetSharedPreferences(this).getInt(SAVENAME_COUNTDOWN, 10);
    }
    protected final void SaveCountdown(int value)
    {
        PreferencesUtils.SaveSharedPreference(this, SAVENAME_COUNTDOWN, value);
    }
}
