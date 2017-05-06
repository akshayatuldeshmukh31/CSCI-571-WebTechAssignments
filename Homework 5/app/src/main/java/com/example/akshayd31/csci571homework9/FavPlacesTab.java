package com.example.akshayd31.csci571homework9;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by akshayd31 on 4/18/17.
 */
public class FavPlacesTab extends Fragment {

    View view;

    public RecyclerView mRecyclerView;
    public FavoriteAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public TextView mNoRecordsTextView;
    public ProgressBar mProgressBar;
    public LinearLayout mMainLinearLayout;
    public JSONArray resultData = null;
    public ArrayList<Bitmap> resultImages = null;
    public ArrayList<String> resultImageUrls = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.favorite_layout, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.favorite_progressbar);
        mMainLinearLayout = (LinearLayout) view.findViewById(R.id.favorite_content);
        mNoRecordsTextView = (TextView) view.findViewById(R.id.favs_no_records_text);

        if(resultData==null || resultImages==null || resultImageUrls==null)
            new GetFavoriteDataFromServer().execute();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(resultData!=null && resultImages!=null && resultImageUrls!=null){
            mAdapter = new FavoriteAdapter("place", resultData, resultImages, resultImageUrls, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    public class GetFavoriteDataFromServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            mMainLinearLayout.setVisibility(View.GONE);
            mNoRecordsTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            mAdapter = new FavoriteAdapter("place", resultData, resultImages, resultImageUrls, getContext());
            mRecyclerView.setAdapter(mAdapter);
            if(resultData!=null && resultImages!=null && resultImageUrls!=null) {
                mProgressBar.setVisibility(View.GONE);
                mMainLinearLayout.setVisibility(View.VISIBLE);
            }
            else{
                mProgressBar.setVisibility(View.GONE);
                mNoRecordsTextView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            resultData = NetworkUtils.getFavoriteDataFromSharedPreferences("place", getContext());
            resultImages = NetworkUtils.getFavoriteImagesFromServer(resultData);
            resultImageUrls = NetworkUtils.getFavoriteImageUrlsFromServer(resultData);

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onResume() {
        new GetFavoriteDataFromServer().execute();
        super.onResume();
    }
}
