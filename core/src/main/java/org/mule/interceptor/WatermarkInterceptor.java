package org.mule.interceptor;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.expression.ExpressionManager;
import org.mule.api.interceptor.Interceptor;
import org.mule.api.store.ObjectStore;
import org.mule.api.store.ObjectStoreException;
import org.mule.processor.AbstractInterceptingMessageProcessor;

import java.io.Serializable;

/**
 * Watermark message processor. Watermark determines the watermark value from the object store once it is
 * called, and stores the watermark value in the object store after the flow ends.
 */
public class WatermarkInterceptor extends AbstractInterceptingMessageProcessor implements Interceptor
{

    /**
     * The watermark key. Can be an expression or a string. The watermark key is used to retrieve/store
     * the watermark value from the object store. It is also used as invocation variable name when the
     * watermark message processor is called.
     * <p/>
     * If the key is an expression, then it is evaluated at the message processor (to retrieve the value) and
     * after the listener process call (to store the value)
     */
    private String variable;

    /**
     * Used to define the default value of the watermark in case the object store does not contain the
     * requested key.
     */
    private String defaultExpression;

    /**
     * Expression to be used to update the watermark value in the object store.
     */
    private String updateExpression;

    /**
     * The mule object store reference. It is optional, in that case it is a persistence object store.
     */
    private ObjectStore objectStore;


    private ExpressionManager expressionManager;


    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        addWatermarkInvocationProperty(event);
        MuleEvent returnedEvent = getListener().process(event);
        storeWatermarkValue(returnedEvent);

        return returnedEvent;
    }

    private void storeWatermarkValue(MuleEvent returnedEvent) throws ObjectStoreException
    {
        Serializable objectStoreKey = evaluate(variable, returnedEvent);
        synchronized (objectStore)
        {
            if (objectStore.contains(objectStoreKey))
            {
                objectStore.remove(objectStoreKey);
            }

            if ( updateExpression == null ){
                objectStore.store(objectStoreKey, (Serializable) returnedEvent.getMessage().getInvocationProperty((String) objectStoreKey));
            }
            else{
                objectStore.store(objectStoreKey, evaluate(updateExpression, returnedEvent));
            }
        }
    }

    private void addWatermarkInvocationProperty(MuleEvent event) throws ObjectStoreException
    {
        String objectStoreKey = (String) evaluate(variable, event);
        if (objectStore.contains(objectStoreKey))
        {
            event.getMessage().setInvocationProperty(objectStoreKey, objectStore.retrieve(objectStoreKey));
        }
        else
        {
            event.getMessage().setInvocationProperty(objectStoreKey, evaluate(defaultExpression, event));
        }
    }

    private Serializable evaluate(String expression, MuleEvent event)
    {
        if (expressionManager.isExpression(expression) && expressionManager.isValidExpression(expression))
        {
            return (Serializable) expressionManager.evaluate(expression, event);
        }

        return expression;
    }

    public void setVariable(String variable)
    {
        this.variable = variable;
    }

    public void setDefaultExpression(String defaultExpression)
    {
        this.defaultExpression = defaultExpression;
    }

    public void setUpdateExpression(String updateExpression)
    {
        this.updateExpression = updateExpression;
    }

    public void setObjectStore(ObjectStore objectStoreReference)
    {
        this.objectStore = objectStoreReference;
    }

    @Override
    public void setMuleContext(MuleContext context)
    {
        super.setMuleContext(context);
        expressionManager = muleContext.getExpressionManager();
    }


}
