package ric.ov.Timer;

//#============================================================================
//# * CountdownTimer
//#============================================================================
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
    public final void Start()
    {
        super.Start();
        _waitingForAlarm = true;
    }
    public final void TurnOffAlarm()
    {
        _waitingForAlarm = false;
    }

    //========================================================================= PROPERTIES
    protected final long GetDisplayTime()
    {
        return _startMS - Math.min(super.GetRunTime(), _startMS);
    }
    public final int StartSecond()
    {
        return (int)(_startMS / 1000);
    }
    public final boolean IsAlarmTime()
    {
        return (_waitingForAlarm && GetDisplayTime() == 0);
    }
}
