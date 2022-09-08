package csie.ndhu.checkin;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Button mTempButton;
    private OkHttpClient client;
    private TextInputLayout mTextInputLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openActivity("csie.ndhu.checkin.ui.login.LoginActivity");
                    return true;
                case R.id.navigation_camera:
                    openActivity("csie.ndhu.checkin.CameraActivity");
                    return true;
                case R.id.navigation_gallery:
                    openActivity("csie.ndhu.checkin.Gallery");
                    return true;
            }
            return false;
        }
    };

    public void openActivity(String activityName) {
        Intent intent = null;
        try {
            intent = new Intent(this, Class.forName(activityName));
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    private final class MessageWebSocketListener extends WebSocketListener {
        private static final int CLOSE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("message", mTextInputLayout.getEditText().getText().toString());
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
            mTextMessage.setText("Error: " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextInputLayout = findViewById(R.id.temptemp);

        mTextMessage = (TextView) findViewById(R.id.message);
        mTempButton = findViewById(R.id.tempButton);
        mTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runWebSocket();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        client = new OkHttpClient();
    }

    private void runWebSocket() {
        Request request = new Request.Builder().url(getString(R.string.websocket_url)).build();
        MessageWebSocketListener listener = new MessageWebSocketListener();
        WebSocket webSocket = client.newWebSocket(request, listener);
    }
    private void testOutput(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextMessage.setText(mTextMessage.getText().toString() + "\n\n" + text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        client.dispatcher().executorService().shutdown();
        super.onDestroy();
    }
}
