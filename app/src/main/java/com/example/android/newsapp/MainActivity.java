package com.example.android.newsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout navDrawer;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up navigation drawer menu
        navDrawer = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        buildNavMenu(navView.getMenu());

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        navigationAction(menuItem);
                        return true;
                    }
                }
        );

        //Toolbar setup
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.navDefaultLabel);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        fragmentManager = getSupportFragmentManager();

        //Load default news list
        if (savedInstanceState == null) {
            loadFragment(getString(R.string.navDefaultLabel), R.id.navDefault);
            navView.setCheckedItem(R.id.navDefault);
        } else {
            actionBar.setTitle(savedInstanceState.getString("actionBarTitle"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("actionBarTitle", actionBar.getTitle().toString());
        super.onSaveInstanceState(outState);
    }

    private void navigationAction(MenuItem item) {
        if (item.getItemId() == R.id.navDefault) {
            actionBar.setTitle(item.getTitle());
            loadFragment(getString(R.string.navDefaultLabel), item.getItemId());
            navDrawer.closeDrawer(GravityCompat.START);
        } else {
            actionBar.setTitle(item.getTitle());
            loadFragment(item.getTitle().toString(), item.getItemId());
            navDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(String section, int loaderId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentFrame, SectionFragment.newInstance(section.toLowerCase(), loaderId));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Add additional menu items
    private void buildNavMenu(Menu menu) {
        int groupId = R.id.sections;
        String[] topics = getResources().getStringArray(R.array.TOPICS);
        for (int i = 0; i < topics.length; i++) {
            menu.add(groupId, i, Menu.NONE, topics[i]);
        }
        menu.setGroupCheckable(R.id.sections, true, true);
    }
}
