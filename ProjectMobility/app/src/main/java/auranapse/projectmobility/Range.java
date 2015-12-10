package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class Range
{
    public double start_;
    public double end_;

    public Range()
    {
        start_ = 0.0;
        end_ = 0.0;
    }
    public Range(float start, float end)
    {
        this.start_ = start;
        this.end_ = end;
    }

    public boolean IsOverlapping(Range range)
    {
        return start_ <= range.end_ && end_ >= range.start_;
    }
}
