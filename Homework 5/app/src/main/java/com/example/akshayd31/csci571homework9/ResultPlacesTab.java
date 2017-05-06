package com.example.akshayd31.csci571homework9;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
public class ResultPlacesTab extends Fragment implements LocationListener {

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

    public LocationManager locationManager;
    public Location location = null;
    public Criteria locationCriteria = null;
    public String provider;

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

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationCriteria = new Criteria();
        provider = locationManager.getBestProvider(locationCriteria, false);

        if (provider != null && !provider.equals("")) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            location = locationManager.getLastKnownLocation(provider);

            if(location==null)
                locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if(location!=null)
                onLocationChanged(location);
        }

        if(resultData==null || resultImages==null || resultImageUrls==null)
            new GetJsonDataFromServer().execute(query);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(resultData!=null && resultImages!=null){
            mAdapter = new ResultAdapter("place", resultData, resultImages, resultImageUrls, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    public class GetButtonJsonDataFromServer extends AsyncTask<String, Integer, String> {

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


            mAdapter = new ResultAdapter("place", resultData, resultImages, resultImageUrls, getContext());
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

            String response = null;

            if(location!=null)
                response = NetworkUtils.getLocationBasedJsonDataFromServer(params[0], location.getLatitude(), location.getLongitude(), getContext());
            else
                response = NetworkUtils.getLocationBasedJsonDataFromServer(params[0], null, null, getContext());
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
    public void onLocationChanged(Location location) {
        Log.d("NEW-LATITUDE",  String.valueOf(location.getLatitude()));
        Log.d("NEW-LONGITUDE",  String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onResume() {
        cancelTasks = false;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("place", false)){
            new RefreshFragment().execute();
            sharedPreferencesEditor.putBoolean("place", false);
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
