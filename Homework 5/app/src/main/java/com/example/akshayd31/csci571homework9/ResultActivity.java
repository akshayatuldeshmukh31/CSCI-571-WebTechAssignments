package com.example.akshayd31.csci571homework9;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ResultUsersTab resultUsersTab = new ResultUsersTab();
    ResultPagesTab resultPagesTab = new ResultPagesTab();
    ResultEventsTab resultEventsTab = new ResultEventsTab();
    ResultPlacesTab resultPlacesTab = new ResultPlacesTab();
    ResultGroupsTab resultGroupsTab = new ResultGroupsTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setTitle("Results");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.results_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.users).setText("Users"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.pages).setText("Pages"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.events).setText("Events"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.places).setText("Places"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.groups).setText("Groups"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.results_pager);
        viewPager.setOffscreenPageLimit(5);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 5);
        viewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(viewPager);

        for(int i = 0; i<mTabLayout.getTabCount(); i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        //To count the number of tabs
        int tabCount;
        private String[] tabTitles = new String[] {"Users", "Pages", "Events", "Places", "Groups"};
        private int[] imageResId = {R.drawable.users, R.drawable.pages, R.drawable.events, R.drawable.places, R.drawable.groups};

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
                case 0: return resultUsersTab;
                case 1: return resultPagesTab;
                case 2: return resultEventsTab;
                case 3: return resultPlacesTab;
                case 4: return resultGroupsTab;
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
        switch(item.getItemId()){
            case android.R.id.home:
                ResultUsersTab.cancelTasks = true;
                ResultPlacesTab.cancelTasks = true;
                ResultPagesTab.cancelTasks = true;
                ResultGroupsTab.cancelTasks = true;
                ResultEventsTab.cancelTasks = true;
                this.finish();
        }
        return false;
    }
}
