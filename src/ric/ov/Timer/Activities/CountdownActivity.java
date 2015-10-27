package ric.ov.Timer.Activities;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import ric.ov.Timer.CountdownTimer;
import ric.ov.Timer.R;
import ric.ov.Timer.SecondSelectDialog;

public final class CountdownActivity extends BaseActivity
{
    //========================================================================= VARIABLES
    private LinearLayout _layMain;
    private View _viewStatus;
    private TextView _txtMin;
    private TextView _txtSec;
    private TextView _txtMS;
    private TextView _lblReset;

    private MediaPlayer _player = new MediaPlayer();
    private Handler _handlerUpdate;
    private CountdownTimer _timer;

    //========================================================================= INITIALIZE
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getWindow().getDecorView().findViewById(android.R.id.content).setKeepScreenOn(true);
        InitializeViews();
        InitializeEvents();
        _timer = new CountdownTimer(GetCountdown() * 1000);
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
        _timer.Toggle();
        _viewStatus.setVisibility(View.VISIBLE);
        if (_timer.IsPaused())
        {
            _player.release();
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
        _timer.Reset();
        _viewStatus.setVisibility(View.INVISIBLE);
        _lblReset.setVisibility(View.INVISIBLE);
    }
    private void UpdateUI()
    {
        _txtMin.setText(_timer.GetMin());
        _txtSec.setText(_timer.GetSec());
        _txtMS.setText(_timer.GetMS());
    }
    private void UpdateAlarm()
    {
        if (_timer.IsAlarmTime())
        {
            _timer.TurnOffAlarm();
            _player.release();
            _player = new MediaPlayer();
            _player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try
            {
                AssetFileDescriptor desc = getAssets().openFd("beep.mp3");
                _player.setDataSource(desc.getFileDescriptor(), desc.getStartOffset(), desc.getLength());
                desc.close();
                _player.prepare();
                _player.start();
            }
            catch (Exception ex) {}
        }
    }

    private void ShowTimeSelectDialog()
    {
        final SecondSelectDialog dialog = new SecondSelectDialog(this, _timer.StartSecond());
        dialog.SetButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialogInterface, int i)
            {
                SaveCountdown(dialog.Value());
                _timer = new CountdownTimer(dialog.Value() * 1000);
                _player.stop();
                Reset();
            }
        });
        dialog.SetButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialogInterface, int i) { }
        });
        dialog.Show();
    }

    //==================================================================== EVENTS
    public final boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, getString(R.string.set_countdown));
        return super.onCreateOptionsMenu(menu);
    }
    public final boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 0:
                ShowTimeSelectDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected final void onPause()
    {
        super.onPause();
        if (!_timer.IsPaused()) Toggle();
        _player.release();
    }

    private final Runnable handlerUpdate_run = new Runnable()
    {
        public final void run()
        {
            UpdateUI();
            UpdateAlarm();
            _handlerUpdate.postDelayed(this, 10); // loop ui update
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
            if (_timer.IsPaused())
            {
                Reset();
                return true;
            }
            else return false;
        }
    };
}
