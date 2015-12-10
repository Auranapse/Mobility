package auranapse.projectmobility;

import java.util.Vector;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class ProductionLine
{
    private Vector<Product> products_ = new Vector<>();

    public Product GetProduct()
    {
        for(Product product : products_)
        {
            if(product.IsDestroyed())
            {
                return product.Create();
            }
        }
        return null;
    }

    public Product AddProduct(Product new_product)
    {
        products_.add(new_product);
        return new_product;
    }
}