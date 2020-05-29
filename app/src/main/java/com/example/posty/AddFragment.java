package com.example.posty;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AddFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add, container, false);

        Button addPic = view.findViewById(R.id.addPicBtn);
        Button addBtn = (Button)view.findViewById(R.id.addNewPostBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText contentET = view.findViewById(R.id.enterContentET);

                //Get Text from the editText
                String content = contentET.getText().toString();

                Context context = getContext().getApplicationContext();
                Toast toast = Toast.makeText(context, "Post added", Toast.LENGTH_SHORT);
                toast.show();

                String username = "";
                String displayName = "";

                //Get username and DisplayName from internal storage
                try{
                    Context fileContext = view.getContext();
                    FileInputStream inputStream = fileContext.openFileInput("SaveName.txt");
                    int c;
                    String reference = "|";

                    String single;
                    int selector = 0;
                    while ((c = inputStream.read()) != -1){
                        single = Character.toString((char) c);
                        if(single.equals(reference)){
                            selector = 1;
                            single = "";
                        }
                        if(selector == 0){
                            username = username + single;
                        }
                        else{
                            displayName = displayName + single;
                        }
                    }

                }catch(IOException e){
                    e.printStackTrace();
                }

                //int position = MainActivity.posts.size();
                //MainActivity.posts.add(position, tempPost);

                contentET.setText(null);



                ImageView imageView = view.findViewById(R.id.myImageView);
                String image_str = "";
                try{
                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
                    byte [] byte_arr = stream.toByteArray();
                    image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    Log.e("Image size",Integer.toString(image_str.length()));
                    Log.e("Picture String",image_str);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("PIC STATUS","PIC FAILED TO UPLOAD");
                }

                /* DEBUG - RESET THE IMAGE WITH BASE64 DATA
                try{
                    imageView.setImageBitmap(null);
                    byte[] newbytes = Base64.decode(image_str.getBytes(), Base64.DEFAULT);
                    Bitmap newBitmap = BitmapFactory.decodeByteArray(newbytes, 0, newbytes.length);
                    imageView.setImageBitmap(newBitmap);
                    Log.e("Reset Image","Success");
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Reset Image Error",e.getMessage());
                }
                */
                imageView.setImageBitmap(null);

                String tableSize = Integer.toString(MainActivity.posts.size());
                //String picName = (username+tableSize+".png");
                String picName = (username+tableSize+".png");
                //String hardCode = "displayName=CoronaCaptain Jack&username=jack&content=Fuck a Quarantine";
                // format for php POST to read in data
                String postString = "displayName="+displayName+"&username="+username+"&content="+content+"&pic="+image_str+"&picName="+picName;
                Log.e("Image size",Integer.toString(image_str.length()));
                Log.e("Before addPost",postString);

                //=============================Try adding to Database=============================
                addPost(MainActivity.DATABSE_WRITE, postString);
                /*
                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Uploading Image...");
                pd.show();
                */



                /*
                try{
                    URL url = new URL(MainActivity.DATABSE_WRITE);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    //con.connect();


                    //String query = "INSERT INTO simple_post(displayName, username, content) VALURES ('CoronaCaptain Jack','jack','This quarantine sucks')";
                    String query = "[{'displayName':'CoronaCaptain Jack','username':'jack','content':'Fuck a quarantine'}]";

                    OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                    Log.e("Progress","Good so far...");
                    out.write(jsonString);

                    //out.flush();
                }catch (Exception f){
                    f.printStackTrace();
                    Log.e("ADD ERROR","We got an error Cap'n");
                }
                */
            }
        });

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Context context = getContext();
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    }
                    //Log.e("PIC SUCCESS",Integer.toString(RESULT_LOAD_IMAGE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //TODO: Add functionality for adding pictures, remove name and username fields

        return view;
    }


    private void addPost(final String UrlWebService, final String postJSON){

        class AddPost extends AsyncTask<Void, Void, String>{


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                HttpURLConnection urlConnection;

                try{
                    URL url = new URL(UrlWebService);
                    Log.e("Progress","Good so far...");
                    Log.e("JSON",postJSON);
                    urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                    writer.write(postJSON);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    //RESPONSE SECTION
                    InputStream inputStream;
                    if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
                        inputStream = urlConnection.getInputStream();
                    } else{
                        inputStream = urlConnection.getErrorStream();
                    }

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String temp, response = "";
                    while ((temp = bufferedReader.readLine()) != null){
                        response += temp;
                    }
                    Log.e("RESPONSE",response);
                    return response;


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("ERROR","Mission Failed. We'll get em next time.");
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result){
                //Log.e("RESULT",result);
            }
        }
        AddPost newPost = new AddPost();
        newPost.execute();
    }



}
