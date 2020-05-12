package com.example.shahk.homework_05;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsArticleAdapter extends ArrayAdapter<NewsArticle>{

    public NewsArticleAdapter(@NonNull Context context, int resource, @NonNull List<NewsArticle> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        NewsArticle article = getItem(position);
        Log.d("Kush",article.toString());

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item,parent,false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView author = (TextView) convertView.findViewById(R.id.tv_Author);
        TextView date = (TextView) convertView.findViewById(R.id.tv_date);


        ImageView newsImage = (ImageView)convertView.findViewById(R.id.imageView);

        if(!article.urlToImage.equals("null")){
            Picasso.get().load(article.urlToImage).into(newsImage);

        }

        title.setText(article.title);
        author.setText(article.author);
        date.setText(article.publishedAt.substring(0,10));

        return convertView;

    }
}
