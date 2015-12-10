package auranapse.projectmobility;

/**
 * Created by Gabriel Wong on 09-Dec-15.
 */
public class GameObject implements Product
{
    public Vector3 velocity_ = new Vector3();
    public Vector3 position_ = new Vector3();

    public Vector3 collision_box_size_ = new Vector3();
    public Vector3 collision_box_offset_ = new Vector3();
    public Vector3 temp_vec_ = new Vector3();

    AABBBox box = new AABBBox();

    boolean active = true;

    public Product Create()
    {
        active = true;
        return this;
    }

    public void Destroy()
    {
        active = false;
    }

    public boolean IsDestroyed()
    {
        return !active;
    }

    public static boolean checkCollision(final GameObject GO1, final GameObject GO2)
    {
        return GO1.box.IsOverlapping(GO2.box);
    }

    public void Update(final double delta_time)
    {
        Vector3 distanceTravelled = temp_vec_;

        distanceTravelled.Copy(velocity_);
        distanceTravelled.Multiply(delta_time);

        position_.Add(distanceTravelled);

        UpdateBox();
    }

    public void UpdateBox()
    {
        box.range_x_.start_ = position_.x_ - collision_box_size_.x_ + collision_box_offset_.x_;
        box.range_x_.end_ = position_.x_ + collision_box_size_.x_ + collision_box_offset_.x_;

        box.range_y_.start_ = position_.y_ - collision_box_size_.y_ + collision_box_offset_.y_;
        box.range_y_.end_ = position_.y_ + collision_box_size_.y_ + collision_box_offset_.y_;
    }
}
