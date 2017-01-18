package com.netjob.guardiannews;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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


    private final String LOG_TAG = "SectionActivity";

    static String mSectionId;
    ListView mNewsListView;
    List<NewsItem> mNewsItems;
    NewsItemAdapter mNewsItemAdapter;
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar_section);

        mNewsListView = (ListView) findViewById(R.id.listview_sections);
        mNewsItems = new ArrayList<>();

        mNewsItemAdapter = new NewsItemAdapter(this, mNewsItems);
        mNewsListView.setAdapter(mNewsItemAdapter);

        mSectionId = getIntent().getExtras().getString(MainActivity.SECTION_ID_KEY);
        if (mSectionId != null) {
            getSupportActionBar().setTitle(getActivityTitleString(mSectionId));

        }

        startLoader(1);

        mNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem currentNewsItem = (NewsItem) mNewsItemAdapter.getItem(position);
                String urlString = currentNewsItem.getArticleUrl();
                Uri uri = Uri.parse(urlString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }


    private void startLoader(int loadMethod) {

        switch (loadMethod) {

            case 1:
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    getLoaderManager().initLoader(0, null, this);
                }
                break;

            case 2:
                getLoaderManager().restartLoader(0, null, this);
                break;

        }

    }


    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {

        mProgressBar.setVisibility(View.GONE);
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
            URL url = QueryUtils.buildSectionUrl(mSectionId, "newest");
            return QueryUtils.makeHttpUrlRequest(url);
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
