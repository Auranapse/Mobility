package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class Service<Product> extends BaseService
{
    Service(final Factory<Product> factory)
    {
        factory_ = factory;
    }

    @Override
    public final BaseFactory GetFactory()
    {
        return factory_;
    }
    @Override
    public final void ServiceProduct(final Receipt receipt)
    {
        ServiceProduct(factory_.GetProduct(receipt));
    }

    protected abstract void ServiceProduct(final Product product);

    private final Factory<Product> factory_;
}
