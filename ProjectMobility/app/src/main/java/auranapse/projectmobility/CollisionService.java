package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class CollisionService<HostBoundary, ClientBoundary> extends Service<ClientBoundary>
{
    public CollisionService(final HostBoundary host_boundary, final Factory<ClientBoundary> factory)
    {
        super(factory);
        host_boundary_ = host_boundary;
        collision_report_ = null;
    }

    public final void SetCollisionReport(final CollisionReport collision_report)
    {
        collision_report_ = collision_report;
    }

    @Override
    protected final void ServiceProduct(final ClientBoundary boundary)
    {
        DoCollision(host_boundary_, boundary, collision_report_);
    }

    protected abstract void DoCollision(final HostBoundary host_boundary, final ClientBoundary client_boundary, final CollisionReport collision_report);

    private final HostBoundary host_boundary_;
    private CollisionReport collision_report_;
}