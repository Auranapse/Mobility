package auranapse.projectmobility;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by 143476L on 11/25/2015.
 */
public class gameThread extends Thread
{
    // The actual view that handles inputs and draws to the surface
    private gamePanelSurfaceView my_view_ = null;

    // Surface holder that can access the physical surface
    private SurfaceHolder holder_ = null;

    // Flag to hold game state
    private boolean is_running_ = false;

    private boolean is_paused_ = false;

    public void Init(SurfaceHolder holder, gamePanelSurfaceView my_view)
    {
        is_running_ = true;
        my_view_ = my_view;
        holder_ = holder;
    }

    public void Exit()
    {
        is_running_ = false;
    }

    public void Pause()
    {
        synchronized (holder_)
        {
            is_paused_ = true;
        }
    }

    public void Unpause()
    {
        synchronized (holder_)
        {
            is_paused_ = false;
            holder_.notifyAll();
        }
    }

    //Return Pause
    public boolean IsPaused()
    {
        return is_paused_;
    }

    @Override
    public void run()
    {
        while (is_running_)
        {
            //Update game state and render state to the screen
            Canvas c = null;
            try
            {
                c = this.holder_.lockCanvas();
                synchronized(holder_)
                {
                    if (my_view_ != null)
                    {
                        if (!is_paused_)
                        {
                            my_view_.Update(System.currentTimeMillis());
                            my_view_.Render(c);
                        }
                    }
                }
                synchronized(holder_)
                {
                    while (is_paused_)
                    {
                        try
                        {
                            holder_.wait();
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
            }

            finally
            {
                if (c!=null)
                {
                    holder_.unlockCanvasAndPost(c);
                }
            }
        }

    }
}
