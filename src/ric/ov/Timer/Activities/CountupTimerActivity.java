package ric.ov.Timer.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import ric.ov.Timer.R;
import ric.ov.Timer.Timers.Timer;

public final class CountupTimerActivity extends BaseActivity
{
    //========================================================================= VARIABLES
    private View _viewStatus;

    private TextView _txtMin;
    private TextView _txtSec;
    private TextView _txtMS;

    private TextView _txtLast;
    private TextView _txtCompare;

    private TextView _lblReset;

    private Handler _handler;
    private Timer _timerCurrent = new Timer();
    private Timer _timerLast = new Timer();

    //========================================================================= INITIALIZE
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countup_timer);

        getWindow().getDecorView().findViewById(android.R.id.content).setKeepScreenOn(true);

        initializeViews();
        initializeEvents();

        _handler = new Handler();
        runnable.run();
    }
    private void initializeViews()
    {
        _viewStatus = findViewById(R.id.viewStatus);

        _txtMin = (TextView)findViewById(R.id.txtMin);
        _txtSec = (TextView)findViewById(R.id.txtSec);
        _txtMS = (TextView)findViewById(R.id.txtMS);

        _txtLast = (TextView)findViewById(R.id.txtLast);
        _txtCompare = (TextView)findViewById(R.id.txtCompare);

        _lblReset = (TextView)findViewById(R.id.lblReset);
    }
    private void initializeEvents()
    {
        View layMain = findViewById(R.id.layMain);

        layMain.setOnClickListener(layMain_onClick);
        layMain.setOnLongClickListener(layMain_onLongClick);
    }

    //========================================================================= FUNCTIONS
    private void toggle()
    {
        _timerCurrent.toggle();
        _viewStatus.setVisibility(View.VISIBLE);

        if (_timerCurrent.isPaused())
        {
            _handler.removeCallbacks(runnable);
            _viewStatus.setBackgroundResource(R.drawable.shape_pause);
            _lblReset.setVisibility(View.VISIBLE);
        }
        else
        {
            _handler.postDelayed(runnable, REFRESH_RATE);
            _viewStatus.setBackgroundResource(R.drawable.shape_run);
            _lblReset.setVisibility(View.INVISIBLE);
        }

        updateUI();
    }
    private void reset()
    {
        // save last time
        _timerLast = new Timer(_timerCurrent.runTime());

        _txtLast.setText(_timerLast.time());
        _txtLast.setVisibility(View.VISIBLE);

        _timerCurrent.reset();

        _handler.removeCallbacks(runnable);
        _viewStatus.setVisibility(View.INVISIBLE);
        _lblReset.setVisibility(View.INVISIBLE);

        updateUI();
    }

    private void updateUI()
    {
        if (_timerCurrent.runTime() == 0 || _timerLast.runTime() == 0)
            _txtCompare.setVisibility(View.INVISIBLE);
        else
        {
            Timer compare = _timerCurrent.subtract(_timerLast);
            String prefix;
            if (compare.runTime() > 0)
            {
                prefix = "+";
                _txtCompare.setTextColor(getResources().getColor(R.color.pause_fill));
            }
            else
            {
                prefix = "-";
                _txtCompare.setTextColor(getResources().getColor(R.color.run_fill));
            }
            _txtCompare.setText(prefix.concat(compare.time()));
            _txtCompare.setVisibility(View.VISIBLE);
        }

        _txtMin.setText(_timerCurrent.minutes());
        _txtSec.setText(_timerCurrent.seconds());
        _txtMS.setText(_timerCurrent.milliseconds());
    }

    //========================================================================= EVENTS
    protected final void onPause()
    {
        super.onPause();
        if (!_timerCurrent.isPaused())
            toggle();
    }

    private final Runnable runnable = new Runnable()
    {
        public final void run()
        {
            updateUI();
            _handler.postDelayed(this, REFRESH_RATE); // loop
        }
    };

    private final View.OnClickListener layMain_onClick = new View.OnClickListener()
    {
        public final void onClick(View view)
        {
            toggle();
        }
    };
    private final View.OnLongClickListener layMain_onLongClick = new View.OnLongClickListener()
    {
        public final boolean onLongClick(View view)
        {
            if (_timerCurrent.isPaused())
            {
                reset();
                return true;
            }
            return false;
        }
    };
}
