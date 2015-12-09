package auranapse.projectmobility;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class Score
{
    private int score_ = 0;

    public int Num()
    {
        return score_;
    }

    public void Increment()
    {
        ++score_;
    }

    public void Reset()
    {
        score_ = 0;
    }
}
