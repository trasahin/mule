package org.mule.test.integration.interceptor;

import static org.junit.Assert.assertEquals;
import org.mule.api.MuleEvent;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.notification.CustomNotificationListener;
import org.mule.api.context.notification.ServerNotification;
import org.mule.api.store.ObjectStore;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.construct.Flow;
import org.mule.context.notification.CustomEventNotification;
import org.mule.context.notification.NotificationException;
import org.mule.tck.junit4.FunctionalTestCase;

import java.util.Queue;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;
import org.mortbay.util.ArrayQueue;

/**
 * Test notes:
 * <p/>
 * For createKeyValueFlow, updatedKeyValueFlow, keyAsAnExpressionFlow, keyAsAnExpressionThatChangesFlow:
 * (1) The update expression of the watermark is the flow variable with the same name as the key (we check with this that
 * the flow variable is correctly populated.
 * (2) The Default expression of the watermark is #[payload]
 */
public class WatermarkInterceptorFlowTestCase extends FunctionalTestCase
{
    private WatermarkNotificationListener listener = new WatermarkNotificationListener();

    @Before
    public void registerListener() throws NotificationException
    {
        muleContext.registerListener(listener);
    }

    /**
     * Scenario: The object store key does not exist and the Object store is the default one.
     * Behaviour: The key is created in the user default transient object store with the value of the update expression
     */
    @Test
    public void notExistingOSKey() throws Exception
    {
        String initialValue = "osValue";
        String finalValue = "osValue";

        Flow flow = getFlow("createKeyValueFlow");
        flow.process(getTestEvent(initialValue));
        assertEquals(finalValue, getDefaultObjectStore().retrieve("test"));

        checkNotifications(initialValue, finalValue);
    }



    /**
     * Scenario: Same as {@link #notExistingOSKey} but with a defined object store
     * Behaviour: Same as {@link #notExistingOSKey} but with a defined object store
     */
    @Test
    public void notExistingOSKeyWithOSDefinition() throws Exception
    {
        Flow flow = getFlow("changedObjectStore");
        flow.process(getTestEvent("osValue"));
        assertEquals("osValue", getObjectStore(MuleProperties.OBJECT_STORE_DEFAULT_IN_MEMORY_NAME).retrieve("test"));

        checkNotifications("osValue", "osValue");

    }

    /**
     * Scenario: The object store contains the key and the object store is the default one.
     * Behaviour: The key is updated with the new value
     */
    @Test
    public void keyExistsAndValueIsUpdated() throws Exception
    {
        ObjectStore os = getDefaultObjectStore();
        os.store("test", "existentValue");

        Flow flow = getFlow("updatedKeyValueFlow");
        flow.process(getTestEvent("osValue"));
        assertEquals("osValue", os.retrieve("test"));

        checkNotifications("existentValue", "osValue");
    }

    /**
     * Scenario: The update expression is not set, then it takes the value to store from the variable flow var
     */
    @Test
    public void updateExpressionNotSet() throws Exception
    {
        ObjectStore os = getDefaultObjectStore();
        os.store("test", "existentValue");

        Flow flow = getFlow("updateExpressionNotSet");
        flow.process(getTestEvent("osValue"));
        assertEquals("osValue", os.retrieve("test"));

        checkNotifications("existentValue", "osValue");
    }


    /**
     * Scenario: The object store key IS an expression and it is updated in the object store
     * Behaviour: The key expression is evaluated and the object store is updated based on the result of that expression.
     */
    @Test
    public void keyIsAMelExpression() throws Exception
    {
        ObjectStore os = getDefaultObjectStore();
        os.store("test", "existentValue");

        Flow flow = getFlow("updatedKeyValueFlow");
        MuleEvent testEvent = getTestEvent("osValue");
        testEvent.setFlowVariable("keyVariable", "test");
        flow.process(testEvent);
        assertEquals("osValue", os.retrieve("test"));

        checkNotifications("existentValue", "osValue");
    }


    /**
     * Scenario: The object store key IS an expression, the value of that expression changes in the flow execution,
     * the new key is updated in the object store.
     * Behaviour: The key expression is evaluated and the object store is updated based on the result of that expression.
     */
    @Test
    public void keyIsAMelExpressionThatChangesInTheFlow() throws Exception
    {
        ObjectStore os = getDefaultObjectStore();
        os.store("test", "existentValue");

        Flow flow = getFlow("keyAsAnExpressionThatChangesFlow");
        MuleEvent testEvent = getTestEvent("osValue");
        testEvent.setFlowVariable("keyVariable", "test");
        flow.process(testEvent);
        assertEquals("existentValue", os.retrieve("test"));
        assertEquals("osValue", os.retrieve("changedKey"));

        checkNotifications("existentValue", "osValue");
    }

    /**
     * Scenario: The flow throws an exception then the watermark is not updated.
     */
    @Test
    public void flowFailsThenWatermarkIsNotUpdated() throws Exception
    {
        ObjectStore os = getDefaultObjectStore();
        if (os.contains("test"))
        {
            os.remove("test");
        }
        os.store("test", "existentValue");

        Flow flow = getFlow("failingFlow");
        try
        {
            flow.process(getTestEvent("osValue"));
        }
        catch (TransformerMessagingException e)
        {
            assertEquals("existentValue", os.retrieve("test"));
            assertEquals(1, listener.getNotifications().size());
        }
    }

    private ObjectStore getDefaultObjectStore()
    {
        return muleContext.getRegistry().get(MuleProperties.DEFAULT_USER_OBJECT_STORE_NAME);
    }

    private ObjectStore getObjectStore(String osName)
    {
        return muleContext.getRegistry().get(osName);
    }

    private Flow getFlow(String interceptorFlow)
    {
        return (Flow) muleContext.getRegistry().lookupFlowConstruct(interceptorFlow);
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/test/integration/watermark-interceptor-flow.xml";
    }


    private void checkNotifications(String initialValue, String finalValue)
    {
        CustomEventNotification initializeEvent = listener.getNotifications().poll();
        assertEquals("Watermark Initialized", initializeEvent.getName());
        assertEquals(initialValue, initializeEvent.getMetaDatas().get("value"));

        CustomEventNotification modifyEvent = listener.getNotifications().poll();
        assertEquals("Watermark Modified", modifyEvent.getName());
        assertEquals(finalValue, modifyEvent.getMetaDatas().get("value"));
    }

    private class WatermarkNotificationListener implements CustomNotificationListener<CustomEventNotification>
    {
        Queue<CustomEventNotification> notifications = new ArrayQueue<CustomEventNotification>();

        @Override
        public void onNotification(ServerNotification notification)
        {
            notifications.add((CustomEventNotification) notification);
        }

        public Queue<CustomEventNotification> getNotifications()
        {
            return notifications;
        }
    }
}
