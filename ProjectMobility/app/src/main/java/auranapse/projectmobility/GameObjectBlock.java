package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 10-Dec-15.
 */
public class GameObjectBlock extends GameObject
{
    private GameObject main_char_;
    private int score_;
    private boolean havent_scored_;

    public GameObjectBlock(GameObject main_char, int score)
    {
        havent_scored_ = false;
        main_char_ = main_char;
        score_ = score;
    }

    public void Update(final float delta_time)
    {
        if(CheckIfScored())
        {
            ++score_;
        }
    }
    public boolean CheckIfScored()
    {
        if(main_char_.Pos_X < Pos_X && havent_scored_)
        {
            havent_scored_ = false;
            return true;
        }

        return false;
    }
}