package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 10-Dec-15.
 */
public class GameObjectBlock extends GameObject
{
    private GameObject main_char_;
    private Score score_;
    private boolean havent_scored_;

    public GameObjectBlock()
    {
    }

    public void Set(GameObject main_char, Score score)
    {
        havent_scored_ = true;
        main_char_ = main_char;
        score_ = score;
    }

    public void Update(final double delta_time)
    {
        super.Update(delta_time);

        if(CheckIfScored())
        {
            score_.Increment();
        }
    }
    public boolean CheckIfScored()
    {
        if(main_char_.position_.x_ > position_.x_ && havent_scored_)
        {
            havent_scored_ = false;
            return true;
        }

        return false;
    }
}