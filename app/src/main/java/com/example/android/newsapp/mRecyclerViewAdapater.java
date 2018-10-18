package com.example.android.newsapp;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class mRecyclerViewAdapater extends RecyclerView.Adapter<mRecyclerViewAdapater.mViewHolder> {
    private final ArrayList<Article> articles;
    private final OnItemClickListener onItemClick;

    public mRecyclerViewAdapater(ArrayList<Article> articles, OnItemClickListener onItemClick) {
        this.articles = articles;
        this.onItemClick = onItemClick;
    }

    public class mViewHolder extends RecyclerView.ViewHolder {
        final TextView articleTitle;
        final TextView publishDate;
        final TextView topic;
        final TextView byLine;
        final TextView trailText;
        final ImageView thumbnail;

        mViewHolder(@NonNull View itemView) {
            super(itemView);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            publishDate = itemView.findViewById(R.id.articlePublishDate);
            topic = itemView.findViewById(R.id.articleTopic);
            byLine = itemView.findViewById(R.id.byLine);
            trailText = itemView.findViewById(R.id.trailText);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.article_item_view, viewGroup, false);

        return new mViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, final int i) {
        Article article = articles.get(i);
        mViewHolder.articleTitle.setText(article.getArticleTitle());
        mViewHolder.topic.setText(article.getArticleTopic());
        mViewHolder.trailText.setText(String.format("%s...", article.getTrailText()));
        mViewHolder.byLine.setText(String.format("By %s", article.getByLine()));
        GlideApp.with(mViewHolder.itemView.getContext()).
                load(makeUri(article.getThumbnailUrl())).centerCrop().
                diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).
                into(mViewHolder.thumbnail);
        mViewHolder.publishDate.setText(
                stringToDate(article.getArticlePublishDate()));
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private String stringToDate(String dateString) {
        String sDate = dateString.split("T")[0];
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(sDate);
            return SimpleDateFormat.getDateInstance().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return sDate;
        }
    }

    private Uri makeUri(String url){
            return Uri.parse(url);
    }
}
