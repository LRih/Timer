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
import android.widget.TextView;
import ric.ov.Timer.Timers.CountdownTimer;
import ric.ov.Timer.R;
import ric.ov.Timer.TimeSelectDialog;

public final class CountdownTimerActivity extends BaseActivity
{
    //========================================================================= VARIABLES
    private View _viewStatus;

    private TextView _txtMin;
    private TextView _txtSec;
    private TextView _txtMS;

    private TextView _lblReset;

    private MediaPlayer _player = new MediaPlayer();
    private Handler _handler;
    private CountdownTimer _timer;

    //========================================================================= INITIALIZE
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getWindow().getDecorView().findViewById(android.R.id.content).setKeepScreenOn(true);

        initializeViews();
        initializeEvents();

        _player = MediaPlayer.create(this, R.raw.beep);

        _timer = new CountdownTimer(loadCountdown() * 1000);

        _handler = new Handler();
        runnable.run();
    }
    private void initializeViews()
    {
        _viewStatus = findViewById(R.id.viewStatus);

        _txtMin = (TextView)findViewById(R.id.txtMin);
        _txtSec = (TextView)findViewById(R.id.txtSec);
        _txtMS = (TextView)findViewById(R.id.txtMS);

        _lblReset = (TextView)findViewById(R.id.lblReset);
    }
    private void initializeEvents()
    {
        View layMain = findViewById(R.id.layMain);

        layMain.setOnClickListener(layMain_onClick);
        layMain.setOnLongClickListener(layMain_onLongClick);
    }

    //========================================================================= TERMINATE
    protected final void onDestroy()
    {
        super.onDestroy();
        _player.release();
    }

    //========================================================================= FUNCTIONS
    private void toggle()
    {
        _timer.toggle();
        _viewStatus.setVisibility(View.VISIBLE);

        if (_timer.isPaused())
        {
            if (_player.isPlaying())
                _player.pause();

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
        _timer.reset();

        if (_player.isPlaying())
            _player.pause();

        _handler.removeCallbacks(runnable);
        _viewStatus.setVisibility(View.INVISIBLE);
        _lblReset.setVisibility(View.INVISIBLE);

        updateUI();
    }

    private void updateUI()
    {
        _txtMin.setText(_timer.minutes());
        _txtSec.setText(_timer.seconds());
        _txtMS.setText(_timer.milliseconds());
    }
    private void updateAlarm()
    {
        if (_timer.isAlarmTime())
        {
            _timer.turnOffAlarm();
            _player.seekTo(0);
            _player.start();
        }
    }

    private void showTimeSelectDialog()
    {
        final TimeSelectDialog dialog = new TimeSelectDialog(this, _timer.startSecond());

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialogInterface, int i)
            {
                saveCountdown(dialog.seconds());
                _timer = new CountdownTimer(dialog.seconds() * 1000);
                reset();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), null);

        dialog.show();
    }

    //==================================================================== EVENTS
    public final boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.countdown_timer, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public final boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuSetCountdown:
                showTimeSelectDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected final void onPause()
    {
        super.onPause();

        if (!_timer.isPaused())
            toggle();
    }

    private final Runnable runnable = new Runnable()
    {
        public final void run()
        {
            updateUI();
            updateAlarm();
            _handler.postDelayed(this, REFRESH_RATE); // loop ui update
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
            if (_timer.isPaused())
            {
                reset();
                return true;
            }
            return false;
        }
    };
}
