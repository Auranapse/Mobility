package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class Boundary
{
    public Boundary()
    {
        collision_support_ = null;
    }

    public final boolean IsOverlapping(final Product<Boundary> boundary)
    {
        collision_support_.CheckCollisionWith(boundary);
    }

    public final void SetCollisionSupport(final CollisionSupport collision_support)
    {
        collision_support_ = collision_support;
    }

    public final boolean CollisionSupportHasBeenSet()
    {
        return collision_support_ != null;
    }

    public abstract double GetArea();

    public CollisionSupport collision_support_;
}
