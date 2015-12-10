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
    Random random_generator_ = new Random();
    // Implement this interface to receive information about changes to the surface.
    Score score_ = new Score();

    private gameThread my_thread_ = new gameThread(); // Thread to control the rendering

    //private Double world_width_, world_height_;
    // 1a) Variables used for background rendering
    private Bitmap background_, scaled_background_, BM_rip;
    // 1b) Define Screen width and Screen height as integer
    private int screen_width_, screen_height_;
    // 1c) Variables for defining background start and end point
    private short bgX = 0, bgY = 0;
    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap man_, block_;
    private Bitmap monies_;
    // 4b) Variable as an index to keep track of the spaceship images

    // frame_information
    private int frame_count_ = 0;
    private long current_time_ = 0;
    private long last_frame_time_ = 0;
    private long last_fps_update_ = 0;
    private double fps_ = 0.0;
    private double delta_time_ = 0.0;

    private double countdown_to_next_spawn_ = 0;

    // Variable for Game State check
    private GAMESTATE game_state_ = GAMESTATE.PLAY;

    private Paint paint_ = new Paint();

    private short touch_position_x_ = 0, touch_position_y_ = 0, drag_delta_x_, drag_delta_y_;

    private GameObject main_character_ = new GameObject();
    private GameObjectMoney money1_ = new GameObjectMoney();
    private GameObjectMoney money2_ = new GameObjectMoney();
    private GameObjectMoney money3_ = new GameObjectMoney();
    private GameObjectMoney money4_ = new GameObjectMoney();
    private GameObjectMoney money5_ = new GameObjectMoney();

    Vector<GameObject> game_object_list_ = new Vector<>();

    //constructor for this GamePanelSurfaceView class
    public gamePanelSurfaceView (Context context)
    {
        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screen_width_ = metrics.widthPixels;
        screen_height_ = metrics.heightPixels;
        // 1e)load the image when this class is being instantiated
        background_ = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        scaled_background_ = Bitmap.createScaledBitmap(background_, screen_width_, screen_height_, true);

        BM_rip = BitmapFactory.decodeResource(getResources(), R.drawable.rip);
        BM_rip = Bitmap.createScaledBitmap(BM_rip, screen_width_, screen_height_, true);

        // 4c) Load the images of the spaceships
        man_ = BitmapFactory.decodeResource(getResources(), R.drawable.man);
        man_ = Bitmap.createScaledBitmap(man_, (int) (screen_width_ / 5.5), (int) ((screen_width_ / 5.5) / 2), true);

        monies_ = BitmapFactory.decodeResource(getResources(), R.drawable.monies);
        monies_ = Bitmap.createScaledBitmap(monies_, (int) (screen_width_ / 5.5), (int) ((screen_width_ / 5.5) / 2), true);

        block_ = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine);
        block_ = Bitmap.createScaledBitmap(block_, (int)(screen_width_/7), (int)((screen_width_/7)*1.6), true);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void UpdateFrameInformation(final long current_time)
    {
        frame_count_++;

        current_time_ = current_time;
        delta_time_ = (current_time_ - last_frame_time_) / 1000.f;

        if(current_time_ - last_fps_update_ > 1000)
        {
            fps_ = (frame_count_ * 1000.f) / (current_time_ - last_fps_update_);
            last_fps_update_ = current_time_;
            frame_count_ = 0;
        }
        last_frame_time_ = current_time_;
    }

    public void Init()
    {
        score_.Reset();

        game_state_ = GAMESTATE.PLAY;
        game_object_list_.clear();

        main_character_.collision_box_size_.x_ = (int)(man_.getWidth()*0.3);
        main_character_.collision_box_size_.y_ = (int)(man_.getHeight()*0.45);
        main_character_.position_.x_ = 200.0;
        main_character_.position_.y_ = screen_height_/2;
        main_character_.velocity_.y_ = 0.0;

        money1_.position_.x_ = 200.0;
        money1_.position_.y_ = screen_height_/2;
        money1_.Create();
        money1_.Set(main_character_, 0.0, 120.0, random_generator_.nextDouble() * 20.0f + 120.0f);
        money2_.position_.x_ = 200;
        money2_.position_.y_ = screen_height_/2;
        money2_.Create();
        money2_.Set(main_character_, 0.0, 120.0, random_generator_.nextDouble() * 20.0 + 120.0);
        money3_.position_.x_ = 200;
        money3_.position_.y_ = screen_height_/2;
        money3_.Create();
        money3_.Set(main_character_, 0.0, 120.0, random_generator_.nextDouble() * 20.0 + 120.0);
        money4_.position_.x_ = 200;
        money4_.position_.y_ = screen_height_/2;
        money4_.Create();
        money4_.Set(main_character_, 0.0, 120.0, random_generator_.nextDouble() * 20.0 + 120.0);
        money5_.position_.x_ = 200;
        money5_.position_.y_ = screen_height_/2;
        money5_.Create();
        money5_.Set(main_character_, 0.0, 120.0, random_generator_.nextDouble() * 20.0 + 120.0);
    }

    public void GenerateBlocks()
    {
        countdown_to_next_spawn_ = random_generator_.nextInt(7) + 3;
        Double gap = block_.getWidth()*1.2;

        int temp = random_generator_.nextInt(block_.getHeight()/3);

        GameObjectBlock GO = new GameObjectBlock();
        GO.Set(main_character_, score_);
        GO.position_.x_ = screen_width_ + block_.getWidth();
        GO.position_.y_ = screen_height_ - temp;
        GO.velocity_.x_ = -120.f;
        GO.velocity_.y_ = 0;
        GO.collision_box_size_.x_ = block_.getWidth()/2;
        GO.collision_box_size_.y_ = block_.getHeight()/2;
        game_object_list_.add(GO);

        GameObject GO1 = new GameObject();
        GO1.position_.x_ = screen_width_ + block_.getWidth();
        GO1.position_.y_ = screen_height_ - temp - gap - block_.getHeight();
        GO1.velocity_.x_ = -120.f;
        GO1.velocity_.y_ = 0;
        GO1.collision_box_size_.x_ = block_.getWidth()/2;
        GO1.collision_box_size_.y_ = block_.getHeight()/2;
        game_object_list_.add(GO1);
    }

    //Update method to update the game play
    public void Update(final long current_time)
    {
        UpdateFrameInformation(current_time);

        if(delta_time_ > 0.5)
        {
            delta_time_ = 0.0;
        }

        switch (game_state_)
        {
            case PLAY:
            {
                countdown_to_next_spawn_ -= delta_time_;

                bgX -= 100 * delta_time_;
                if(bgX < -screen_width_)
                {
                    bgX = 0;
                }

                if(countdown_to_next_spawn_ < 0)
                {
                    GenerateBlocks();
                }

                main_character_.velocity_.y_ += 275 * delta_time_;
                main_character_.Update(delta_time_);

                money1_.Update(delta_time_);
                money2_.Update(delta_time_);
                money3_.Update(delta_time_);
                money4_.Update(delta_time_);
                money5_.Update(delta_time_);
                if(main_character_.position_.y_ > screen_height_)
                {
                    game_state_ = GAMESTATE.DEATH;
                }

                for (GameObject object : game_object_list_)
                {
                    object.Update(delta_time_);

                    if(GameObject.checkCollision(object, main_character_))
                    {
                        main_character_.position_.y_ = screen_height_/2;
                        main_character_.velocity_.y_ = 0;
                        game_state_ = GAMESTATE.DEATH;
                    }

                    if(object.position_.x_ < 0 - block_.getWidth())
                    {
                        object.Destroy();
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
    public void Render(Canvas canvas)
    {
        if(canvas == null)
        {
            return;
        }

        switch (game_state_)
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

                canvas.drawText("Gameover, you lost your money", screen_width_/2 - 300, screen_height_/2 - 50, paint);
                canvas.drawText("Score: " + score_.Num(), screen_width_/2 - 300, screen_height_/2, paint);
                break;
        }
    }

    public void RenderGameplay(Canvas canvas)
    {
        canvas.drawBitmap(scaled_background_, bgX, bgY, null);
        canvas.drawBitmap(scaled_background_, bgX + screen_width_, bgY, null);
        // 4d) Draw the spaceships
        canvas.drawBitmap(monies_, (int)(money1_.position_.x_ - monies_.getWidth()*0.5), (int)(money1_.position_.y_ - monies_.getHeight()*0.5), null);
        canvas.drawBitmap(monies_, (int)(money2_.position_.x_ - monies_.getWidth()*0.5), (int)(money2_.position_.y_ - monies_.getHeight()*0.5), null);
        canvas.drawBitmap(monies_, (int)(money3_.position_.x_ - monies_.getWidth()*0.5), (int)(money3_.position_.y_ - monies_.getHeight()*0.5), null);
        canvas.drawBitmap(monies_, (int)(money4_.position_.x_ - monies_.getWidth()*0.5), (int)(money4_.position_.y_ - monies_.getHeight()*0.5), null);
        canvas.drawBitmap(monies_, (int)(money5_.position_.x_ - monies_.getWidth()*0.5), (int)(money5_.position_.y_ - monies_.getHeight()*0.5), null);

        canvas.drawBitmap(man_, (int)(main_character_.position_.x_ - man_.getWidth()*0.5), (int)(main_character_.position_.y_ - man_.getHeight()*0.5), null);

        for (GameObject object : game_object_list_)
        {
            canvas.drawBitmap(block_, (int)(object.position_.x_ - block_.getWidth()*0.5), (int)(object.position_.y_ - block_.getHeight()*0.5), null);
        }

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        paint.setStrokeWidth(100);
        paint.setTextSize(25);
        canvas.drawText("FPS: " + fps_, 130, 60, paint);
        canvas.drawText("Score: " + score_.Num(), 130, 80, paint);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Create the thread
        if (!my_thread_.isAlive())
        {
            my_thread_.Init(getHolder(), this);
            my_thread_.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Destroy the thread
        if (my_thread_.isAlive())
        {
            my_thread_.Exit();
        }
        boolean retry = true;
        while (retry)
        {
            try
            {
                my_thread_.join();
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //short X = (short) event.getX();
        //short Y = (short) event.getY();
        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            switch (game_state_)
            {
                case PLAY:
                {
                    main_character_.velocity_.y_ = -200;
                }
                break;
                case DEATH:
                {
                    Init();
                }
                break;
            }

        }

        return super.onTouchEvent(event);
    }
}

