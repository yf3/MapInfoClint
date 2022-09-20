package csie.ndhu.mapInfo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    openActivity("csie.ndhu.mapInfo.MainActivity");
                    return true;
                case R.id.navigation_maps:
                    return true;
                case R.id.navigation_camera:
                    openActivity("csie.ndhu.mapInfo.CameraActivity");
                    return true;
                case R.id.navigation_gallery:
                    openActivity("csie.ndhu.mapInfo.Gallery");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
