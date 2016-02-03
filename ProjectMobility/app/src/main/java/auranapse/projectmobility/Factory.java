package auranapse.projectmobility;

import java.util.Vector;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class Factory<Product> extends BaseFactory
{
    public Factory()
    {
        products_ = new Vector<>();
        production_rate_ = 1;
    }
    public Factory(final int production_rate)
    {
        products_ = new Vector<>();
        production_rate_ = production_rate;
    }

    public final void SetProductionRate(final int production_rate)
    {
        production_rate_ = production_rate;
    }
    public final Product GetProduct(final Receipt receipt)
    {
        if(receipt.GetFactory() == this)
        {
            return products_.get(receipt.GetId());
        }
        return null;
    }

    @Override
    protected final void ProduceMoreProducts()
    {
        for(int i = 0; i < production_rate_; ++i)
        {
            products_.add(GetNewProduct());
            AddNewReceipt(products_.size() - 1);
        }

    }
    protected abstract Product GetNewProduct();

    private Vector<Product> products_;
    private int production_rate_;
}