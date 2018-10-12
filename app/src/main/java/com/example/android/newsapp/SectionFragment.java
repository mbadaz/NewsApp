package com.example.android.newsapp;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link android.support.v4.app.Fragment } subclass.
 */
public class SectionFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks {
    private static final String ARG_PARAM1 = "section";
    private static final String ARG_PARAM2 = "loaderId";
    private static final String GUARDIAN_API_BASE_URL = "https://content.guardianapis.com/";
    private static final String API_KEY = "2975dac6-df2a-4b49-86de-000fa4f28eb5";
    private ArrayList<Article> articles;
    private String section;
    private int loaderId;
    private RecyclerView recyclerView;
    private ProgressBar loadingSpinner;
    private TextView notification;
    private boolean loaded;

    //Item click listener for the RecyclerView
    private final mRecyclerViewAdapater.OnItemClickListener clickListener = new mRecyclerViewAdapater.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Article article = articles.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getArticleUrl()));
            startActivity(intent);
        }
    };

    public SectionFragment() {
        // Required empty public constructor
    }

    public static SectionFragment newInstance(String section, int loaderId) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, section);
        args.putInt(ARG_PARAM2, loaderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            section = getArguments().getString(ARG_PARAM1);
            loaderId = getArguments().getInt(ARG_PARAM2);
        }

        if(savedInstanceState != null){
            loaded = savedInstanceState.getBoolean("loaded");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.articleRecylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        notification = view.findViewById(R.id.notificaton);
        loadingSpinner = view.findViewById(R.id.loadingSpinner);

        //Initialise loader
        Loader loader = LoaderManager.getInstance(getActivity()).initLoader(loaderId,null, this);
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (loaded){
            //if loader has already loaded load the cached data
            loader.startLoading();
        }else if(networkInfo != null && networkInfo.isConnected()){
            //or else fetch new data from the net if there is network
            loader.forceLoad();
        }else {
            notification.setText(R.string.noNetworkNotice);
            loadingSpinner.setVisibility(View.GONE);
        }

    }

    //Override LoaderManager callbacks

    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Uri.parse(GUARDIAN_API_BASE_URL + section);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter("show-fields", "byline,trailText");
        builder.appendQueryParameter("page-size", "15");
        builder.appendQueryParameter("api-key", API_KEY);
        return new NewsLoader(getContext(), builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {
                        loadingSpinner.setVisibility(View.GONE);
        if (data !=  null) {
            articles = (ArrayList<Article>) data;
            recyclerView.setAdapter(new mRecyclerViewAdapater(articles, clickListener));
            loaded = true;
        } else {
            notification.setText(R.string.noNewsNotice);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        loader.reset();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("loaded", loaded);
        super.onSaveInstanceState(outState);
    }
}
