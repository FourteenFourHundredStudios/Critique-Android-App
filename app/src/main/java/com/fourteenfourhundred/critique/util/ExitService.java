package com.fourteenfourhundred.critique.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ExitService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        this.stopSelf();
    }

}
