package auranapse.projectmobility;

import java.util.Random;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class GameObjectMoney extends GameObject
{
    boolean fly_ = false;
    GameObject main_character_ = null;
    Vector3 fly_velocity_ = new Vector3();
    Vector3 normal_velocity_ = new Vector3();
    double speed_ = 0;

    public void Set(GameObject main_character, final double fly_velocity_x, final double fly_velocity_y, final double speed)
    {
        main_character_ = main_character;
        fly_velocity_.x_ = fly_velocity_x;
        fly_velocity_.y_ = fly_velocity_y;
        speed_ = speed;
    }

    public Product Create()
    {
        fly_ = false;
        return this;
    }

    public void Update(float delta_time)
    {
        super.Update(delta_time);
        if(fly_)
        {
            Vel_X = (float)fly_velocity_.x_;
            Vel_Y = (float)fly_velocity_.y_;
        }
        else
        {
            normal_velocity_.x_ = main_character_.Pos_X - Pos_X;
            normal_velocity_.y_ = main_character_.Pos_Y - Pos_Y;

            normal_velocity_.Normalise().Multiply(speed_);

            Vel_X = (float)normal_velocity_.x_;
            Vel_Y = (float)normal_velocity_.y_;
        }
    }

    public void ReactToCollision()
    {
        fly_ = true;
    }

    public void Destroy()
    {
    }
}
