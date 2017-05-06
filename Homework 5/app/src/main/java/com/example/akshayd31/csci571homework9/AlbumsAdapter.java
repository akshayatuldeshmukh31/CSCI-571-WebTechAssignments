package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akshayd31 on 4/24/17.
 */
public class AlbumsAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<String> listHeaders, listIds;
    HashMap<String, ArrayList<Bitmap>> photos;

    public AlbumsAdapter(ArrayList<String> listIds, ArrayList<String> listHeaders, HashMap<String, ArrayList<Bitmap>> photos, Context context){
        this.listIds = listIds;
        this.listHeaders = listHeaders;
        this.photos = photos;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        if(listHeaders==null)
            return 0;

        return listHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(photos==null)
            return 0;

        return photos.get(listIds.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(listHeaders==null)
            return 0;

        return listHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(photos==null)
            return 0;

        return photos.get(listIds.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String header = (String) getGroup(groupPosition);

        if(convertView==null)
            convertView = LayoutInflater.from(context).inflate(R.layout.albums_list_group, parent, false);

        TextView heading = (TextView) convertView.findViewById(R.id.album_heading);
        heading.setText(header);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Bitmap photo = null;

        Log.d("GROUP-POSITION", String.valueOf(groupPosition));

        if(getChildrenCount(groupPosition)>childPosition)
            photo = (Bitmap) getChild(groupPosition, childPosition);

        if(convertView==null)
            convertView = LayoutInflater.from(context).inflate(R.layout.albums_list_item, parent, false);

        ImageView image = (ImageView) convertView.findViewById(R.id.album_photo);

        if (photo != null) {
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(photo);
        } else
            image.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
