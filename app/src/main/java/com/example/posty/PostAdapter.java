package com.example.posty;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.postViewHolder> {
    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> objects){
        //super(context, resource, objects);

        this.posts = objects;
        this.context = context;

    }

    public class postViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTV;
        public TextView contentTV;
        public TextView displayNameTV;
        ConstraintLayout constraintLayout;
        CircleImageView profilePicView;
        ImageView picView;

        public postViewHolder(View view){
            super(view);

            nameTV = view.findViewById(R.id.usernameTextView);
            nameTV.setTypeface(null, Typeface.ITALIC);
            contentTV = view.findViewById(R.id.contentTextView);
            displayNameTV = view.findViewById(R.id.displayNameTV);
            displayNameTV.setTypeface(null, Typeface.BOLD);
            profilePicView = view.findViewById(R.id.circularProfileImageViewDetail);
            picView = view.findViewById(R.id.contentImageView);
            constraintLayout = view.findViewById(R.id.constraintLayout);
        }
    }

    @Override
    public PostAdapter.postViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.post_detail, parent, false);

        postViewHolder viewHolder = new postViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostAdapter.postViewHolder viewHolder, int position){

        Post post = posts.get(position);
        TextView nameTV = viewHolder.nameTV;
        TextView contentTV = viewHolder.contentTV;
        ImageView picView = viewHolder.picView;

        //Assuming we have ArrayList images and an imageview image
        //Glide.with(context).asBitmap().load(images.get(position)).into(viewHolder.image);

        String username = post.getUsername().toString();
        String displayName = post.getDisplayName().toString();
        String content = post.getContent().toString();
        String picLocation = post.getPicture().toString();
        Bitmap imageBitMap = post.getPic();


        /*
        if(picLocation==(null) || picLocation.equals("null") || picLocation.equals("NULL") || picLocation == "") {

        }else{
            //Open image from folder
            //
            String URL = "http://192.168.1.132/api/images/" + picLocation;
            Log.e("String Name",picLocation);
            try {
                //new DownloadImageTask(picView.execute(URL);
                new DownloadImageTask(picView).execute(URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //


            //Open image from database
            try{
                //Log.e("Image",picLocation);
                picLocation = picLocation.replaceAll(" ","+");
                //Log.e("New Image",picLocation);
                byte[] newbytes = Base64.decode(picLocation.getBytes(), Base64.DEFAULT);
                Bitmap newBitmap = BitmapFactory.decodeByteArray(newbytes, 0, newbytes.length);
                viewHolder.picView.setImageBitmap(newBitmap);
                Log.e("Reset Image","Success");

            }catch (Exception e){
                e.printStackTrace();
                Log.e("Reset Image Error",e.getMessage());
            }

        }
        */

        viewHolder.displayNameTV.setText(displayName);
        viewHolder.nameTV.setText("@"+username);
        viewHolder.contentTV.setText(content);
        viewHolder.picView.setImageBitmap(imageBitMap);

        //You can add OnClickListeners here!!!!
    }

    @Override
    public int getItemCount(){
        return posts.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            /*
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            */

            try{
                URL url = new URL(urldisplay);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream input = con.getInputStream();
                mIcon11 = BitmapFactory.decodeStream(input);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("Error",e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error",e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(result == null){

            }else{
                bmImage.setImageBitmap(result);
            }
        }
    }

}
