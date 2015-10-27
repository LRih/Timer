package ric.ov.Timer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public final class SecondSelectDialog
{
    //========================================================================= VARIABLES
    private final Button _btnUp;
    private final Button _btnDown;
    private final Button _btnMinuteUp;
    private final Button _btnMinuteDown;
    private final TextView _txtValue;
    private final AlertDialog _dialog;

    private Handler _handler;
    private int _value;
    private int _direction;

    //========================================================================= INITIALIZE
    public SecondSelectDialog(Context context, int value)
    {
        _value = value;
        _dialog = new AlertDialog.Builder(context).create();
        _dialog.setTitle(context.getString(R.string.set_time));
        // inflate layout
        LayoutInflater inflater = _dialog.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_second_select, null);
        // controls
        _btnUp = (Button)view.findViewById(R.id.btnUp);
        _btnDown = (Button)view.findViewById(R.id.btnDown);
        _btnMinuteUp = (Button)view.findViewById(R.id.btnMinuteUp);
        _btnMinuteDown = (Button)view.findViewById(R.id.btnMinuteDown);
        _txtValue = (TextView)view.findViewById(R.id.txtValue);
        _btnUp.setOnTouchListener(btnUp_onTouch);
        _btnDown.setOnTouchListener(btnDown_onTouch);
        _btnMinuteUp.setOnTouchListener(btnMinuteUp_onTouch);
        _btnMinuteDown.setOnTouchListener(btnMinuteDown_onTouch);
        _txtValue.setText(String.valueOf(_value));
        _dialog.setView(view);
        _handler = new Handler();
    }

    //========================================================================= FUNCTIONS
    public final void SetButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener)
    {
        _dialog.setButton(whichButton, text, listener);
    }
    public final void Show()
    {
        _dialog.show();
    }

    //========================================================================= PROPERTIES
    public final int Value()
    {
        return _value;
    }
    public final void SetValue(int value)
    {
        _value = Math.min(Math.max(value, 0), 999);
        _txtValue.setText(String.valueOf(_value));
    }

    //========================================================================= EVENTS
    private final Runnable handler_run = new Runnable()
    {
        public void run()
        {
            SetValue(_value + _direction);
            _handler.postDelayed(this, 50);
        }
    };

    private final View.OnTouchListener btnMinuteUp_onTouch = new View.OnTouchListener()
    {
        public final boolean onTouch(View view, MotionEvent motionEvent)
        {
            return ButtonOnTouch(60, motionEvent);
        }
    };
    private final View.OnTouchListener btnMinuteDown_onTouch = new View.OnTouchListener()
    {
        public final boolean onTouch(View view, MotionEvent motionEvent)
        {
            return ButtonOnTouch(-60, motionEvent);
        }
    };
    private final View.OnTouchListener btnUp_onTouch = new View.OnTouchListener()
    {
        public final boolean onTouch(View view, MotionEvent motionEvent)
        {
            return ButtonOnTouch(1, motionEvent);
        }
    };
    private final View.OnTouchListener btnDown_onTouch = new View.OnTouchListener()
    {
        public final boolean onTouch(View view, MotionEvent motionEvent)
        {
            return ButtonOnTouch(-1, motionEvent);
        }
    };
    private boolean ButtonOnTouch(int direction, MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                _direction = direction;
                SetValue(_value + _direction);
                _handler.postDelayed(handler_run, 750);
                break;
            case MotionEvent.ACTION_UP:
                _handler.removeCallbacks(handler_run);
                break;
        }
        return false;
    }
}