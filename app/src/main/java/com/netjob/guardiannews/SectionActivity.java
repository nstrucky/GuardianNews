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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * Created by root on 1/15/17.
 */

public class SectionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    protected static String mSectionId;
    protected static String mUserSearchInput = null;
    private ListView mNewsListView;
    private List<NewsItem> mNewsItems;
    private NewsItemAdapter mNewsItemAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mSearchButton;
    private Button mTryAgainButton;
    private EditText mSearchBox;
    private TextView mEmptyListText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_section);
        mSearchButton = (ImageButton) findViewById(R.id.image_button_section_search);
        mSearchBox = (EditText) findViewById(R.id.editText_section_search);
        mEmptyListText = (TextView) findViewById(R.id.textView_emptyList);
        mTryAgainButton = (Button) findViewById(R.id.button_tryAgain);
        mNewsListView = (ListView) findViewById(R.id.listview_sections);
        mNewsItems = new ArrayList<>();

        mNewsItemAdapter = new NewsItemAdapter(this, mNewsItems);
        mNewsListView.setAdapter(mNewsItemAdapter);
        mNewsListView.setEmptyView(mEmptyListText);

        mSectionId = getIntent().getExtras().getString(MainActivity.SECTION_ID_KEY);
        if (mSectionId != null) {
            getSupportActionBar().setTitle(getActivityTitleString(mSectionId));

        }

        startLoader(1);

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToOnlineArticle(position);
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
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
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {

        mProgressBar.setVisibility(View.GONE);
        mEmptyListText.setVisibility(View.GONE);
        mNewsItemAdapter.clear();
        if (newsItems != null) {
            mNewsItemAdapter.addAll(newsItems);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {

    }

    private static class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

        public NewsLoader(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public List<NewsItem> loadInBackground() {

            URL url;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String orderByPref = sharedPreferences.getString(getContext().getString(R.string.settings_order_by_key), "");

            if (mUserSearchInput != null) {
                url = QueryUtils.buildSearchUrl(mSectionId, mUserSearchInput, orderByPref);
                mUserSearchInput = null;
            } else {
                //if no search is performed it would not make sense to organize by relevance
                //so this defaults to newest
                url = QueryUtils.buildSectionUrl(mSectionId,
                        getContext().getString(R.string.settings_order_by_default));
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

    private void goToOnlineArticle(int position) {
        NewsItem currentNewsItem = (NewsItem) mNewsItemAdapter.getItem(position);
        String urlString = currentNewsItem.getArticleUrl();
        Uri uri = Uri.parse(urlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

    private String getActivityTitleString(String sectionId) {

        String sectionTitle;

        if (sectionId.equals(getString(R.string.world))) {
            sectionTitle = getString(R.string.world_title);

        } else if (sectionId.equals(getString(R.string.us_news))) {
            sectionTitle = getString(R.string.us_news_title);

        } else if (sectionId.equals(getString(R.string.film))) {
            sectionTitle = getString(R.string.film_title);

        } else if (sectionId.equals(getString(R.string.education))) {
            sectionTitle = getString(R.string.education_title);

        } else if (sectionId.equals(getString(R.string.culture))) {
            sectionTitle = getString(R.string.culture_title);

        } else if (sectionId.equals(getString(R.string.business))) {
            sectionTitle = getString(R.string.business_title);

        } else if (sectionId.equals(getString(R.string.artanddesign))){
            sectionTitle = getString(R.string.artanddesign_title);

        } else {
            sectionTitle = getString(R.string.section_title_default);
        }
            return sectionTitle;
    }

}
