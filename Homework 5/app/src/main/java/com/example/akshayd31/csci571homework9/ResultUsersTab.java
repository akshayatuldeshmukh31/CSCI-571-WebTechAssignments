package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshayd31 on 4/18/17.
 */
public class ResultUsersTab extends Fragment {

    View view;

    public RecyclerView mRecyclerView;
    public ResultAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public Button mPreviousButton, mNextButton;
    public String query;
    public ProgressBar mProgressBar;
    public LinearLayout mMainLinearLayout, mButtonLayout;
    public JSONObject resultData = null;
    public ArrayList<Bitmap> resultImages = null;
    public ArrayList<String> resultImageUrls = null;

    public static boolean cancelTasks = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        cancelTasks = false;

        view = inflater.inflate(R.layout.result_layout, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.result_progressbar);
        mMainLinearLayout = (LinearLayout) view.findViewById(R.id.result_content);
        mButtonLayout = (LinearLayout) view.findViewById(R.id.result_button_layout);
        mPreviousButton = (Button) view.findViewById(R.id.result_previous_button);
        mNextButton = (Button) view.findViewById(R.id.result_next_button);

        query = getActivity().getIntent().getStringExtra("query");
        Log.d("CHECK-QUERY", query);

        if(resultData==null || resultImages==null || resultImageUrls==null)
            new GetJsonDataFromServer().execute(query);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(resultData!=null && resultImages!=null){
            mAdapter = new ResultAdapter("user", resultData, resultImages, resultImageUrls, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    public class GetButtonJsonDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onCancelled() {

        }

        @Override
        protected void onPreExecute() {
            mMainLinearLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            mPreviousButton.setEnabled(false);
            mNextButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            try {
                if(resultData.getJSONObject("paging").has("previous")){
                    mPreviousButton.setEnabled(true);
                    mPreviousButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("previous"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mPreviousButton.setEnabled(false);
                }

                if(resultData.getJSONObject("paging").has("next")){
                    mNextButton.setEnabled(true);
                    mNextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("next"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mNextButton.setEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mProgressBar.setVisibility(View.GONE);
            mMainLinearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            String response = NetworkUtils.getNormalJsonDataFromServer(null, null, true, params[0], getContext());
            try {
                Log.d("RESPONSE", response);
                resultData = new JSONObject(response);

                if(cancelTasks==true){
                    cancel(true);
                }
                if(isCancelled())
                    return null;

                resultImageUrls = NetworkUtils.getResultImageUrlsFromServer(resultData);

                if(cancelTasks==true){
                    cancel(true);
                }
                if(isCancelled())
                    return null;

                resultImages = NetworkUtils.getResultImagesFromServer(resultData);

                if(cancelTasks==true){
                    cancel(true);
                }
                if(isCancelled())
                    return null;

                mAdapter.updateData(resultData, resultImages, resultImageUrls);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public class GetJsonDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onCancelled(String s) {

        }

        @Override
        protected void onPreExecute() {
            mMainLinearLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            mPreviousButton.setEnabled(false);
            mNextButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            try {
                if(resultData.getJSONObject("paging").has("previous")){
                    mPreviousButton.setEnabled(true);
                    mPreviousButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("previous"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mPreviousButton.setEnabled(false);
                }

                if(resultData.getJSONObject("paging").has("next")){
                    mNextButton.setEnabled(true);
                    mNextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("next"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mNextButton.setEnabled(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            mAdapter = new ResultAdapter("user", resultData, resultImages, resultImageUrls, getContext());
            mRecyclerView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.GONE);
            mMainLinearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            String response = NetworkUtils.getNormalJsonDataFromServer(params[0], "user", false, null, getContext());
            try {
                resultData = new JSONObject(response);

                if(cancelTasks==true){
                    cancel(true);
                }
                if(isCancelled())
                    return null;

                resultImages = NetworkUtils.getResultImagesFromServer(resultData);

                if(cancelTasks==true){
                    cancel(true);
                }
                if(isCancelled())
                    return null;

                resultImageUrls = NetworkUtils.getResultImageUrlsFromServer(resultData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onResume() {
        cancelTasks = false;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("user", false)){
            new RefreshFragment().execute();
            sharedPreferencesEditor.putBoolean("user", false);
            sharedPreferencesEditor.commit();
        }
        super.onResume();
    }

    public class RefreshFragment extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onCancelled(String s) {

        }

        @Override
        protected void onPreExecute() {
            mMainLinearLayout.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            mPreviousButton.setEnabled(false);
            mNextButton.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String s) {
            mPreviousButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            try {
                if(resultData.getJSONObject("paging").has("previous")){
                    mPreviousButton.setEnabled(true);
                    mPreviousButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("previous"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mPreviousButton.setEnabled(false);
                }

                if(resultData.getJSONObject("paging").has("next")){
                    mNextButton.setEnabled(true);
                    mNextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new GetButtonJsonDataFromServer().execute(resultData.getJSONObject("paging").getString("next"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
                    mNextButton.setEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mProgressBar.setVisibility(View.GONE);
            mMainLinearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... params) {

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            if(mAdapter!=null)
                mAdapter.notifyDataSetChanged();

            if(cancelTasks==true){
                cancel(true);
            }
            if(isCancelled())
                return null;

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
