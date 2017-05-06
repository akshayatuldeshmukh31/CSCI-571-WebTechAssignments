package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    public String type, id, name, url;
    private Bitmap image = null;
    private boolean isFavorite = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    PostsTab postsTab = new PostsTab();
    AlbumsTab albumsTab = new AlbumsTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        getSupportActionBar().setTitle("More Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");

        Log.d("URL-DETAILS", url);

        try {
            String favorites = sharedPreferences.getString(getString(R.string.favorites), null);
            Log.d("BEFORE-JSON", favorites);

            if(favorites!=null) {
                JSONObject jsonObject = new JSONObject(favorites);

                if (jsonObject != null) {
                    if (jsonObject.has(type)) {
                        boolean flag = false;
                        JSONArray jsonArray = jsonObject.getJSONArray(type);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).getString("id").equals(id)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag)
                            isFavorite = true;
                        else
                            isFavorite = false;
                    } else
                        isFavorite = false;
                } else
                    isFavorite = false;
            }
            else
                isFavorite = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mTabLayout = (TabLayout) findViewById(R.id.details_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.albums).setText("Albums"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.posts).setText("Posts"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.details_pager);
        viewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(viewPager);

        for(int i = 0; i<mTabLayout.getTabCount(); i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        menu.findItem(R.id.details_add_fav).setVisible(!isFavorite);
        menu.findItem(R.id.details_remove_fav).setVisible(isFavorite);
        return true;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        //To count the number of tabs
        int tabCount;
        private String[] tabTitles = new String[] {"Albums", "Posts"};
        private int[] imageResId = {R.drawable.albums, R.drawable.posts};

        public ViewPagerAdapter(FragmentManager fm, int tabCount){
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            //Returning current tab
            switch(position){
                case 0: return albumsTab;
                case 1: return postsTab;
                default: return null;
            }
        }

        public View getTabView(int i) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab, null);
            TextView tv = (TextView) v.findViewById(R.id.tab_text);
            tv.setText(tabTitles[i]);
            ImageView img = (ImageView) v.findViewById(R.id.tab_icon);
            img.setImageResource(imageResId[i]);
            return v;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            albumsTab.cancelTasks = true;
            postsTab.cancelTasks = true;
            if(getIntent().getBooleanExtra("fromFavorite", true)==false) {
                editor.putBoolean(type, true);
                editor.commit();
            }
            this.finish();
            return true;
        }
        else if(id == R.id.details_add_fav){
            String favorites = sharedPreferences.getString(getString(R.string.favorites), null);
            JSONObject favData;

            try {
                favData = new JSONObject(favorites);

                JSONArray favTypeArr = favData.getJSONArray(type);
                JSONObject entry = new JSONObject();

                entry.put("id", this.id);
                entry.put("name", name);
                entry.put("url", url);

                favTypeArr.put(entry);
                favData.put(type, favTypeArr);

                Log.d("ADDED TO FAVORITES", favData.toString());
                editor.putString(getString(R.string.favorites), favData.toString());
                editor.commit();

                Toast mToast = Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT);
                mToast.show();

                isFavorite = true;
                invalidateOptionsMenu();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(id == R.id.details_remove_fav){
            String favorites = sharedPreferences.getString(getString(R.string.favorites), null);
            JSONObject favData;

            try {
                favData = new JSONObject(favorites);

                JSONArray jsonArray = favData.getJSONArray(type);

                for(int i = 0; i<jsonArray.length();i++){
                    if(jsonArray.getJSONObject(i).getString("id").equals(this.id)){
                        jsonArray.remove(i);
                        break;
                    }
                }

                favData.put(type, jsonArray);
                Log.d("REMOVED FROM FAVORITES", favData.toString());
                editor.putString(getString(R.string.favorites), favData.toString());
                editor.commit();

                Toast mToast = Toast.makeText(this, "Removed from Favorites!", Toast.LENGTH_SHORT);
                mToast.show();

                isFavorite = false;
                invalidateOptionsMenu();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(id == R.id.details_share){
            Toast.makeText(DetailsActivity.this, "Sharing " + name + "!", Toast.LENGTH_SHORT).show();
            ShareDialog shareDialog;
            FacebookSdk.sdkInitialize(getApplicationContext());
            shareDialog = new ShareDialog(this);

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(name)
                    .setImageUrl(Uri.parse(url))
                    .setContentDescription("FB SEARCH FROM CSCI571")
                    .setContentUrl(Uri.parse("http://facebook.com/" + this.id))
                    .build();

            shareDialog.show(linkContent);

        }
        return super.onOptionsItemSelected(item);
    }
}
