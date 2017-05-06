package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by akshayd31 on 4/11/17.
 */
public class SearchFormFragment extends Fragment {

    View view;
    private Button mClearButton, mSearchButton;
    private AutoCompleteTextView mQueryTextView;
    Context context;
    private LocationManager locationManager;
    private Location location = null;
    private Criteria locationCriteria = null;
    private String provider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_form_fragment, container, false);
        mQueryTextView = (AutoCompleteTextView) view.findViewById(R.id.query_textview);
        mClearButton = (Button) view.findViewById(R.id.clear_button);
        mSearchButton = (Button) view.findViewById(R.id.search_button);

//        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        locationCriteria = new Criteria();
//        provider = locationManager.getBestProvider(locationCriteria, false);
//
//        if (provider != null && !provider.equals("")) {
//            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
//            }
//            location = locationManager.getLastKnownLocation(provider);
//            locationManager.requestLocationUpdates(provider, 20000, 1, this);
//
//            if(location!=null)
//                onLocationChanged(location);
//        }

        mClearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mQueryTextView.setText("");
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mQueryTextView.getText().toString().trim().isEmpty() || mQueryTextView.getText().toString().trim() == "") {
                    Toast mToast = Toast.makeText(context, "Please enter a keyword!", Toast.LENGTH_LONG);
                    mToast.show();
                } else {
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    intent.putExtra("query", mQueryTextView.getText().toString().trim().replaceAll(" ", "%2B"));
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
