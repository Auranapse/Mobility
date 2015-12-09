package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class AABBBox
{
    public Range range_x_;
    public Range range_y_;
    //public Range range_z_;

    public AABBBox()
    {
        range_x_ = new Range();
        range_y_ = new Range();
        //range_z_ = new Range();
    }

    public AABBBox(final Range range_x, final Range range_y)//, final Range range_z)
    {
        range_x_ = range_x;
        range_y_ = range_y;
        //range_z_ = new Range(range_z);
    }

    boolean IsOverlapping(final AABBBox box)
    {
        return range_x_.IsOverlapping(box.range_x_) && range_y_.IsOverlapping(box.range_y_);// && range_x_.IsOverlapping(box.z);
    }
}