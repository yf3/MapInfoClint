package csie.ndhu.mapInfo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_camera:
                    openActivity("csie.ndhu.mapInfo.CameraActivity");
                    return true;
                default:
                    NavigationUI.onNavDestinationSelected(item, navController);
                    return true;
            }
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

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
