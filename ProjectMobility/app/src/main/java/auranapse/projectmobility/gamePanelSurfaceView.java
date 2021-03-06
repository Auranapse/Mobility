package auranapse.projectmobility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Random;
import java.util.Vector;

/**
 * Created by 143476L on 11/25/2015.
 */
public class gamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener
{
    private final SensorManager sensor;

    private float[] f_Accelerometer = {0, 0, 0};
    private long lastTime = System.currentTimeMillis();

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent)
    {
        f_Accelerometer = SenseEvent.values;
    }

    enum GAMESTATE
    {
        SPLASH,
        MENU,
        PLAY,
        PAUSED,
        DEATH,
        TOTAL
    }

    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;


    Random random_generator_ = new Random();
    // Implement this interface to receive information about changes to the surface.
    Score score_ = new Score();
    int i_highscore;


    private gameThread my_thread_ = new gameThread(); // Thread to control the rendering

    //private Double world_width_, world_height_;

    private int screen_width_, screen_height_;

    //Variables for defining background start and end point
    private short bgX = 0, bgY = 0;

    Bitmap bitmap_background_, bitmap_rip_, bitmap_main_character_, bitmap_block_, bitmap_monies_, bitmap_invulnerability_, bitmap_splash_, bitmap_menu_;

    // frame_information
    private int frame_count_ = 0;
    private long current_time_ = 0;
    private long last_frame_time_ = 0;
    private long last_fps_update_ = 0;
    private double fps_ = 0.0;
    private double delta_time_ = 0.0;
    private double speed_multiplier = 1.0;
    private int score_to_increase = 5;
    private float timer_ = 0.f;

    //spawner information
    private double countdown_to_next_spawn_ = 0.0;

    private final double invulnerability_ = 2.5;
    private double current_invulnerability_ = 0.0;

    // Variable for Game State check
    private GAMESTATE game_state_ = GAMESTATE.SPLASH;

    private Paint paint_ = new Paint();

    //private short touch_position_x_ = 0, touch_position_y_ = 0, drag_delta_x_, drag_delta_y_;

    private GameObject main_character_ = new GameObject();

    private GameObject button_Play_ = new GameObject();
    Bitmap bitmap_button_Play_;

    private GameObject button_Menu_ = new GameObject();
    Bitmap bitmap_button_Menu_;

    private GameObject button_Retry_ = new GameObject();
    Bitmap bitmap_button_Retry_;

    private GameObject button_Pause_ = new GameObject();
    Bitmap bitmap_button_Pause_;

    private GameObject clickymouse_ = new GameObject();


    final int num_monies_ = 5;
    int current_num_monies_ = 0;
    Vector<GameObject> game_object_list_ = new Vector<>();
    Vector<GameObjectMoney> monies_list_ = new Vector<>();

    //Audio
    MediaPlayer AUDIO_BACKGROUNDMUSIC;
    private SoundPool AUDIO_SOUNDS;
    private int AUDIO_FLY, AUDIO_CASINO;

    public void SensorMove()
    {
        float tempX, tempY;
        tempX = (f_Accelerometer[1] * ((System.currentTimeMillis() - lastTime)/1000));
        tempY = (f_Accelerometer[0] * ((System.currentTimeMillis() - lastTime)/1000));
    }

    //constructor for this GamePanelSurfaceView class
    public gamePanelSurfaceView (Context context)
    {
        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        sensor = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor.registerListener(this, sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screen_width_ = metrics.widthPixels;
        screen_height_ = metrics.heightPixels;
        // 1e)load the image when this class is being instantiated
        Bitmap bitmap;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene, options);
        bitmap_background_ = Bitmap.createScaledBitmap(bitmap, screen_width_, screen_height_, true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        bitmap_splash_ = Bitmap.createScaledBitmap(bitmap, screen_width_, screen_height_, true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background, options);
        bitmap_menu_ = Bitmap.createScaledBitmap(bitmap, screen_width_, screen_height_, true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rip, options);
        bitmap_rip_ = Bitmap.createScaledBitmap(bitmap, screen_width_, screen_height_, true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man, options);
        bitmap_main_character_ = Bitmap.createScaledBitmap(bitmap, (int) (screen_width_ / 5.5), (int) ((screen_width_ / 5.5) / 2), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.monies, options);
        bitmap_monies_ = Bitmap.createScaledBitmap(bitmap, (int) (screen_width_ / 5.5), (int) ((screen_width_ / 5.5) / 2), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slot_machine, options);
        bitmap_block_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/7.0), (int)((screen_width_/7)*1.6), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.invulnerability, options);
        bitmap_invulnerability_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/5.5), (int)(screen_width_ / 5.5), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
