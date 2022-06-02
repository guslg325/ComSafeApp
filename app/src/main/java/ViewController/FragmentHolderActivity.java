package ViewController;

import android.os.Bundle;
import android.view.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.*;
import androidx.drawerlayout.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import LogicController.FragmentDrawer;
import LogicController.FragmentDrawer.FragmentDrawerListener;
import com.example.comsafe.R;

public class FragmentHolderActivity extends AppCompatActivity implements FragmentDrawerListener{
    private static String TAG = FragmentHolderActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Fragment fragment = new AboutFragment();
            String title = getString(R.string.tvTitleAbout);
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                // set the toolbar title
                getSupportActionBar().setTitle(title);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.tvTitleMainMenu);
        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                title = getString(R.string.tvTitleProfile);
                break;
            case 1:
                fragment = new MainMenuFragment();
                title = getString(R.string.tvTitleMainMenu);
                break;
            case 2:
                fragment = new VisitasFragment();
                title = getString(R.string.tvTitleVisitas);
                break;
            case 3:
                fragment = new ReservasFragment();
                title = getString(R.string.tvTitleReservas);
                break;
            case 4:
                fragment = new AutomovilesFragment();
                title = getString(R.string.tvTitleAutomoviles);
                break;
            case 5:
                fragment = new ReportesFragment();
                title = getString(R.string.tvTitleReportes);
                break;
            case 6:
                fragment = new QuejasFragment();
                title = getString(R.string.tvTitleQuejas);
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}
