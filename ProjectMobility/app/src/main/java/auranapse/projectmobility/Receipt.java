package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public class Receipt
{
    public Receipt(final BaseFactory factory, final int id)
    {
        factory_ = factory;
        id_ = id;
    }

    public final BaseFactory GetFactory()
    {
        return factory_;
    }
    public final int GetId()
    {
        return id_;
    }
    public final void ReturnProduct()
    {
        factory_.ReturnProduct(this);
    }

    private final BaseFactory factory_;
    private final int id_;
}