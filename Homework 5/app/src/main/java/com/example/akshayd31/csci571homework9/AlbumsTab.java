package com.example.akshayd31.csci571homework9;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akshayd31 on 4/23/17.
 */
public class AlbumsTab extends Fragment {

    View view;

    private ExpandableListView expandableListView;
    private AlbumsAdapter albumsAdapter;
    private ProgressBar mProgressBar;
    private TextView mNoRecordsTextView;
    private ArrayList<String> albumHeaders = null, albumIds = null;
    private HashMap<String, ArrayList<Bitmap>> photos = null;
    private String id = null;

    public static boolean cancelTasks = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        cancelTasks = false;
        view = inflater.inflate(R.layout.details_albums_layout, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.album_progressbar);
        mNoRecordsTextView = (TextView) view.findViewById(R.id.albums_no_records_text);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list_view);

        id = getActivity().getIntent().getStringExtra("id");
        Log.d("INTENT-ID", id);

        if(albumHeaders==null || photos==null || albumIds==null)
            new GetJsonDataFromServer().execute(id);

        if(albumHeaders!=null && photos!=null && albumIds!=null) {
            albumsAdapter = new AlbumsAdapter(albumIds, albumHeaders, photos, getContext());
            expandableListView.setAdapter(albumsAdapter);
        }

        return view;
    }

    public class GetJsonDataFromServer extends AsyncTask<String, Integer, String> {

        @Override
        protected void onCancelled(String s) {

        }

        @Override
        protected void onPreExecute() {
            expandableListView.setVisibility(View.GONE);
            mNoRecordsTextView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            if(albumIds!=null && albumHeaders!=null && photos!=null) {
                albumsAdapter = new AlbumsAdapter(albumIds, albumHeaders, photos, getContext());
                expandableListView.setAdapter(albumsAdapter);
                mProgressBar.setVisibility(View.GONE);
                expandableListView.setVisibility(View.VISIBLE);
            }
            else{
                mProgressBar.setVisibility(View.GONE);
                mNoRecordsTextView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            if(cancelTasks)
                cancel(true);
            if(isCancelled())
                return null;

            String response = NetworkUtils.getAlbumsAndPostsJsonDataFromServer(params[0], getContext());

            if(cancelTasks)
                cancel(true);
            if(isCancelled())
                return null;

            if(response!=null)
                Log.d("ALBUM-DATA", response);
            try {
                if(response!=null) {
                    JSONObject jsonResponse = new JSONObject(response);

                    if(cancelTasks)
                        cancel(true);
                    if(isCancelled())
                        return null;

                    albumIds = NetworkUtils.getAlbumIdsFromServer(jsonResponse);

                    if(cancelTasks)
                        cancel(true);
                    if(isCancelled())
                        return null;

                    albumHeaders = NetworkUtils.getAlbumHeadersFromServer(jsonResponse);

                    if(cancelTasks)
                        cancel(true);
                    if(isCancelled())
                        return null;

                    photos = NetworkUtils.getAlbumPhotosFromServer(jsonResponse);
                }
                else{
                    albumIds = null;
                    albumHeaders = null;
                    photos = null;
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