//
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_play, options);
        bitmap_button_Play_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/94*20), (int)(screen_width_ /256*20), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }
//
        button_Play_.collision_box_size_.x_ = (int)(bitmap_button_Play_.getWidth()*0.5);
        button_Play_.collision_box_size_.y_ = (int)(bitmap_button_Play_.getHeight()*0.5);
        button_Play_.position_.x_ = screen_width_*0.5;
        button_Play_.position_.y_ = screen_height_*0.5;
        button_Play_.UpdateBox();


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_menu, options);
        bitmap_button_Menu_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/94*15), (int)(screen_width_ /256*15), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        button_Menu_.collision_box_size_.x_ = (int)(bitmap_button_Menu_.getWidth()*0.5);
        button_Menu_.collision_box_size_.y_ = (int)(bitmap_button_Menu_.getHeight()*0.5);
        button_Menu_.position_.x_ = screen_width_*0.25;
        button_Menu_.position_.y_ = screen_height_*0.8;
        button_Menu_.UpdateBox();

//
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_retry, options);
        bitmap_button_Retry_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/94*15), (int)(screen_width_ /256*15), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        button_Retry_.collision_box_size_.x_ = (int)(bitmap_button_Retry_.getWidth()*0.5);
        button_Retry_.collision_box_size_.y_ = (int)(bitmap_button_Retry_.getHeight()*0.5);
        button_Retry_.position_.x_ = screen_width_*0.75;
        button_Retry_.position_.y_ = screen_height_*0.8;
        button_Retry_.UpdateBox();

