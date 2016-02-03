package auranapse.projectmobility;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class BaseService
{
    public BaseService()
    {

    }

    public abstract BaseFactory GetFactory();
    public abstract void ServiceProduct(final Receipt receipt);
}
