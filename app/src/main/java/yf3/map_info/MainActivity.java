package yf3.map_info;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    private NavController mNavController;

    private final NavigationBarView.OnItemSelectedListener mOnItemSelectedListener = (item -> {
        // TODO: arguments
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return true;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        mNavController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(mOnItemSelectedListener);
        NavigationUI.setupWithNavController(bottomNav, mNavController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
