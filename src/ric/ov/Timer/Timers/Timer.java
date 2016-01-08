package ric.ov.Timer.Timers;

public class Timer
{
    //========================================================================= CONSTANTS
    private static final int MAX_TIME = 5999999;

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
    public void start()
    {
        _startTime = System.currentTimeMillis();
        _isPaused = false;
    }
    public final void pause()
    {
        _countedTime = runTime();
        _isPaused = true;
    }
    public final void toggle()
    {
        if (_isPaused) start();
        else pause();
    }
    public final void reset()
    {
        _countedTime = 0;
        _isPaused = true;
    }

    public final Timer subtract(Timer timer)
    {
        return new Timer((runTime() / 10 - timer.runTime() / 10) * 10);
    }

    //========================================================================= PROPERTIES
    public final String time()
    {
        long runTime = Math.abs(displayTime()) / 10;
        int minutes = (int)(runTime / 60 / 100);
        int seconds = (int)(runTime / 100) % 60;
        int ms = (int)(runTime % 100);
        return String.format("%02d:%02d.%02d", minutes, seconds, ms);
    }
    public final String minutes()
    {
        long runTime = Math.abs(displayTime()) / 10;
        return String.format("%02d", runTime / 60 / 100);
    }
    public final String seconds()
    {
        long runTime = Math.abs(displayTime()) / 10;
        return String.format("%02d", (runTime / 100) % 60);
    }
    public final String milliseconds()
    {
        long runTime = Math.abs(displayTime()) / 10;
        return String.format("%02d", runTime % 100);
    }

    public final long runTime()
    {
        long runTime = _countedTime;

        if (!_isPaused)
            runTime += System.currentTimeMillis() - _startTime;

        return Math.min(runTime, MAX_TIME);
    }
    protected long displayTime()
    {
        return runTime();
    }

    public final boolean isPaused()
    {
        return _isPaused;
    }
}
