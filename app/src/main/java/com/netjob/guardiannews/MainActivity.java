package com.netjob.guardiannews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String SECTION_ID_KEY = "sectionIdKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String sectionId;
        Intent intent;

        switch (id) {

            case R.id.menu_item_artanddesign:
                Toast.makeText(this, "art and design", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.artanddesign);
                break;

            case R.id.menu_item_business:
                Toast.makeText(this, "business", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.business);
                break;

            case R.id.menu_item_culture:
                Toast.makeText(this, "culture", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.culture);
                break;

            case R.id.menu_item_education:
                Toast.makeText(this, "education", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.education);
                break;

            case R.id.menu_item_film:
                Toast.makeText(this, "film", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.film);
                break;

            case R.id.menu_item_us_news:
                Toast.makeText(this, "us news", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.us_news);
                break;

            case R.id.menu_item_world_news:
                Toast.makeText(this, "world news", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.world);
                break;

            default:
                Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
                sectionId = getString(R.string.world);
                break;

        }

        intent = new Intent(this, SectionActivity.class);
        intent.putExtra(SECTION_ID_KEY, sectionId);
        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
