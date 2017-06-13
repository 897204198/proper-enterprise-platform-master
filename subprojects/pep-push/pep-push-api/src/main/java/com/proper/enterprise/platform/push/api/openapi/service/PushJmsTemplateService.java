package com.proper.enterprise.platform.push.api.openapi.service;

/**
 * 对JmsTemplate的代理服务
 * Created by shen on 2017/5/12.
 */
public interface PushJmsTemplateService {
    /**
     * Send the given object to the specified destination, converting the object
     * to a JMS message with a configured MessageConverter.
     *
     * @param destinationName the name of the destination to send this message to
     *                        (to be resolved to an actual destination by a DestinationResolver)
     * @param message         the object to convert to a message
     */
    void saveConvertAndSend(String destinationName, final Object message);
}
