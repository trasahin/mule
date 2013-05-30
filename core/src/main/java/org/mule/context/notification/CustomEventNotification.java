package org.mule.context.notification;


import org.mule.api.MuleEvent;
import org.mule.api.context.notification.BlockingServerEvent;
import org.mule.api.processor.MessageProcessor;

import java.util.Collections;
import java.util.Map;

/**
 * A Custom event notification represents an event in a particular flow. That event has a name as identifier
 * and metadata to notify some attributes of the particular event.
 *
 * @see org.mule.interceptor.WatermarkInterceptor#process(org.mule.api.MuleEvent) as a usage example.
 */
public class CustomEventNotification extends CustomNotification implements BlockingServerEvent
{

    static
    {
        registerAction("custom event", CustomEventNotification.CUSTOM_EVENT_EVENT_ACTION);
    }

    public static final int CUSTOM_EVENT_EVENT_ACTION = 120000;

    private static final long serialVersionUID = 1L;

    /**
     * The message processor that fired the custom event.
     */
    protected MessageProcessor processor;

    /**
     * The name of the event. it is used as an identifier
     */
    protected String name;

    /**
     * Metadata of the event.
     * Note: keep the name of metaDatas for API backward compatibility
     */
    protected Map<String, String> metaDatas;

    public CustomEventNotification(final MuleEvent source, final MessageProcessor processor, final String name, final Map<String, String> metaDatas)
    {
        super(source, CustomEventNotification.CUSTOM_EVENT_EVENT_ACTION);

        this.processor = processor;
        this.name = name;
        this.metaDatas = metaDatas;
    }

    @Override
    public MuleEvent getSource()
    {
        return (MuleEvent) super.getSource();
    }

    public MessageProcessor getProcessor()
    {
        return this.processor;
    }

    public String getName()
    {
        return this.name;
    }

    public Map<String, String> getMetaDatas()
    {
        return Collections.<String, String>unmodifiableMap(this.metaDatas);
    }
}
