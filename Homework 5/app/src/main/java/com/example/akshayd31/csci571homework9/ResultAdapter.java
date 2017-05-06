package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshayd31 on 4/19/17.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    JSONObject resultData = null;
    ArrayList<Bitmap> images = null;
    ArrayList<String> imageUrls = null;
    String type = null;
    int numOfItems;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    public ResultAdapter(String type, JSONObject resultData, ArrayList<Bitmap> images, ArrayList<String> imageUrls, Context context){
        this.type = type;
        this.resultData = resultData;
        this.images = images;
        this.imageUrls = imageUrls;
        this.context = context;

        if(resultData!=null){
            try {
                numOfItems = resultData.getJSONArray("data").length();
            } catch (JSONException e) {
                numOfItems = 0;
                e.printStackTrace();
            }
        }

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder{

        //TODO Make list of items
        public String id;
        public ImageView favoriteIcon, profileImage, detailsImage;
        public TextView resultTopic;

        public ResultViewHolder(View itemView) {
            super(itemView);
            resultTopic = (TextView) itemView.findViewById(R.id.result_textview);
            profileImage = (ImageView) itemView.findViewById(R.id.result_profile);
            favoriteIcon = (ImageView) itemView.findViewById(R.id.result_favorite);
            detailsImage = (ImageView) itemView.findViewById(R.id.result_details);
            detailsImage.setClickable(true);

            detailsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ResultUsersTab.cancelTasks = true;
                    ResultPlacesTab.cancelTasks = true;
                    ResultPagesTab.cancelTasks = true;
                    ResultGroupsTab.cancelTasks = true;
                    ResultEventsTab.cancelTasks = true;

                    Intent intent = new Intent(context, DetailsActivity.class);

                    try {
                        intent.putExtra("fromFavorite", false);
                        intent.putExtra("type", type);
                        intent.putExtra("name", resultData.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("name"));
                        intent.putExtra("url", resultData.getJSONArray("data").getJSONObject(getAdapterPosition()).getJSONObject("picture").getJSONObject("data").getString("url"));
                        intent.putExtra("id", resultData.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return numOfItems;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        try {
            holder.id = resultData.getJSONArray("data").getJSONObject(position).getString("id");
            holder.resultTopic.setText(resultData.getJSONArray("data").getJSONObject(position).getString("name"));
            holder.profileImage.setImageBitmap(images.get(position));

            //TODO Improve favorite detection
            String favorites = sharedPreferences.getString(context.getString(R.string.favorites), null);
            JSONObject jsonObject = new JSONObject(favorites);
            JSONArray jsonArray = jsonObject.getJSONArray(type);
            boolean flag = false;

            for(int i = 0; i<jsonArray.length(); i++){
                if(jsonArray.getJSONObject(i).getString("id").equals(holder.id)){
                    flag = true;
                    break;
                }
            }

            if(flag)
                holder.favoriteIcon.setImageResource(R.drawable.favorites_on);
            else
                holder.favoriteIcon.setImageResource(R.drawable.favorites_off);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_viewholder, parent, false);
        return new ResultViewHolder(itemView);
    }

    public void updateData(JSONObject resultData, ArrayList<Bitmap> images, ArrayList<String> imageUrls){

        if(resultData==null || images==null || imageUrls==null)
            return;

        this.resultData = resultData;

        if(this.images!=null){
            this.images.clear();
        }
        else{
            this.images = new ArrayList<>();
        }
        this.images.addAll(images);

        if(this.imageUrls!=null){
            this.imageUrls.clear();
            this.imageUrls.addAll(imageUrls);
        }
        else{
            this.imageUrls = imageUrls;
        }
        notifyDataSetChanged();
    }
}
