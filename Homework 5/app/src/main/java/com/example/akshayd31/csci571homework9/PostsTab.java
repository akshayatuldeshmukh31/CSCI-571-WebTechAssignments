package com.example.akshayd31.csci571homework9;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akshayd31 on 4/23/17.
 */

//TODO Straighten out code for posts
public class PostsTab extends Fragment {

    View view;

    private RecyclerView mRecyclerView;
    private PostAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private LinearLayout mMainLinearLayout;
    private TextView mNoRecordsTextView;
    private JSONObject postData = null;
    private Bitmap postImage = null;
    private String id = null;

    public static boolean cancelTasks = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cancelTasks = false;
        view = inflater.inflate(R.layout.details_posts_layout, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.post_progressbar);
        mNoRecordsTextView = (TextView) view.findViewById(R.id.posts_no_records_text);
        mMainLinearLayout = (LinearLayout) view.findViewById(R.id.post_content);

        id = getActivity().getIntent().getStringExtra("id");
        Log.d("INTENT-ID", id);

        if(postData==null || postImage==null)
            new GetJsonDataFromServer().execute(id);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.post_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(postData!=null && postImage!=null){
            try {
                mAdapter = new PostAdapter(postData.getJSONObject("posts"), postImage, postData.getString("name"), getContext());
                mRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public class GetJsonDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onCancelled(String s) {

        }

        @Override
        protected void onPreExecute() {
            mMainLinearLayout.setVisibility(View.GONE);
            mNoRecordsTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(postData!=null && postImage!=null) {
                    mAdapter = new PostAdapter(postData.getJSONObject("posts"), postImage, postData.getString("name"), getContext());
                    mRecyclerView.setAdapter(mAdapter);
                    mProgressBar.setVisibility(View.GONE);
                    mMainLinearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    mProgressBar.setVisibility(View.GONE);
                    mNoRecordsTextView.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(String... params) {

            if(cancelTasks)
                cancel(true);
            if(isCancelled())
                return null;

            String response = NetworkUtils.getAlbumsAndPostsJsonDataFromServer(params[0], getContext());
            try {
                if(response!=null) {

                    if(cancelTasks)
                        cancel(true);
                    if(isCancelled())
                        return null;

                    postData = new JSONObject(response);

                    if(cancelTasks)
                        cancel(true);
                    if(isCancelled())
                        return null;

                    if(!postData.has("posts")) {
                        postData = null;
                        postImage = null;
                    }
                    else
                        postImage = NetworkUtils.getProfileImage(postData);
                }
                else {
                    postData = null;
                    postImage = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(cancelTasks)
                cancel(true);
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