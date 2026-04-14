package com.pfe.interceptors;


import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;


@Component
public class CustomPostInsertEventListener implements PostInsertEventListener {

    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {

    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
