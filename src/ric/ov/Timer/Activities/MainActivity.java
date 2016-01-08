package ric.ov.Timer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ric.ov.Timer.R;

//#============================================================================
//# * Changes (1.1b4 - 1.1.1b5)
//#============================================================================

public final class MainActivity extends Activity
{
    //========================================================================= INITIALIZE
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //========================================================================= FUNCTIONS
    private void showAboutDialog()
    {
        AboutDialog.show(this, R.drawable.icon, getString(R.string.app_name), "", 2013, null);
    }

    //========================================================================= EVENTS
    public final boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public final boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuAbout:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public final void onCountupTimerClick(View view)
    {
        Intent intent = new Intent(this, CountupTimerActivity.class);
        startActivity(intent);
    }
    public final void onCountdownTimerClick(View view)
    {
        Intent intent = new Intent(this, CountdownTimerActivity.class);
        startActivity(intent);
    }
}
