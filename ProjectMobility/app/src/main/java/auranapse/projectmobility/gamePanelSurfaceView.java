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

import java.util.Random;
import java.util.Vector;

/**
 * Created by 143476L on 11/25/2015.
 */
public class gamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    enum GAMESTATE
    {
        PLAY,
        DEATH,
    }
    Random randomGenerator = new Random();
    // Implement this interface to receive information about changes to the surface.
    Score score_ = new Score();

    private gameThread myThread = null; // Thread to control the rendering

    //private float world_width_, world_height_;
    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg, BM_rip;
    // 1b) Define Screen width and Screen height as integer
    private int ScreenWidth, ScreenHeight;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap BM_man, BM_block;
    // 4b) Variable as an index to keep track of the spaceship images

    // Variables for FPS
    private float FPS;
    private float deltaTime;
    private long dt;

    private float countdown_to_next_spawn = 0;

    // Variable for Game State check
    private GAMESTATE GameState = GAMESTATE.PLAY;

    private Paint paint = new Paint();

    private short TOUCHPOS_X = 0, TOUCHPOS_Y = 0, DRAGDELTA_X, DRAGDELTA_Y;

    private GameObject MAN_CHAR = new GameObject();

    Vector<GameObject> GO_list = new Vector<GameObject>();

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

        BM_rip = BitmapFactory.decodeResource(getResources(), R.drawable.rip);
        BM_rip = Bitmap.createScaledBitmap(BM_rip, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        BM_man = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        BM_man = Bitmap.createScaledBitmap(BM_man, (int)(ScreenWidth/5.5), (int)((ScreenWidth/5.5)/2), true);

        BM_block = BitmapFactory.decodeResource(getResources(), R.drawable.button);
        BM_block = Bitmap.createScaledBitmap(BM_block, (int)(ScreenWidth/19), (int)(ScreenWidth/19)*8, true);

        // Create the game loop thread
        myThread = new gameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void init()
    {
        score_.Reset();

        GameState = GAMESTATE.PLAY;
        GO_list.clear();

        MAN_CHAR.COL_X = (int)(BM_man.getWidth()*0.3);
        MAN_CHAR.COL_Y = (int)(BM_man.getHeight()*0.45);
        MAN_CHAR.Pos_X = 200;
        MAN_CHAR.Pos_Y = ScreenHeight/2;
        MAN_CHAR.Vel_Y = 0;
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

    public void generateBlocks()
    {
        countdown_to_next_spawn = randomGenerator.nextInt(7) + 3;
        float gap = BM_block.getWidth()*2.6f;

        int temp = randomGenerator.nextInt(BM_block.getHeight()/3);


        GameObjectBlock GO = new GameObjectBlock();
        GO.Set(MAN_CHAR, score_);
        GO.Pos_X = ScreenWidth + BM_block.getWidth();
        GO.Pos_Y = ScreenHeight - temp;
        GO.Vel_X = -120.f;
        GO.Vel_Y = 0;
        GO.COL_X = BM_block.getWidth()/2;
        GO.COL_Y = BM_block.getHeight()/2;
        GO_list.add(GO);

        GameObject GO1 = new GameObject();
        GO1.Pos_X = ScreenWidth + BM_block.getWidth();
        GO1.Pos_Y = ScreenHeight - temp - gap - BM_block.getHeight();
        GO1.Vel_X = -120.f;
        GO1.Vel_Y = 0;
        GO1.COL_X = BM_block.getWidth()/2;
        GO1.COL_Y = BM_block.getHeight()/2;
        GO_list.add(GO1);
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

        for (int i = 0; i < GO_list.size(); ++i)
        {
                canvas.drawBitmap(BM_block, GO_list.get(i).Pos_X - (int)(BM_block.getWidth()*0.5), GO_list.get(i).Pos_Y - (int)(BM_block.getHeight()*0.5), null);
        }

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        paint.setStrokeWidth(100);
        paint.setTextSize(25);
        canvas.drawText("FPS: " + FPS, 130, 60, paint);
        canvas.drawText("Score: " + score_.Num(), 130, 80, paint);
    }

    //Update method to update the game play
    public void update(float dt, float fps)
    {
        if(dt > 0.5)
        {
            dt = 0.f;
        }
        FPS = fps;

        switch (GameState)
        {
            case PLAY:
            {
                countdown_to_next_spawn -= dt;

                bgX -= 100 * dt;
                if(bgX < -ScreenWidth)
                {
                    bgX = 0;
                }

                if(countdown_to_next_spawn < 0)
                {
                    generateBlocks();
                }

                MAN_CHAR.Vel_Y += 275 * dt;

                MAN_CHAR.Pos_X += MAN_CHAR.Vel_X * dt;
                MAN_CHAR.Pos_Y += MAN_CHAR.Vel_Y * dt;

                MAN_CHAR.UpdateBox();

                if(MAN_CHAR.Pos_Y > ScreenHeight)
                {
                    GameState = GAMESTATE.DEATH;
                }

                for (int i = 0; i < GO_list.size(); ++i)
                {
                    GameObject obj = GO_list.get(i);

                    obj.Update(dt);

                    if(GameObject.checkCollision(obj, MAN_CHAR))
                    {
                        MAN_CHAR.Pos_Y = ScreenHeight/2;
                        MAN_CHAR.Vel_Y = 0;
                        GameState = GAMESTATE.DEATH;
                    }

                    if(obj.Pos_X < 0 - BM_block.getWidth())
                    {
                        GO_list.remove(i);
                        --i;
                    }
                }
            }
            break;
            case DEATH:
            {

            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        switch (GameState)
        {
            case PLAY:
                RenderGameplay(canvas);
                break;
            case DEATH:
                canvas.drawBitmap(BM_rip, 0, 0, null);

                Paint paint = new Paint();
                paint.setARGB(255, 255, 255, 255);
                paint.setStrokeWidth(100);
                paint.setTextSize(40);
                canvas.drawText("Score: " + score_.Num(), ScreenWidth/2, ScreenHeight/2, paint);
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
            switch (GameState)
            {
                case PLAY:
                {
                    MAN_CHAR.Vel_Y = -200;
                }
                break;
                case DEATH:
                {
                    init();
                }
                break;
            }

        }

        return super.onTouchEvent(event);
    }
}

