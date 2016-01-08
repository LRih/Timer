package ric.ov.Timer.Timers;

public final class CountdownTimer extends Timer
{
    //========================================================================= VARIABLES
    private long _startMS;
    private boolean _waitingForAlarm = false;

    //========================================================================= INITIALIZE
    public CountdownTimer(long startMS)
    {
        super();
        _startMS = startMS;
    }

    //========================================================================= FUNCTIONS
    public final void start()
    {
        super.start();
        _waitingForAlarm = true;
    }
    public final void turnOffAlarm()
    {
        _waitingForAlarm = false;
    }

    //========================================================================= PROPERTIES
    protected final long displayTime()
    {
        return _startMS - Math.min(super.runTime(), _startMS);
    }
    public final int startSecond()
    {
        return (int)(_startMS / 1000);
    }

    public final boolean isAlarmTime()
    {
        return (_waitingForAlarm && displayTime() == 0);
    }
}
