package com.example.posty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //MainActivity.posts.clear();

        Context context = getContext();
        RecyclerView recyclerView = view.findViewById(R.id.mainRecView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        downloadJSON(MainActivity.DATABASE_URL);

        for(int i = 0; i<MainActivity.posts.size();i++){
            Post currentPost = MainActivity.posts.get(i);
            String currentPicName = currentPost.getPicture();
            if(currentPicName == null || currentPicName == "NULL" || currentPicName == "null"){

            }else{
                Log.e("Progress",Integer.toString(i));
                String url = "http://192.168.1.132/api/images/" + currentPicName;
                downloadPics(url,i);
            }
        }

        PostAdapter postAdapter = new PostAdapter(context, MainActivity.posts);
        recyclerView.setHasFixedSize(true);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
        itemDecorator.setDrawable(ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(itemDecorator);

        String text = Integer.toString(MainActivity.posts.size());

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        //toast.show();

        if(MainActivity.posts.isEmpty()){

        }else{
            postAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(postAdapter);

        }




//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    //============================= Load From Database =====================================
    public void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ListView Error",e.getMessage());
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(MainActivity.DATABASE_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //String responeMsg = con.getResponseMessage().toString();
                    //Log.e("CONN Status",responeMsg);
                    //con.setRequestMethod("GET");
                    //con.setConnectTimeout(3000);
                    //con.connect();
                    //Log.e("CONN STATUS",con.toString());
                    StringBuilder sb = new StringBuilder();
                    //==================DEBUG=======================================
                    try{
                        InputStream in = new BufferedInputStream(con.getInputStream());
                        if(in.available()>-1){
                            //Log.e("ConnStatus","Connection Success!");
                        }
                        else{
                            //Log.e("ConnStatus","Connection Failed");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        //Log.e("ConnStatus","NO CONNECTION");
                    }
                    //================================================================
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    int i = 0;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                        i = i +1;
                    }
                    //Log.e("MyCounter","Count is: " + Integer.toString(i));
                    //Log.e("MyJSON",sb.toString());
                    //Log.e("MyJSONTrimmed",sb.toString().trim());
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("Android Error",e.getMessage());
                    //Log.e("JSON ERROR","Unable to load JSON");
                    String failed = "Error Loading JSON";
                    return failed;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    //============================= TODO: CHANGE FUNCTION For our case (posts)================================
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] post = new String[jsonArray.length()];
        MainActivity.posts.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String tempDispName = obj.getString("displayName");
            String tempUsername = obj.getString("username");
            String tempContent = obj.getString("content");
            //TODO: Actually get and process pictures
            String tempProfilePic = "";
            String tempPic = obj.getString("pic");
            if (tempPic == "NULL" || tempPic == ""){
                tempPic = "";
            }
            Bitmap tempPict = null;
            /*
            if(tempPic==(null) || tempPic.equals("null") || tempPic.equals("NULL") || tempPic == "") {

            }else {
                //Open image from folder
                ///*
                //Log.e("PIC NAME",tempPic);
                String URL = "http://192.168.1.132/api/images/" + tempPic;
                //Log.e("String Name", tempPic);
                try {
                    //new DownloadImageTask(picView.execute(URL);
                    tempPict = getBitMap(URL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            */
            Post tempPost = new Post(tempUsername,tempDispName,tempProfilePic,tempContent,tempPic,tempPict);
            MainActivity.posts.add(i,tempPost);
            post[i] = obj.getString("displayName") + " " + obj.getString("username") + " " + obj.getString("content") + " " + obj.getString("pic");
        }
        Collections.reverse(MainActivity.posts);//=========================Reverse post order
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        //listView.setAdapter(arrayAdapter);
        //Log.e("DisplayName",post[0].toString());
        //Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if (post.length>0){
            //Toast toast = Toast.makeText(context, post[0], duration);
            //toast.show();
        }else{
            //Toast toast = Toast.makeText(context, "Error getting posts", duration);
            //toast.show();
        }

    }

    private Bitmap getBitMap(String URL){
        Bitmap bitmap = null;
        //String URL = "http://192.168.1.132/api/images/" + picLocation;
        /*
        try {
            InputStream in = new java.net.URL(URL).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        */

        try{
            URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStream input = con.getInputStream();
            Log.e("Progress","GOOD SO FAR");
            bitmap = BitmapFactory.decodeStream(input);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Error","Malformed URL");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error","IOException");
        }
        Log.e("Bitmap",bitmap.toString());
        return bitmap;
    }



    //============================= Load Pics From Database =====================================
    public void downloadPics(final String urlWebService, final int position) {

        class DownloadPics extends AsyncTask<Void, Void, Bitmap> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //TODO: change this function to load pics into MainActivity.posts
                loadPicIntoList(s, position);
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap pic = null;
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    InputStream input = con.getInputStream();
                    pic = BitmapFactory.decodeStream(input);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("Error",e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Error",e.getMessage());
                }
                return pic;
            }
        }
        DownloadPics getPics = new DownloadPics();
        getPics.execute();
    }

    private void loadPicIntoList(Bitmap bitmap, int position){
        //Post tempPost = MainActivity.posts.get(position);
        Post tempPost = MainActivity.posts.get(position);
        String tempUsername = tempPost.getUsername();
        String displayName = tempPost.getDisplayName();
        String profilePic = tempPost.getProfilePic();
        String content = tempPost.getContent();
        String picture = tempPost.getPicture();
        Post newPost = new Post(tempUsername, displayName, profilePic, content, picture, bitmap);
        MainActivity.posts.set(position,newPost);
    }


}
