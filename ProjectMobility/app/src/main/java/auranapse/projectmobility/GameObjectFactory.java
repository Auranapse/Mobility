package auranapse.projectmobility;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class GameObjectFactory
{
    ProductionLine block_production_ = new ProductionLine();
    ProductionLine money_production_ = new ProductionLine();

    public GameObject GetBlock(GameObject main_character, Score score)
    {
        GameObjectBlock product = (GameObjectBlock)block_production_.GetProduct();
        if(product == null)
        {
            block_production_.AddProduct(product = new GameObjectBlock());
        }
        product.Set(main_character, score);
        return product;
    }

    public GameObject GetMoney(GameObject main_character, final double fly_velocity_x, final double fly_velocity_y, final double speed)
    {
        GameObjectMoney product = (GameObjectMoney)money_production_.GetProduct();
        if(product == null)
        {
            block_production_.AddProduct(product = new GameObjectMoney());
        }
        product.Set(main_character, fly_velocity_x, fly_velocity_y, speed);
        return product;
    }
}