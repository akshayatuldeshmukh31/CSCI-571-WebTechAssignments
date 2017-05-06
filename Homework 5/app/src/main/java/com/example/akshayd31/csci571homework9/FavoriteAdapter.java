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

import java.util.ArrayList;

/**
 * Created by akshayd31 on 4/25/17.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    JSONArray resultData = null;
    ArrayList<Bitmap> images = null;
    ArrayList<String> imageUrls = null;
    String type = null;
    int numOfItems;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    public FavoriteAdapter(String type, JSONArray resultData, ArrayList<Bitmap> images, ArrayList<String> imageUrls, Context context){
        this.type = type;
        this.resultData = resultData;
        this.images = images;
        this.imageUrls = imageUrls;
        this.context = context;

        if(resultData!=null){
            numOfItems = resultData.length();
        }
        else{
            numOfItems = 0;
        }

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{

        //TODO Make list of items
        public String id;
        public ImageView favoriteIcon, profileImage, detailsImage;
        public TextView resultTopic;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            resultTopic = (TextView) itemView.findViewById(R.id.result_textview);
            profileImage = (ImageView) itemView.findViewById(R.id.result_profile);
            favoriteIcon = (ImageView) itemView.findViewById(R.id.result_favorite);
            detailsImage = (ImageView) itemView.findViewById(R.id.result_details);
            detailsImage.setClickable(true);

            detailsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailsActivity.class);

                    try {
                        intent.putExtra("fromFavorite", true);
                        intent.putExtra("type", type);
                        intent.putExtra("name", resultData.getJSONObject(getAdapterPosition()).getString("name"));
                        intent.putExtra("url", resultData.getJSONObject(getAdapterPosition()).getString("url"));
                        intent.putExtra("id", resultData.getJSONObject(getAdapterPosition()).getString("id"));
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
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        try {
            holder.id = resultData.getJSONObject(position).getString("id");
            holder.resultTopic.setText(resultData.getJSONObject(position).getString("name"));
            holder.profileImage.setImageBitmap(images.get(position));
            holder.favoriteIcon.setImageResource(R.drawable.favorites_on);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_viewholder, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    public void updateData(JSONArray resultData, ArrayList<Bitmap> images, ArrayList<String> imageUrls){

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
