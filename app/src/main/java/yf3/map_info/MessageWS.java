package yf3.map_info;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MessageWS {
    private OkHttpClient client;

    public MessageWS() {
        client = new OkHttpClient();
    }

    private final class MessageWebSocketListener extends WebSocketListener {
        private static final int CLOSE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("message", "ready");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            webSocket.send(obj.toString());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            testOutput("Receiving: " + text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(CLOSE_STATUS, null);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        }
    }

    public void run() {
        Request request = new Request.Builder().url(Configs.WEBSOCKET_URL).build();
        MessageWebSocketListener listener = new MessageWebSocketListener();
        WebSocket webSocket = client.newWebSocket(request, listener);
    }

    public void testOutput(final String text) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });
    }

    public void onClosing() {
        client.dispatcher().executorService().shutdown();
    }
}
