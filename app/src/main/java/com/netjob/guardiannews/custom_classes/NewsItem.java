package com.netjob.guardiannews.custom_classes;

import android.graphics.Bitmap;

/**
 * Created by root on 1/15/17.
 */

public class NewsItem {


    Bitmap mThumbnailBitmap; //fields --> thumbnail
    Bitmap mAuthorPhoto; //tags --> bylineImageUrl
    String mArticleTitle; //webTitle
    String mArticleBody;// fields --> body
    String mArticleUrl; //webUrl
    String mAuthorName; //fields --> byline
//    String mAuthorBio; //tags --> bio



    public NewsItem(Bitmap thumbnailBitmap, Bitmap authorPhoto, String articleTitle,
                    String articleUrl, String articleBody, String authorName) {

        mThumbnailBitmap = thumbnailBitmap;
        mAuthorPhoto = authorPhoto;
        mArticleTitle = articleTitle;
        mArticleBody = summarizeArticleBody(articleBody);
        mArticleUrl = articleUrl;
        mAuthorName = authorName;

    }


    private String summarizeArticleBody(String body) {

        //TODO create logic to cut string down to manageable size
        return null;
    }

    public Bitmap getThumbnailBitmap() {
        return mThumbnailBitmap;
    }

    public void setThumbnailbitmap(Bitmap thumbnailBitmap) {
        mThumbnailBitmap = thumbnailBitmap;
    }

    public Bitmap getAuthorPhoto() {
        return mAuthorPhoto;
    }

    public void setAuthorPhoto(Bitmap authorPhoto) {
        mAuthorPhoto = authorPhoto;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        mArticleTitle = articleTitle;
    }

    public String getArticleBody() {
        return mArticleBody;
    }

    public void setArticleBody(String articleBody) {
        mArticleBody = articleBody;
    }

    public String getArticleUrl() {
        return mArticleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        mArticleUrl = articleUrl;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

//    public String getmAuthorBio() {
//        return mAuthorBio;
//    }
//
//    public void setAuthorBio(String authorBio) {
//        mAuthorBio = authorBio;
//    }


}
