// NetworkChangeReceiver.java
package com.appreman.app.Email;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnectedToInternet(context)) {
            Log.d("NetworkChangeReceiver", "Conexión a Internet restablecida");
            MailSender mailSender = new MailSender();
            mailSender.retryPendingEmails(context);
        } else {
            Log.d("NetworkChangeReceiver", "Conexión a Internet perdida");
        }
    }

    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
