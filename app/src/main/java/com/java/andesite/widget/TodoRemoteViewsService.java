package com.java.andesite.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class TodoRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TodoRemoteViewsFactory(this.getApplicationContext());
    }
}