//
        options.inDither = false;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.button_pause, options);
        bitmap_button_Pause_ = Bitmap.createScaledBitmap(bitmap, (int)(screen_width_/25), (int)(screen_width_ /25), true);

        if(bitmap != null)
        {
            bitmap.recycle();
            bitmap = null;
        }

        button_Pause_.collision_box_size_.x_ = (int)(bitmap_button_Pause_.getWidth()*0.5);
        button_Pause_.collision_box_size_.y_ = (int)(bitmap_button_Pause_.getHeight()*0.5);
        button_Pause_.position_.x_ = screen_width_*0.9;
        button_Pause_.position_.y_ = screen_height_*0.1;
        button_Pause_.UpdateBox();


        clickymouse_.collision_box_size_.x_ = 0.1;
        clickymouse_.collision_box_size_.y_ = 0.1;
        clickymouse_.position_.x_ = 0;
        clickymouse_.position_.y_ = 0;

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        for(int i = 0; i < num_monies_; ++i)
        {
            monies_list_.add(new GameObjectMoney());
        }

        AUDIO_BACKGROUNDMUSIC = MediaPlayer.create(context, R.raw.bgm);
        AUDIO_BACKGROUNDMUSIC.setVolume(0.6f, 0.6f);
        AUDIO_BACKGROUNDMUSIC.start();
        AUDIO_SOUNDS = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        AUDIO_CASINO = AUDIO_SOUNDS.load(context, R.raw.casino, 1);
        AUDIO_FLY = AUDIO_SOUNDS.load(context, R.raw.fly, 1);

        preferenceSettings = context.getSharedPreferences("GAME", PREFERENCE_MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();


        i_highscore = preferenceSettings.getInt("G_HIGHSCORE", 0);

        preferenceEditor.commit();

        game_state_ = GAMESTATE.SPLASH;
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
        game_object_list_.clear();

        main_character_.collision_box_size_.x_ = (int)(bitmap_main_character_.getWidth()*0.3);
        main_character_.collision_box_size_.y_ = (int)(bitmap_main_character_.getHeight()*0.45);
        main_character_.position_.x_ = 200.0;
        main_character_.position_.y_ = screen_height_*0.5;
        main_character_.velocity_.y_ = 0.0;

        current_invulnerability_ = invulnerability_;

        current_num_monies_ = num_monies_;

        for(GameObjectMoney munny : monies_list_)
        {
            munny.position_.x_ = random_generator_.nextDouble() * 50.0 + 175.0;
            munny.position_.y_ = random_generator_.nextDouble() * 50.0 + screen_height_*0.5 - 25;
            munny.Create();
            munny.Set(main_character_, 0.0, -120.0, random_generator_.nextDouble() * 50.0 + 100.0);
        }
    }

    public void GenerateBlocks()
    {
        countdown_to_next_spawn_ = random_generator_.nextInt(7) + 3;
        Double gap = bitmap_block_.getWidth()*1.2;

        int temp = random_generator_.nextInt(bitmap_block_.getHeight()/3);

        GameObjectBlock GO = new GameObjectBlock();
        GO.Set(main_character_, score_);
        GO.position_.x_ = screen_width_ + bitmap_block_.getWidth();
        GO.position_.y_ = screen_height_ - temp;
        GO.velocity_.x_ = -120.f;
        GO.velocity_.y_ = 0;
        GO.collision_box_size_.x_ = bitmap_block_.getWidth()/2;
        GO.collision_box_size_.y_ = bitmap_block_.getHeight()/2;
        game_object_list_.add(GO);

        GameObject GO1 = new GameObject();
        GO1.position_.x_ = screen_width_ + bitmap_block_.getWidth();
        GO1.position_.y_ = screen_height_ - temp - gap - bitmap_block_.getHeight();
        GO1.velocity_.x_ = -120.f;
        GO1.velocity_.y_ = 0;
        GO1.collision_box_size_.x_ = bitmap_block_.getWidth()/2;
        GO1.collision_box_size_.y_ = bitmap_block_.getHeight()/2;
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
            case SPLASH:
            {
                if(timer_ > 3)
                {
                    game_state_ = GAMESTATE.MENU;
                }
                else
                {
                    timer_ += delta_time_;
                }
            }
            break;
            case MENU:
            {
                if(GameObject.checkCollision(button_Play_, clickymouse_))
                {
                    game_state_ = GAMESTATE.PLAY;
                    Init();
                }
            }
            break;
            case PLAY:
            {
                countdown_to_next_spawn_ -= delta_time_ * speed_multiplier;

                bgX -= 100 * delta_time_;
                if (bgX < -screen_width_) {
                    bgX = 0;
                }

                if (countdown_to_next_spawn_ < 0) {
                    GenerateBlocks();
                }

                main_character_.velocity_.y_ += 275 * delta_time_;
                main_character_.Update(delta_time_);

                for (GameObjectMoney munny : monies_list_) {
                    munny.Update(delta_time_);
                }

                if (main_character_.position_.y_ > screen_height_)
                {
                    countdown_to_next_spawn_ = random_generator_.nextDouble() * 3.0;
                    for(GameObjectMoney munny : monies_list_)
                    {
                        munny.ReactToCollision();
                    }
                    game_state_ = GAMESTATE.DEATH;
                }

                if (current_invulnerability_ >= 0)
                {
                    current_invulnerability_ -= delta_time_;
                }

                if(score_to_increase == score_.Num() % score_to_increase + 1)
                {
                    score_to_increase += 5;
                    speed_multiplier += 0.5;
                }

                for (GameObject object : game_object_list_)
                {
                    object.Update(delta_time_ * speed_multiplier);

                    if (object.position_.x_ < 0 - bitmap_block_.getWidth())
                    {
                        object.Destroy();
                        continue;
                    }

                    if(current_invulnerability_ >= 0)
                    {
                        continue;
                    }

                    if (GameObject.checkCollision(object, main_character_))
                    {
                        current_invulnerability_ = invulnerability_;
                        AUDIO_SOUNDS.play(AUDIO_CASINO, 1.0f, 1.0f, 0, 0, 1);
                        --current_num_monies_;
                        monies_list_.get(current_num_monies_).ReactToCollision();
                        if (current_num_monies_ == 0)
                        {
                            countdown_to_next_spawn_ = random_generator_.nextDouble() * 3.0;
                            main_character_.position_.y_ = screen_height_ / 2;
                            main_character_.velocity_.y_ = 0;
                            game_state_ = GAMESTATE.DEATH;
                        }
                    }
                }

                if(GameObject.checkCollision(button_Pause_, clickymouse_))
                {
                    game_state_ = GAMESTATE.PAUSED;
                }
            }
            break;
            case DEATH:
            {
                if(score_.Num() > i_highscore)
                {
                    i_highscore = score_.Num();
                    preferenceEditor.putInt("G_HIGHSCORE", i_highscore);
                }
                if(countdown_to_next_spawn_ > 0)
                {
                    countdown_to_next_spawn_ -= delta_time_;
                }
                for(GameObjectMoney munny : monies_list_)
                {
                    munny.Update(delta_time_);

                    if(munny.position_.y_ < -bitmap_monies_.getHeight() && countdown_to_next_spawn_ <= 0)
                    {
                        munny.position_.x_ = random_generator_.nextDouble() * screen_width_;
                        munny.position_.y_ = screen_height_ + bitmap_monies_.getHeight();
                        countdown_to_next_spawn_ = random_generator_.nextDouble() * 3.0;
                        break;
                    }
                }

                if(GameObject.checkCollision(button_Retry_, clickymouse_))
                {
                    game_state_ = GAMESTATE.PLAY;
                    Init();
                }
                else if(GameObject.checkCollision(button_Menu_, clickymouse_))
                {
                    game_state_ = GAMESTATE.MENU;
                }
            }
            break;
        }

        clickymouse_.UpdateBox();
        clickymouse_.position_.x_ = 0;
        clickymouse_.position_.y_ = 0;
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
            case SPLASH:
            {
                canvas.drawBitmap(bitmap_splash_, 0, 0, null);
            }
            break;
            case MENU:
            {
                canvas.drawBitmap(bitmap_menu_, 0, 0, null);
                canvas.drawBitmap(bitmap_button_Play_, (int)(button_Play_.position_.x_ - bitmap_button_Play_.getWidth()*0.5), (int)(button_Play_.position_.y_ - bitmap_button_Play_.getHeight()*0.5), null);
            }
            break;
            case PLAY:
            {
                RenderGameplay(canvas);
                canvas.drawBitmap(bitmap_button_Pause_, (int) (button_Pause_.position_.x_ - bitmap_button_Pause_.getWidth() * 0.5), (int) (button_Pause_.position_.y_ - bitmap_button_Pause_.getHeight() * 0.5), null);
                break;
            }
            case PAUSED:
            {
                Paint paint = new Paint();
                paint.setARGB(255, 255, 255, 255);
                paint.setStrokeWidth(100);
                paint.setTextSize(40);

                RenderGameplay(canvas);
                canvas.drawText("Paused", screen_width_ / 2 - 100, screen_height_ / 2, paint);
            }
            break;
            case DEATH:
            {
                canvas.drawBitmap(bitmap_menu_, 0, 0, null);

                for(GameObjectMoney munny : monies_list_)
                {
                    canvas.drawBitmap(bitmap_monies_, (int)(munny.position_.x_ - bitmap_monies_.getWidth()*0.5), (int)(munny.position_.y_ - bitmap_monies_.getHeight()*0.5), null);
                }

                Paint paint = new Paint();
                paint.setARGB(255, 255, 255, 255);
                paint.setStrokeWidth(100);
                paint.setTextSize(40);

                canvas.drawText("Gameover, you lost your money", screen_width_ / 2 - 300, screen_height_ / 2 - 50, paint);
                canvas.drawText("Score: " + score_.Num(), screen_width_/2 - 300, screen_height_/2, paint);
                canvas.drawText("Highscore: " + i_highscore, screen_width_/2 - 300, screen_height_/2 + 50, paint);

                canvas.drawBitmap(bitmap_button_Menu_, (int) (button_Menu_.position_.x_ - bitmap_button_Menu_.getWidth() * 0.5), (int) (button_Menu_.position_.y_ - bitmap_button_Menu_.getHeight() * 0.5), null);
                canvas.drawBitmap(bitmap_button_Retry_, (int) (button_Retry_.position_.x_ - bitmap_button_Retry_.getWidth() * 0.5), (int) (button_Retry_.position_.y_ - bitmap_button_Retry_.getHeight() * 0.5), null);
                break;
            }
        }
    }

    public void RenderGameplay(Canvas canvas)
    {
        canvas.drawBitmap(bitmap_background_, bgX, bgY, null);
        canvas.drawBitmap(bitmap_background_, bgX + screen_width_, bgY, null);

        for(GameObjectMoney munny : monies_list_)
        {
            canvas.drawBitmap(bitmap_monies_, (int)(munny.position_.x_ - bitmap_monies_.getWidth()*0.5), (int)(munny.position_.y_ - bitmap_monies_.getHeight()*0.5), null);
        }

        canvas.drawBitmap(bitmap_main_character_, (int)(main_character_.position_.x_ - bitmap_main_character_.getWidth()*0.5), (int)(main_character_.position_.y_ - bitmap_main_character_.getHeight()*0.5), null);
        if(current_invulnerability_ >= 0)
        {
            canvas.drawBitmap(bitmap_invulnerability_, (int) (main_character_.position_.x_ - bitmap_invulnerability_.getWidth() * 0.5), (int) (main_character_.position_.y_ - bitmap_invulnerability_.getHeight() * 0.5), null);
        }

        //canvas.drawBitmap(bitmap_invulnerability_, (int) (f_Accelerometer[1] - bitmap_invulnerability_.getWidth() * 0.5), (int) (f_Accelerometer[0] - bitmap_invulnerability_.getHeight() * 0.5), null);


        for (GameObject object : game_object_list_)
        {
            canvas.drawBitmap(bitmap_block_, (int)(object.position_.x_ - bitmap_block_.getWidth()*0.5), (int)(object.position_.y_ - bitmap_block_.getHeight()*0.5), null);
        }

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 255, 255, 255);
        paint.setStrokeWidth(100);
        paint.setTextSize(25);
        canvas.drawText("FPS: " + fps_, 130, 60, paint);
        canvas.drawText("Score: " + score_.Num(), 130, 80, paint);
        canvas.drawText("Highscore: " + i_highscore, 130, 100, paint);
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
        preferenceEditor.commit();
        AUDIO_SOUNDS.unload(AUDIO_FLY);
        AUDIO_SOUNDS.unload(AUDIO_CASINO);
        AUDIO_BACKGROUNDMUSIC.stop();
        AUDIO_BACKGROUNDMUSIC.release();

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
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            switch (game_state_)
            {
                case MENU:
                {

                }
                break;
                case PLAY:
                {
                    AUDIO_SOUNDS.play(AUDIO_FLY, 2.0f, 2.0f, 0, 0, 1.0f);
                    if(main_character_.velocity_.y_ > 0)
                    {
                        main_character_.velocity_.y_ = -200;
                    }
                    else
                    {
                        main_character_.velocity_.y_ += -100;
                    }
                }
                break;
                case DEATH:
                {

                }
                break;
                case PAUSED:
                {
                    game_state_ = GAMESTATE.PLAY;
                }
                break;
            }

            clickymouse_.position_.x_ = event.getX();
            clickymouse_.position_.y_ = event.getY();

        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            clickymouse_.position_.x_ = 0;
            clickymouse_.position_.y_ = 0;
        }

        return super.onTouchEvent(event);
    }
}

