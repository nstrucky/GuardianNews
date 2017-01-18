package com.netjob.guardiannews.custom_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netjob.guardiannews.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by root on 1/15/17.
 */

public class NewsItemAdapter extends ArrayAdapter {


    public NewsItemAdapter(Context context, List<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        NewsItem currentNewsItem = (NewsItem) getItem(position);
        Bitmap authorPhoto = currentNewsItem.getAuthorPhoto();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_article, null, false);

            viewHolder.mArticleTitleTextView = (TextView) convertView.findViewById(R.id.textView_article_title);
            viewHolder.mPublicationTextView = (TextView) convertView.findViewById(R.id.textView_publicationDate);
            viewHolder.mArticleBodyTextView = (TextView) convertView.findViewById(R.id.textView_article_body);
            viewHolder.mBylineTextView = (TextView) convertView.findViewById(R.id.textView_byline);
            viewHolder.mBylineImageView = (ImageView) convertView.findViewById(R.id.imageView_bylineImage);
            viewHolder.mArticleThumbnailImageView = (ImageView) convertView.findViewById(R.id.imageView_article_thumbnail);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.mArticleTitleTextView.setText(currentNewsItem.getArticleTitle());
        viewHolder.mPublicationTextView.setText(currentNewsItem.getPublicationDate());
        viewHolder.mArticleBodyTextView.setText(currentNewsItem.getArticleBody());

        viewHolder.mBylineTextView.setText(currentNewsItem.getAuthorName());

        if (authorPhoto != null) {
            viewHolder.mBylineImageView.setImageBitmap(authorPhoto);
        } else {
            viewHolder.mBylineImageView.setImageResource(R.drawable.ic_assignment_ind_black_24dp);
        }
        viewHolder.mArticleThumbnailImageView.setImageBitmap(currentNewsItem.getThumbnailBitmap());



        return convertView;
    }
}
