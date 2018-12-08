package com.shadow649.hardwareshop.service;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.GenericEventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Emanuele Lombardi
 */
@Service
public class AxonNotificationService implements NotificationService {

    private final EventBus eventBus;

    @Autowired
    public AxonNotificationService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public void notify(Object event) {
        eventBus.publish(GenericEventMessage.asEventMessage(event));
    }
}
