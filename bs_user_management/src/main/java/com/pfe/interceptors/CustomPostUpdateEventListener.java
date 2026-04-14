package com.pfe.interceptors;


import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;



@Component
public class CustomPostUpdateEventListener implements PostUpdateEventListener {


    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {

    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister entityPersister) {
        return false;
    }
}
