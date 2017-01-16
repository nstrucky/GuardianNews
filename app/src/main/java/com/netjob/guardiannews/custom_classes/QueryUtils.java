package com.netjob.guardiannews.custom_classes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import java.net.URL;
import java.util.List;

/**
 * Created by root on 1/15/17.
 */

public class QueryUtils {

    private static final String BASE_URL = "https://content.guardianapis.com/search?";
    private static final String PARAM_APIKEY = "api-key";
    private static final String PARAM_QUERY = "q";
    private static final String PARAM_SECTION = "section";
    private static final String PARAM_ORDER_BY = "order-by";
    private static final String PARAM_SHOWFIELDS = "show-fields";
    private static final String apiKey = "e73a902b-926f-478b-b828-10bb9d32c833";
    private static final String fieldsToInclude = "thumbnail,byline,bodyText";

    public static URL buildSectionUrl(/*Context context,*/ String sectionId, String orderBy) {

        URL url;
        Uri uri;

            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_SECTION, sectionId)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .build();


        return null;
    }

    public static URL buildSearchUrl(String sectionId, String searchQuery, String orderBy) {

        URL url;
        Uri uri;

        if (sectionId !=null) {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, searchQuery)
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_SECTION, sectionId)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .build();

        } else {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_QUERY, searchQuery)
                    .appendQueryParameter(PARAM_APIKEY, apiKey)
                    .appendQueryParameter(PARAM_ORDER_BY, orderBy)
                    .appendQueryParameter(PARAM_SHOWFIELDS, fieldsToInclude)
                    .build();
        }



        return null;
    }


    public static List<NewsItem> makeHttpUrlRequest() {




        return null;
    }





}
