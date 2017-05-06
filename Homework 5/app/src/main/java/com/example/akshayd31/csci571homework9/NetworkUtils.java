package com.example.akshayd31.csci571homework9;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akshayd31 on 4/19/17.
 */
public class NetworkUtils {

    public static String getNormalJsonDataFromServer(String query, String type, boolean isCompleteUrlGiven, String completeUrl, Context context){
        String url = null;

        if(!isCompleteUrlGiven)
            url = context.getString(R.string.php_server_url) + "?query=" + query
                    + "&type=" + type;
        else
            url = completeUrl;

        Log.d("FINAL-URL", url);

        URL reqUrl = null;
        try {
            reqUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            if (reqUrl != null) {
                urlConnection = (HttpURLConnection) reqUrl.openConnection();
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        StringBuffer response = new StringBuffer();

        try {
            if (in != null) {
                while((line = in.readLine())!=null){
                    response.append(line);
                }
                in.close();

                if(isCompleteUrlGiven)
                    return response.toString();

                JSONObject jsonResponse = new JSONObject(response.toString());

                if(jsonResponse.getString("status")=="true" | jsonResponse.getBoolean("status")==true){
                    Log.d("INFO", jsonResponse.getString("data"));
                    return jsonResponse.getString("data");
                }
                else {
                    Log.d("SERVER-RESPONSE", jsonResponse.getString("status"));
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLocationBasedJsonDataFromServer(String query, Double latitude, Double longitude, Context context){
        String url = null;

        if(latitude!=null && longitude!=null) {
            url = context.getString(R.string.php_server_url) + "?query=" + query
                    + "&type=place&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude);
        }
        else{
            url = context.getString(R.string.php_server_url) + "?query=" + query
                    + "&type=place&latitude=&longitude=";
        }

        Log.d("FINAL-URL", url);

        URL reqUrl = null;
        try {
            reqUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            if (reqUrl != null) {
                urlConnection = (HttpURLConnection) reqUrl.openConnection();
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        StringBuffer response = new StringBuffer();

        try {
            if (in != null) {
                while((line = in.readLine())!=null){
                    response.append(line);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                if(jsonResponse.getString("status")=="true" | jsonResponse.getBoolean("status")==true){
                    Log.d("INFO", jsonResponse.getString("data"));
                    return jsonResponse.getString("data");
                }
                else {
                    Log.d("SERVER-RESPONSE", jsonResponse.getString("status"));
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<String> getResultImageUrlsFromServer(JSONObject data){
        ArrayList<String> response = null;

        try {
            JSONArray arrayOfInfo = data.getJSONArray("data");
            for(int i = 0; i < arrayOfInfo.length(); i++){
                if(response==null)
                    response = new ArrayList<String>();
                response.add(arrayOfInfo.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static ArrayList<Bitmap> getResultImagesFromServer(JSONObject data){
        ArrayList<Bitmap> response = null;
        URL url;
        HttpURLConnection connection;
        InputStream input;
        Bitmap imageResult;
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 4;

        try {
            JSONArray arrayOfInfo = data.getJSONArray("data");
            for(int i = 0; i < arrayOfInfo.length(); i++){
                url = new URL(arrayOfInfo.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
                imageResult = BitmapFactory.decodeStream(input, null, mOptions);

                if(response==null)
                    response = new ArrayList<>();
                response.add(imageResult);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String getAlbumsAndPostsJsonDataFromServer(String id, Context context){
        String url = context.getString(R.string.php_server_url) + "?id=" + id;

        Log.d("FINAL-URL-ALBUM-POST", url);

        URL reqUrl = null;
        try {
            reqUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            if (reqUrl != null) {
                urlConnection = (HttpURLConnection) reqUrl.openConnection();
                urlConnection.setRequestMethod("GET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            if (urlConnection != null) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        StringBuffer response = new StringBuffer();

        try {
            if (in != null) {
                while((line = in.readLine())!=null){
                    response.append(line);
                }
                in.close();


                JSONObject jsonResponse = new JSONObject(response.toString());

                if(jsonResponse.getString("status")=="true" | jsonResponse.getBoolean("status")==true){
                    Log.d("INFO", jsonResponse.getString("data"));
                    return jsonResponse.getString("data");
                }
                else {
                    Log.d("SERVER-RESPONSE", jsonResponse.getString("status"));
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<String> getAlbumHeadersFromServer(JSONObject jsonObject){
        if(jsonObject==null)
            return null;

        if(!jsonObject.has("albums"))
            return null;

        try {
            ArrayList<String> response = new ArrayList<>();
            JSONArray albumData = jsonObject.getJSONObject("albums").getJSONArray("data");

            for(int i = 0; i<albumData.length(); i++){
                response.add(albumData.getJSONObject(i).getString("name"));
            }

            return response;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<String> getAlbumIdsFromServer(JSONObject jsonObject){
        if(jsonObject==null)
            return null;

        if(!jsonObject.has("albums"))
            return null;

        try {
            ArrayList<String> response = new ArrayList<>();
            JSONArray albumData = jsonObject.getJSONObject("albums").getJSONArray("data");

            for(int i = 0; i<albumData.length(); i++){
                response.add(albumData.getJSONObject(i).getString("id"));
            }

            return response;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static HashMap<String, ArrayList<Bitmap>> getAlbumPhotosFromServer(JSONObject jsonObject){
        if(jsonObject==null)
            return null;

        if(!jsonObject.has("albums"))
            return null;

        try {
            HashMap<String, ArrayList<Bitmap>> response = new HashMap<>();
            ArrayList<Bitmap> temp = null;
            URL url;
            HttpURLConnection connection;
            InputStream input;
            Bitmap imageResult;
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inSampleSize = 4;
            JSONArray albumData = jsonObject.getJSONObject("albums").getJSONArray("data");

            for(int i = 0; i<albumData.length(); i++){
                temp = new ArrayList<>();
                if(albumData.getJSONObject(i).has("photos")) {
                    for (int j = 0; j < albumData.getJSONObject(i).getJSONObject("photos").getJSONArray("data").length(); j++) {
                        Log.d("PHOTO_URL", albumData.getJSONObject(i).getJSONObject("photos").getJSONArray("data").getJSONObject(j).getString("picture"));
                        url = new URL(albumData.getJSONObject(i).getJSONObject("photos").getJSONArray("data").getJSONObject(j).getString("picture"));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        try {
                            input = connection.getInputStream();
                            imageResult = BitmapFactory.decodeStream(input, null, mOptions);
                            temp.add(imageResult);
                        } catch(FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.d("id", albumData.getJSONObject(i).getString("id"));
                response.put(albumData.getJSONObject(i).getString("id"), temp);
            }

            return response;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap getProfileImage(JSONObject jsonObject){
        if(jsonObject==null)
            return null;

        try {
            URL url = null;
            HttpURLConnection connection;
            InputStream input = null;
            Bitmap imageResult = null;
            BitmapFactory.Options mOptions = new BitmapFactory.Options();
            mOptions.inSampleSize = 4;

            Log.d("PHOTO_URL", jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
            url = new URL(jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            try {
                input = connection.getInputStream();
                imageResult = BitmapFactory.decodeStream(input, null, mOptions);
                return imageResult;
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getFavoriteDataFromSharedPreferences(String type, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);

        String favData = sharedPreferences.getString(context.getString(R.string.favorites), null);

        if(favData!=null){
            try {
                JSONObject jsonObject = new JSONObject(favData);

                if(jsonObject.has(type)){
                    JSONArray jsonArray = jsonObject.getJSONArray(type);
                    return jsonArray;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    public static ArrayList<Bitmap> getFavoriteImagesFromServer(JSONArray jsonArray){
        ArrayList<Bitmap> images = new ArrayList<>();
        URL url;
        HttpURLConnection connection;
        InputStream input;
        Bitmap imageResult;
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 4;

        for(int i = 0; i<jsonArray.length(); i++){
            try {
                url = new URL(jsonArray.getJSONObject(i).getString("url"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                try {
                    input = connection.getInputStream();
                    imageResult = BitmapFactory.decodeStream(input, null, mOptions);
                    images.add(imageResult);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

        }
        return images;
    }

    public static ArrayList<String> getFavoriteImageUrlsFromServer(JSONArray jsonArray){
        ArrayList<String> imageUrls = new ArrayList<>();

        for(int i = 0; i<jsonArray.length(); i++){
            try {
                imageUrls.add(jsonArray.getJSONObject(i).getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return imageUrls;
    }
}
