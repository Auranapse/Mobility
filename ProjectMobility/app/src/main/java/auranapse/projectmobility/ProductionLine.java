package auranapse.projectmobility;

import android.util.Pair;

import java.util.Vector;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class ProductionLine<ProductType>
{
    public ProductType GetProduct()
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
    public ProductType AddProduct(Product new_product)
    {
        products_.add(new_product);
        return (ProductType)new_product;
    }

    private
    Vector<Product> products_ = new Vector<>();
}