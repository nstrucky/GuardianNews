package com.netjob.guardiannews.custom_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/15/17.
 */

public class QueryUtils {

    private static final String LOG_TAG = "Query Utilities";

    private static final String BASE_URL = "https://content.guardianapis.com/search?";
    private static final String PARAM_APIKEY = "api-key";
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_SECTION = "section";
    private static final String PARAM_ORDER_BY = "order-by";
    private static final String PARAM_SHOWFIELDS = "show-fields";
    private static final String PARAM_SHOWTAGS = "show-tags";
    private static final String apiKey = "e73a902b-926f-478b-b828-10bb9d32c833";
    private static final String fieldsToInclude = "thumbnail,byline,bodyText";
    private static final String tagsToInclude = "contributor";

    public static URL buildSectionUrl(String sectionId, String orderBy) {

        URL url = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_SECTION, sectionId)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .appendQueryParameter(PARAM_SHOWTAGS, tagsToInclude)
                    .build();

        try {
            url = new URL(uri.toString());

        } catch (IOException e) {
            Log.e(LOG_TAG, "buildSectionUrl()", e);
        }

        return url;
    }

    public static URL buildSearchUrl(String sectionId, String searchQuery, String orderBy) {

        URL url = null;
        Uri uri;

        if (sectionId !=null) {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, searchQuery)
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_SECTION, sectionId)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .appendQueryParameter(PARAM_SHOWTAGS, tagsToInclude)
                    .build();

        } else {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, searchQuery)
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .appendQueryParameter(PARAM_SHOWTAGS, tagsToInclude)
                    .build();
        }

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "buildSearchUrl()", e);
        }

        return url;
    }

    public static List<NewsItem> makeHttpUrlRequest(URL url) {

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String jsonToParse = "";
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {

                inputStream = httpURLConnection.getInputStream();
                jsonToParse = readFromInputStream(inputStream);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "makeHttpUrlRequest()", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "readFromInputStream()", e);
                }
            }
        }

        return fetchNewsItemData(jsonToParse);
    }

    private static String readFromInputStream(InputStream inputStream) {

        String jsonToParse = "";

        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream == null) {
            return jsonToParse;

        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");

            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "readFromInputStream()", e);
        }

        jsonToParse = stringBuilder.toString();
        Log.i(LOG_TAG, jsonToParse);

        return jsonToParse;

    }

    private static List<NewsItem> fetchNewsItemData(String jsonToParse) {

        List<NewsItem> newsItems = new ArrayList<>();

        try {
            JSONArray results = new JSONObject(jsonToParse).getJSONObject("response").getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                String authorPhotoUrl = null;
                String authorName;

                JSONObject newsItem = results.getJSONObject(i);
                JSONObject fields = newsItem.getJSONObject("fields");

                if (!newsItem.isNull("tags") && newsItem.has("tags")) {
                    JSONArray tagsArray = newsItem.getJSONArray("tags");
                    if (tagsArray.length() > 0) {
                        JSONObject tags = tagsArray.getJSONObject(0);
                        if (tags.has("bylineImageUrl") && !tags.isNull("bylineImageUrl")) {
                            authorPhotoUrl = tags.getString("bylineImageUrl");
                        } else {
                            authorPhotoUrl = null;
                        }
                    }
                }



                String thumbnailString = fields.getString("thumbnail");
                String articleBody = fields.getString("bodyText");

                if (fields.has("byline") && !fields.isNull("byline")) {
                    authorName = fields.getString("byline");
                } else {
                    authorName = null;
                }

                String articleTitle = newsItem.getString("webTitle");
                String articleUrl = newsItem.getString("webUrl");
                String publicationDate = newsItem.getString("webPublicationDate");

                Bitmap thumbnail = getImageBitmap(thumbnailString);
                Bitmap authorPhoto = getImageBitmap(authorPhotoUrl);

                NewsItem listNewsItem = new NewsItem(thumbnail, authorPhoto, articleTitle,
                        articleUrl, articleBody, authorName, publicationDate);

                newsItems.add(listNewsItem);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "fetchNewsItemData()", e);
        }

        return newsItems;
    }

    private static Bitmap getImageBitmap(String imageUrl) {

        Bitmap imageBitmap = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        if (imageUrl == null) {
            return null;
        }

        try {
            URL url = new URL(imageUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                imageBitmap = BitmapFactory.decodeStream(inputStream);
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "getImageBitmap()", e);

        } catch (IOException e) {
            Log.e(LOG_TAG, "getImageBitmap()", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "getImageBitmap()", e);
                }
            }
        }

        return imageBitmap;
    }




}
