package org.chpir.android.roster;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ParticipantDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ParticipantDetailsActivity";
    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private String[] mHeaders;
    private int currentMenuItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_participant_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mHeaders = getIntent().getStringArrayExtra("RosterHeaders");
        mDrawer = (DrawerLayout) findViewById(R.id.roster_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.roster_drawer_view);

        setNavigationViewMenu();
        setNavigationViewListener(navigationView);
    }

    private void setNavigationViewMenu() {
        Menu menu = navigationView.getMenu();
        for (int k = 0; k < mHeaders.length; k++) {
            menu.add(R.id.drawer_menu_group, k, Menu.NONE, mHeaders[k]).setShowAsAction(MenuItem
                    .SHOW_AS_ACTION_ALWAYS);
        }
        menu.getItem(currentMenuItem).setChecked(true);
    }

    private void setNavigationViewListener(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        unCheckAllMenuItems(navigationView);
                        menuItem.setChecked(true);
                        mDrawer.closeDrawers();
                        updateFragment(menuItem.getItemId());
                        return true;
                    }
                });
    }

    private void unCheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                item.setChecked(false);
            }
        }
    }

    private void updateFragment(int position) {
        RosterItemFragment fragment = new RosterItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("FragmentText", mHeaders[position]);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.roster_item_container) == null) {
            fragmentManager.beginTransaction().add(R.id.roster_item_container, fragment).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.roster_item_container, fragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RosterItemFragment fragment = new RosterItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("FragmentText", mHeaders[0]);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.roster_item_container) == null) {
            fragmentManager.beginTransaction().add(R.id.roster_item_container, fragment).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.roster_item_container, fragment)
                    .commit();
        }
    }

}