package auranapse.projectmobility;

/**
 * Created by mrlol on 10-Dec-15.
 */
public class GameObjectFactory
{
    ProductionLine<GameObjectBlock> block_production_ = new ProductionLine<>();

    public GameObject GetBlock(GameObject main_character, Score score)
    {
        GameObjectBlock product = block_production_.GetProduct();
        if(product == null)
        {
            block_production_.AddProduct(product = new GameObjectBlock(main_character, score));
        }
        product.Set(main_character, score);
        return product;
    }
}