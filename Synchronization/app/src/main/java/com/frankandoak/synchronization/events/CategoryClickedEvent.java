package com.frankandoak.synchronization.events;

import com.frankandoak.synchronization.models.RemoteCategory;

/**
 * Created by mj_eilers on 15-02-19.
 */
public class CategoryClickedEvent {

    private RemoteCategory mCategory;


    public CategoryClickedEvent(RemoteCategory category) {
        this.mCategory = category;
    }

    public RemoteCategory getCategory() {
        return mCategory;
    }
}
