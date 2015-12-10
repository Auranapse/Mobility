package auranapse.projectmobility;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class Vector3
{
    public double x_ = 0;
    public double y_ = 0;
    public double z_ = 0;

    public double Length()
    {
        return Math.sqrt(x_ * x_ * y_ * y_ * z_ * z_);
    }

    public double LengthSquared()
    {
        return x_ * x_ * y_ * y_ * z_ * z_;
    }

    public Vector3 Normalise()
    {
        return Multiply(1.0/Length());
    }

    public Vector3 Multiply(final double multiply)
    {
        x_ *= multiply;
        y_ *= multiply;
        z_ *= multiply;
        return this;
    }

    public Vector3 Add(final Vector3 vector)
    {
        x_ += vector.x_;
        y_ += vector.y_;
        z_ += vector.z_;
        return this;
    }

    public Vector3 Subtract(final Vector3 vector)
    {
        x_ -= vector.x_;
        y_ -= vector.y_;
        z_ -= vector.z_;
        return this;
    }

    public Vector3 Copy(final Vector3 vector)
    {
        x_ = vector.x_;
        y_ = vector.y_;
        z_ = vector.z_;
        return this;
    }
}