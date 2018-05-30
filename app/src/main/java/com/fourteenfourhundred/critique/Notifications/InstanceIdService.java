package com.fourteenfourhundred.critique.Notifications;




    import android.util.Log;

    import com.fourteenfourhundred.critique.Framework.API.API;
    import com.fourteenfourhundred.critique.Framework.API.ApiRequest;
    import com.fourteenfourhundred.critique.storage.Data;
    import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


    public class InstanceIdService extends FirebaseInstanceIdService {
        public InstanceIdService() {
            super();
        }

        @Override
        public void onTokenRefresh() {
            super.onTokenRefresh();
            String token = FirebaseInstanceId.getInstance().getToken();

            API api = Data.backgroundApi;
            new ApiRequest.setNotificationKey(api,token).execute(__ -> {
                Log.e("Notification key","Server received notification key");
            });
        }


}
