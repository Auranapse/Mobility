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
    Vector3 distance_from_main_character = new Vector3();
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
        super.Create();
        fly_ = false;
        return this;
    }

    public void Update(double delta_time)
    {
        super.Update(delta_time);
        if(fly_)
        {
            velocity_.Copy(fly_velocity_);
        }
        else
        {
            distance_from_main_character.Copy(main_character_.position_).Subtract(position_);

            normal_velocity_.Add((distance_from_main_character).Normalise().Multiply(speed_).Multiply(delta_time));

            velocity_.Copy(normal_velocity_);
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
