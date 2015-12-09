package auranapse.projectmobility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 143476L on 11/19/2015.
 */
public class splash extends Activity
{
    protected boolean _active = true;
    protected int _splashTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_splash);

        Thread splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    while(_active && (waited < _splashTime))
                    {
                        sleep(200);
                        if(_active)
                        {
                            waited += 200;
                        }
                    }
                }
                catch(InterruptedException e)
                {
                    //do nothing
                }
                finally
                {
                    finish();
                    Intent intent = new Intent(splash.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        };

        splashTread.start();
    }

    public boolean OnTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            _active = false;
        }

        return true;
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}
