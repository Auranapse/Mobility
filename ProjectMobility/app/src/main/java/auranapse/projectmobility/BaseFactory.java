package auranapse.projectmobility;

import java.util.Stack;

/**
 * Created by mrlol on 03-Feb-16.
 */
public abstract class BaseFactory
{
    public BaseFactory()
    {
        receipts_ = new Stack<>();
    }

    public final Receipt OrderProduct()
    {
        if (receipts_.empty())
        {
            ProduceMoreProducts();
        }
        return receipts_.pop();
    }
    public final void ReturnProduct(final Receipt receipt)
    {
        AddNewReceipt(receipt);
    }

    protected final void AddNewReceipt(final int receiptNo)
    {
        AddNewReceipt(new Receipt(this, receiptNo));
    }
    protected abstract void ProduceMoreProducts();

    private final void AddNewReceipt(final Receipt receipt)
    {
        receipts_.push(receipt);
    }

    private Stack<Receipt> receipts_;
}