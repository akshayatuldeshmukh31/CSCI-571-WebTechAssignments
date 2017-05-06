package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akshayd31 on 4/23/17.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    JSONObject postData;
    Bitmap postImage;
    String heading;
    Context context;
    int numOfItems = 0;

    public PostAdapter(JSONObject postData, Bitmap postImage, String heading, Context context){
        this.postData = postData;
        this.postImage = postImage;
        this.heading = heading;
        this.context = context;

        Log.d("POST-DATA-RECVD", postData.toString());
        try {
            Log.d("POST-DATA-LENGTH", String.valueOf(postData.getJSONArray("data").length()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            numOfItems = postData.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        public ImageView postProfile;
        public TextView postHeading, postDate, postBody;

        public PostViewHolder(View itemView) {
            super(itemView);
            postProfile = (ImageView) itemView.findViewById(R.id.post_profile);
            postHeading = (TextView) itemView.findViewById(R.id.post_heading);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            postBody = (TextView) itemView.findViewById(R.id.post_body);
        }
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_viewholder, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.postProfile.setImageBitmap(postImage);
        holder.postHeading.setText(heading);

        try {
            holder.postDate.setText(postData.getJSONArray("data").getJSONObject(position).getString("created_time").split("T")[0]
                + " " + postData.getJSONArray("data").getJSONObject(position).getString("created_time").split("T")[1].split("\\+")[0]);

            if(postData.getJSONArray("data").getJSONObject(position).has("message"))
                holder.postBody.setText(postData.getJSONArray("data").getJSONObject(position).getString("message"));
            else if(postData.getJSONArray("data").getJSONObject(position).has("story"))
                holder.postBody.setText(postData.getJSONArray("data").getJSONObject(position).getString("story"));
            else
                holder.postBody.setText("");
        } catch (JSONException e) {
            holder.postDate.setText("");
            holder.postBody.setText("");
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return numOfItems;
    }
}
