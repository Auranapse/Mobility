package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class AABBBox extends Volume
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

    @Override
    public final double GetArea()
    {
        double length_x = range_x_.GetLength();
        double length_y = range_y_.GetLength();
        double length_z = range_z_.GetLength();

        return length_x * length_y * 2 + length_y * length_z * 2 + length_z * length_x * 2;
    }
    @Override
    public final double GetVolume()
    {
        return range_x_.GetLength() * range_y_.GetLength() * range_z_.GetLength();
    }
    public boolean IsOverlapping(final AABBBox box)
    {
        return range_x_.IsOverlapping(box.range_x_) && range_y_.IsOverlapping(box.range_y_) && range_z_.IsOverlapping(box.range_z_);
    }
}