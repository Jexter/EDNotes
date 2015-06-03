package myapps.alex.se.ednotes.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.fragments.NavigationDrawerFragment;
import myapps.alex.se.ednotes.fragments.SystemListFragment;
import myapps.alex.se.ednotes.persistence.Storage;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SystemListFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        getSupportActionBar().setTitle(Utils.getTitleWithFont(this, mTitle));

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));


        //Storage.resetPrefsProperty(AppConstants.SORT_TYPE);
    }

    @Override
    public void onResume() {
        super.onResume();

        Storage.exposeData();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, SystemListFragment.newInstance()).commit();
                mTitle = getString(R.string.title_systems);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(Utils.getTitleWithFont(this, mTitle));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(menu != null) {
            menu.clear();
        }

        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
