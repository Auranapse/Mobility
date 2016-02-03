package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public class CollisionSupport extends Support
{
    public CollisionSupport()
    {
        collision_report_ = new CollisionReport();
    }
    public final CollisionReport CheckCollisionWith(final Product<Boundary> boundary)
    {
        boundary.AcceptSupport(this);
        return collision_report_;
    }
    public final void AddCollisionService(CollisionService service)
    {
           service.SetCollisionReport(collision_report_);
    }

    private CollisionReport collision_report_;
}