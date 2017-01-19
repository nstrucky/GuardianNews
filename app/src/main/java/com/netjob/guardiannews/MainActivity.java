package com.netjob.guardiannews;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.netjob.guardiannews.custom_classes.NewsItem;
import com.netjob.guardiannews.custom_classes.NewsItemAdapter;
import com.netjob.guardiannews.custom_classes.QueryUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<NewsItem>> {


    protected static String mUserSearchInput = null;
    private ImageButton mSearchButton;
    private Button mTryAgainButton;
    private EditText mSearchBox;
    private ProgressBar mProgressBar;
    private TextView mEmptyListText;
    private ListView mNewsItemListView;
    private NewsItemAdapter mNewsItemAdapter;
    private List<NewsItem> mNewsItems;

    protected static final String SECTION_ID_KEY = "sectionIdKey";

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

        mSearchButton = (ImageButton) findViewById(R.id.image_button_search);
        mTryAgainButton = (Button) findViewById(R.id.button_tryAgain_general);
        mSearchBox = (EditText) findViewById(R.id.editText_general_search);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_general);
        mEmptyListText = (TextView) findViewById(R.id.textView_emptyList_general);
        mNewsItemListView = (ListView) findViewById(R.id.listview_general);

        mNewsItems = new ArrayList<>();
        mNewsItemAdapter = new NewsItemAdapter(this, mNewsItems);

        mNewsItemListView.setEmptyView(mEmptyListText);
        mNewsItemListView.setAdapter(mNewsItemAdapter);

        startLoader(1);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        mNewsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOnlineArticle(position);
            }
        });

        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoader(2);
            }
        });

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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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
                sectionId = getString(R.string.artanddesign);
                break;

            case R.id.menu_item_business:
                sectionId = getString(R.string.business);
                break;

            case R.id.menu_item_culture:
                sectionId = getString(R.string.culture);
                break;

            case R.id.menu_item_education:
                sectionId = getString(R.string.education);
                break;

            case R.id.menu_item_film:
                sectionId = getString(R.string.film);
                break;

            case R.id.menu_item_us_news:
                sectionId = getString(R.string.us_news);
                break;

            case R.id.menu_item_world_news:
                sectionId = getString(R.string.world);
                break;

            default:
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

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoaderGeneral(this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {

        mNewsItemAdapter.clear();
        mProgressBar.setVisibility(View.GONE);
        mEmptyListText.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mNewsItemAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {

    }

    private static class NewsLoaderGeneral extends AsyncTaskLoader<List<NewsItem>> {

        public NewsLoaderGeneral(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public List<NewsItem> loadInBackground() {

            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            String orderByPref = sharedPreferences.getString(
                    getContext().getString(R.string.settings_order_by_key), "");

            URL url;
            if (mUserSearchInput !=null) {
                url = QueryUtils.buildSearchUrl(null, mUserSearchInput, orderByPref);
                mUserSearchInput = null;
            } else {
                url = QueryUtils.buildSectionUrl();
            }

            return QueryUtils.makeHttpUrlRequest(url);
        }
    }

    private void search() {
        mNewsItemAdapter.clear();
        mUserSearchInput = mSearchBox.getText().toString();
        hideSoftKeyboard();
        startLoader(2);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void goToOnlineArticle(int position) {
        NewsItem currentNewsItem = (NewsItem) mNewsItemAdapter.getItem(position);
        String urlString = currentNewsItem.getArticleUrl();
        Uri uri = Uri.parse(urlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void startLoader(int loadMethod) {


        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
            mEmptyListText.setVisibility(View.INVISIBLE);
            mTryAgainButton.setVisibility(View.GONE);
            switch (loadMethod) {

                case 1:
                    getLoaderManager().initLoader(0, null, this);

                    break;

                case 2:
                    mProgressBar.setVisibility(View.VISIBLE);
                    getLoaderManager().restartLoader(0, null, this);
                    break;

            }

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyListText.setText(getString(R.string.no_internet_connection));

        }

    }
}
