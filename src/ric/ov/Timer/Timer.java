package ric.ov.Timer;

public class Timer
{
    //========================================================================= CONSTANTS
    private static final int MAX_TIME = 5999999;

    //========================================================================= VARIABLES
    private boolean _isPaused = true;
    private long _countedTime;
    private long _startTime;

    //========================================================================= INITIALIZE
    public Timer()
    {
        this(0);
    }
    public Timer(long startMS)
    {
        _countedTime = startMS;
    }

    //========================================================================= FUNCTIONS
    public void Start()
    {
        _startTime = System.currentTimeMillis();
        _isPaused = false;
    }
    public final void Pause()
    {
        _countedTime = GetRunTime();
        _isPaused = true;
    }
    public final void Toggle()
    {
        if (_isPaused) Start();
        else Pause();
    }
    public final void Reset()
    {
        _countedTime = 0;
        _isPaused = true;
    }

    public final Timer Subtract(Timer timer)
    {
        return new Timer((GetRunTime() / 10 - timer.GetRunTime() / 10) * 10);
    }

    //========================================================================= PROPERTIES
    public final String GetTime()
    {
        long runTime = Math.abs(GetDisplayTime()) / 10;
        int minutes = (int)(runTime / 60 / 100);
        int seconds = (int)(runTime / 100) % 60;
        int ms = (int)(runTime % 100);
        return String.format("%02d:%02d.%02d", minutes, seconds, ms);
    }
    public final String GetMin()
    {
        long runTime = Math.abs(GetDisplayTime()) / 10;
        return String.format("%02d", runTime / 60 / 100);
    }
    public final String GetSec()
    {
        long runTime = Math.abs(GetDisplayTime()) / 10;
        return String.format("%02d", (runTime / 100) % 60);
    }
    public final String GetMS()
    {
        long runTime = Math.abs(GetDisplayTime()) / 10;
        return String.format("%02d", runTime % 100);
    }
    public final long GetRunTime()
    {
        long runTime;
        if (_isPaused) runTime = _countedTime;
        else runTime = _countedTime + System.currentTimeMillis() - _startTime;
        return Math.min(runTime, MAX_TIME);
    }
    protected long GetDisplayTime()
    {
        return GetRunTime();
    }

    public final boolean IsPaused()
    {
        return _isPaused;
    }
}
