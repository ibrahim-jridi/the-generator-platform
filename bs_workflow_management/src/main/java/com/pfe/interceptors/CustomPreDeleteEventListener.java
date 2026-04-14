package com.pfe.interceptors;


import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.springframework.stereotype.Component;


@Component
public class CustomPreDeleteEventListener implements PreDeleteEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent preDeleteEvent) {
        return true;
    }
}
