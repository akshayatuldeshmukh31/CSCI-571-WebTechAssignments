package com.example.akshayd31.csci571homework9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by akshayd31 on 4/16/17.
 */
public class FavoriteFragment extends Fragment {

    View view;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.favorites_fragment, container, false);

        mTabLayout = (TabLayout) view.findViewById(R.id.favorites_tab);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.users).setText("Users"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.pages).setText("Pages"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.events).setText("Events"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.places).setText("Places"));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.tab).setIcon(R.drawable.groups).setText("Groups"));

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.favorites_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getChildFragmentManager(), 5);
        viewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(viewPager);

        for(int i = 0; i<mTabLayout.getTabCount(); i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
        return view;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

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
                case 0: FavUsersTab favUsersTab = new FavUsersTab();
                        return favUsersTab;
                case 1: FavPagesTab favPagesTab = new FavPagesTab();
                        return favPagesTab;
                case 2: FavEventsTab favEventsTab = new FavEventsTab();
                        return favEventsTab;
                case 3: FavPlacesTab favPlacesTab = new FavPlacesTab();
                        return favPlacesTab;
                case 4: FavGroupsTab favGroupsTab = new FavGroupsTab();
                        return favGroupsTab;
                default: return null;
            }
        }

        public View getTabView(int i) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tab, null);
            TextView tv = (TextView) v.findViewById(R.id.tab_text);
            tv.setText(tabTitles[i]);
            ImageView img = (ImageView) v.findViewById(R.id.tab_icon);
            img.setImageResource(imageResId[i]);
            return v;
        }
    }
}

