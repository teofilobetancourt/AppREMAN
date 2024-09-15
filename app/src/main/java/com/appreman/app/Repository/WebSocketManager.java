    package com.appreman.app.Repository;

    import android.util.Log;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.appreman.appreman.R;

    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.Response;
    import okhttp3.WebSocket;
    import okhttp3.WebSocketListener;

    public class WebSocketManager {

        private static final String TAG = "WebSocketManager";
        private static final String SERVER_URL = "ws://192.168.3.126:8080";

        private OkHttpClient client;
        private WebSocket webSocket;
        private NotificationListener listener;
        private int notificationCount = 0;

        public interface NotificationListener {
            void onNotificationReceived(int count);
        }


        public WebSocketManager(NotificationListener listener) {
            this.listener = listener;
        }

        public void start() {
            client = new OkHttpClient();
            Request request = new Request.Builder().url(SERVER_URL).build();
            webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    Log.d(TAG, "WebSocket connected");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    Log.d(TAG, "Message received: " + text);
                    notificationCount++;
                    if (listener != null) {
                        listener.onNotificationReceived(notificationCount);
                    }
                    updateNotificationIcon();
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    Log.e(TAG, "WebSocket connection failed", t);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    Log.d(TAG, "WebSocket closed: " + reason);
                }
            });
        }

        public void sendNotification(String message) {
            if (webSocket != null) {
                webSocket.send(message);
            } else {
                Log.e(TAG, "WebSocket is not connected");
            }
        }

        private void updateNotificationIcon() {
            if (listener != null) {
                // The listener should handle UI updates
                listener.onNotificationReceived(notificationCount);
            }
        }

        public void close() {
            if (webSocket != null) {
                webSocket.close(1000, "Goodbye!");
            }
        }
    }
