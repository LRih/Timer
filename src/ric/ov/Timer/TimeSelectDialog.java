package ric.ov.Timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public final class TimeSelectDialog
{
    //========================================================================= VARIABLES
    private static final int MAX_TIME = 36000; // 10 hours

    private final AlertDialog _dialog;

    private EditText _txtMins;
    private EditText _txtSecs;

    //========================================================================= INITIALIZE
    public TimeSelectDialog(Context context, int seconds)
    {
        _dialog = new AlertDialog.Builder(context)
                                 .setTitle(context.getString(R.string.set_time))
                                 .create();

        View view = initializeViews();

        _dialog.setView(view);

        // set seconds and focus on seconds text box
        setSeconds(seconds);
        _txtSecs.requestFocus();

        // enable keyboard on load
        _dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private View initializeViews()
    {
        LayoutInflater inflater = _dialog.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_time_select, null, false);

        _txtMins = (EditText)view.findViewById(R.id.txtMins);
        _txtSecs = (EditText)view.findViewById(R.id.txtSecs);

        _txtMins.setSelectAllOnFocus(true);
        _txtSecs.setSelectAllOnFocus(true);

        _txtMins.setOnFocusChangeListener(txtMins_onFocusChange);
        _txtSecs.setOnFocusChangeListener(txtSecs_onFocusChange);

        return view;
    }

    //========================================================================= FUNCTIONS
    private int toValidMinuteRange(int minutes)
    {
        return Math.min(Math.max(minutes, 0), MAX_TIME / 60);
    }
    private int toValidSecondRange(int seconds)
    {
        return Math.min(Math.max(seconds, 0), 60);
    }

    public final void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener)
    {
        _dialog.setButton(whichButton, text, listener);
    }
    public final void show()
    {
        _dialog.show();
    }

    //========================================================================= PROPERTIES
    private int rawMinute()
    {
        if (_txtMins.length() == 0)
            return 0;
        return Integer.parseInt(_txtMins.getText().toString());
    }
    private int rawSecond()
    {
        if (_txtSecs.length() == 0)
            return 0;
        return Integer.parseInt(_txtSecs.getText().toString());
    }

    public final int seconds()
    {
        int mins = toValidMinuteRange(rawMinute());
        int secs = toValidSecondRange(rawSecond());

        return Math.min(Math.max(mins * 60 + secs, 0), MAX_TIME);
    }
    public final void setSeconds(int seconds)
    {
        _txtMins.setText(String.valueOf(seconds / 60));
        _txtSecs.setText(String.valueOf(seconds % 60));
    }

    //========================================================================= EVENTS
    private final View.OnFocusChangeListener txtMins_onFocusChange = new View.OnFocusChangeListener()
    {
        public final void onFocusChange(View v, boolean hasFocus)
        {
            if (!hasFocus)
                _txtMins.setText(String.valueOf(toValidMinuteRange(rawMinute())));
        }
    };

    private final View.OnFocusChangeListener txtSecs_onFocusChange = new View.OnFocusChangeListener()
    {
        public final void onFocusChange(View v, boolean hasFocus)
        {
            if (!hasFocus)
                _txtSecs.setText(String.valueOf(toValidSecondRange(rawSecond())));
        }
    };
}