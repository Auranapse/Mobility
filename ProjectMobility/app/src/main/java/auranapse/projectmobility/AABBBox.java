package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class AABBBox
{
    public Range range_x_;
    public Range range_y_;
    public Range range_z_;

    public AABBBox()
    {
        range_x_ = new Range();
        range_y_ = new Range();
        range_z_ = new Range();
    }

    boolean IsOverlapping(final AABBBox box)
    {
        return range_x_.IsOverlapping(box.range_x_) && range_y_.IsOverlapping(box.range_y_) && range_z_.IsOverlapping(box.range_z_);
    }
}