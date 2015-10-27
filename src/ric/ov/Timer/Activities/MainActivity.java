package ric.ov.Timer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import ric.ov.OneVisual.Controls.AboutDialog;
import ric.ov.Timer.R;
import ric.ov.Timer.Timer;

//#============================================================================
//# * Changes (1.0.2b3 -> 1.0.3b4)
//#============================================================================
// added up/down buttons for minute step on countdown screen
// added rate button to about dialog

public final class MainActivity extends BaseActivity
{
    //========================================================================= VARIABLES
    private LinearLayout _layMain;
    private View _viewStatus;
    private TextView _txtMin;
    private TextView _txtSec;
    private TextView _txtMS;
    private TextView _txtLast;
    private TextView _txtCompare;
    private TextView _lblReset;

    private Handler _handlerUpdate;
    private Timer _timerCurrent = new Timer();
    private Timer _timerLast = new Timer();

    //========================================================================= INITIALIZE
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().findViewById(android.R.id.content).setKeepScreenOn(true);
        InitializeViews();
        InitializeEvents();
        _handlerUpdate = new Handler();
        handlerUpdate_run.run();
    }
    private void InitializeViews()
    {
        _layMain = (LinearLayout)findViewById(R.id.layMain);
        _viewStatus = (View)findViewById(R.id.viewStatus);
        _txtMin = (TextView)findViewById(R.id.txtMin);
        _txtSec = (TextView)findViewById(R.id.txtSec);
        _txtMS = (TextView)findViewById(R.id.txtMS);
        _txtLast = (TextView)findViewById(R.id.txtLast);
        _txtCompare = (TextView)findViewById(R.id.txtCompare);
        _lblReset = (TextView)findViewById(R.id.lblReset);
    }
    private void InitializeEvents()
    {
        _layMain.setOnClickListener(layMain_onClick);
        _layMain.setOnLongClickListener(layMain_onLongClick);
    }

    //========================================================================= FUNCTIONS
    private void Toggle()
    {
        _timerCurrent.Toggle();
        _viewStatus.setVisibility(View.VISIBLE);
        if (_timerCurrent.IsPaused())
        {
            _viewStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_pause));
            _lblReset.setVisibility(View.VISIBLE);
        }
        else
        {
            _viewStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_run));
            _lblReset.setVisibility(View.INVISIBLE);
        }
    }
    private void Reset()
    {
        // save last time
        _timerLast = new Timer(_timerCurrent.GetRunTime());
        _txtLast.setText(_timerLast.GetTime());
        _txtLast.setVisibility(View.VISIBLE);
        _timerCurrent.Reset();
        _viewStatus.setVisibility(View.INVISIBLE);
        _lblReset.setVisibility(View.INVISIBLE);
    }
    private void UpdateUI()
    {
        if (_timerCurrent.GetRunTime() == 0 || _timerLast.GetRunTime() == 0) _txtCompare.setVisibility(View.INVISIBLE);
        else
        {
            Timer compare = _timerCurrent.Subtract(_timerLast);
            String prefix;
            if (compare.GetRunTime() > 0)
            {
                prefix = "+";
                _txtCompare.setTextColor(getResources().getColor(R.color.pause_fill));
            }
            else
            {
                prefix = "-";
                _txtCompare.setTextColor(getResources().getColor(R.color.run_fill));
            }
            _txtCompare.setText(prefix.concat(compare.GetTime()));
            _txtCompare.setVisibility(View.VISIBLE);
        }
        _txtMin.setText(_timerCurrent.GetMin());
        _txtSec.setText(_timerCurrent.GetSec());
        _txtMS.setText(_timerCurrent.GetMS());
    }

    private void ShowAboutDialog()
    {
        AboutDialog.Show(this, R.drawable.icon, getString(R.string.app_name), "", 2013, false, null);
    }

    //========================================================================= EVENTS
    public final boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.countdown_timer));
        menu.add(0, 1, 1, getString(R.string.about));
        return super.onCreateOptionsMenu(menu);
    }
    public final boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 0:
                startActivity(new Intent(this, CountdownActivity.class));
                return true;
            case 1:
                ShowAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected final void onPause()
    {
        super.onPause();
        if (!_timerCurrent.IsPaused()) Toggle();
    }

    private final Runnable handlerUpdate_run = new Runnable()
    {
        public final void run()
        {
            UpdateUI();
            _handlerUpdate.postDelayed(this, 10); // loop
        }
    };
    private final View.OnClickListener layMain_onClick = new View.OnClickListener()
    {
        public final void onClick(View view)
        {
            Toggle();
        }
    };
    private final View.OnLongClickListener layMain_onLongClick = new View.OnLongClickListener()
    {
        public final boolean onLongClick(View view)
        {
            if (_timerCurrent.IsPaused())
            {
                Reset();
                return true;
            }
            else return false;
        }
    };
}
