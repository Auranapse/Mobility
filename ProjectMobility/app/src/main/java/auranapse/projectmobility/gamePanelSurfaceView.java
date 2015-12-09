package auranapse.projectmobility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Vector;

/**
 * Created by 143476L on 11/25/2015.
 */
public class gamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    // Implement this interface to receive information about changes to the surface.
    float score = 0.f;

    private gameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;
    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap BM_man, BM_block;
    // 4b) Variable as an index to keep track of the spaceship images

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // Variable for Game State check
    private short GameState = 0;

    Paint paint = new Paint();

    private short TOUCHPOS_X = 0, TOUCHPOS_Y = 0, DRAGDELTA_X, DRAGDELTA_Y;

    GameObject MAN_CHAR = new GameObject();
    Vector<GameObject> GO_list;

    //constructor for this GamePanelSurfaceView class
    public gamePanelSurfaceView (Context context)
    {
        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);

        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        BM_man = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        BM_man = Bitmap.createScaledBitmap(BM_man, 400, 200, true);

        BM_block = BitmapFactory.decodeResource(getResources(), R.drawable.button);
        BM_block = Bitmap.createScaledBitmap(BM_block, 100, 800, true);

        score = 0.f;

        MAN_CHAR.COL_X = 400;
        MAN_CHAR.COL_Y = 100;

        // Create the game loop thread
        myThread = new gameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Create the thread
        if (!myThread.isAlive())
        {
            myThread = new gameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry)
        {
            try
            {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    public void RenderGameplay(Canvas canvas)
    {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null)
        {
            return;
        }

        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);
        // 4d) Draw the spaceships
        canvas.drawBitmap(BM_man, MAN_CHAR.Pos_X - (int)(BM_man.getWidth()*0.5), MAN_CHAR.Pos_Y - (int)(BM_man.getHeight()*0.5), null);

        for (int i = 0; i < 10; ++i)
        {
                canvas.drawBitmap(BM_block, 20 - (int)(BM_block.getWidth()*0.5), 20 - (int)(BM_block.getHeight()*0.5), null);
        }
        //


        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("FPS: " + FPS, 130, 75, paint);
    }


    //Update method to update the game play
    public void update(float dt, float fps)
    {
        score += dt;

        if(dt > 0.5)
        {
            dt = 0.f;
        }
        FPS = fps;

        switch (GameState)
        {
            case 0:
            {
                bgX -= 400 * dt;
                if(bgX < -ScreenWidth)
                {
                    bgX = 0;
                }
                // 3) Update the background to allow panning effect

                MAN_CHAR.Vel_Y += 250 * dt;

                MAN_CHAR.Pos_X += MAN_CHAR.Vel_X * dt;
                MAN_CHAR.Pos_Y += MAN_CHAR.Vel_Y * dt;
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        short X = (short) event.getX();
        short Y = (short) event.getY();
        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //CHAR_POS_X = X;
            //CHAR_POS_Y = Y;
            MAN_CHAR.Vel_Y = -200;
        }

        return super.onTouchEvent(event);
    }
}

