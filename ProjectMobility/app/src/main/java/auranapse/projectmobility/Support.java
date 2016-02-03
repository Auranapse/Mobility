package auranapse.projectmobility;

import java.util.HashMap;

/**
 * Created by mrlol on 03-Feb-16.
 */
public class Support
{
    public Support()
    {
        service_table_ = new HashMap<>();
    }
    public final void AddService(final BaseService service)
    {
        service_table_.put(service.GetFactory(), service);
    }
    public final BaseService GetServiceFor(final BaseFactory factory)
    {
        if(service_table_.containsKey(factory))
        {
            return service_table_.get(factory);
        }
        return null;
    }

    HashMap<BaseFactory, BaseService> service_table_;
}
