package auranapse.projectmobility;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class Product<Type>
{
    public Product()
    {
        receipt_ = null;
        product_ = null;
    }

    public final <Class extends Type> void Copy(final Product<Class> product)
    {
        ReturnToFactory();
        receipt_ = product.GetReceipt();
        product_ = product.Get();
    }
    public final boolean Exists()
    {
        return receipt_ == null;
    }
    public final void ReturnToFactory()
    {
        if(receipt_ == null)
        {
            return;
        }

        receipt_.ReturnProduct();
        receipt_ = null;
        product_ = null;
    }
    public final Receipt GetReceipt()
    {
        return receipt_;
    }
    public final Type Get()
    {
        return product_;
    }
    public final <Class extends Type> void OrderFrom(final Factory<Class> factory)
    {
        receipt_ = factory.OrderProduct();
        product_ = factory.GetProduct(receipt_);
    }
    public final void AcceptSupport(final Support support)
    {
        if(receipt_ == null)
        {
            return;
        }

        BaseService service = support.GetServiceFor(receipt_.GetFactory());
        if(service != null)
        {
            service.ServiceProduct(receipt_);
        }
    }
    public final <Class extends Type> boolean Equals(final Product<Class> product)
    {
        return product_ == product.Get();
    }
    public final boolean Equals(final Type var)
    {
        return product_ == var;
    }

    private Receipt receipt_;
    private Type product_;
}