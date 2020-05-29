package com.example.posty;

//======================================NOT ANDROID X VERSION =======================================

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Post> posts = new ArrayList<>();
    private ActionBar toolbar;

    private static int RESULT_LOAD_IMAGE = 1;

    public static String DATABASE_URL = "http://192.168.1.132/api/stock_service.php";
    public static String DATABSE_WRITE = "http://192.168.1.132/api/add_simple.php";
    //public static String DATABASE_URL = "https://developer.android.com/reference/java/net/HttpURLConnection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //int resID = getResources().getIdentifier("startup_sound.mp3", "raw", getPackageName());
        //MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
        MediaPlayer mediaPlayer=MediaPlayer.create(this,R.raw.startup_sound);
        mediaPlayer.start();

        posts.clear();

        toolbar = getSupportActionBar();
        toolbar.setTitle("Home");

        /*  ================ Initializes "posts" with 2 dummy posts. No longer needed ==============
        Post dummyPost = new Post("@michael", "Michael, The Creator", "This is my dummy post.");
        posts.add(0,dummyPost);

        Post dummyPost2 = new Post("@nate", "Nate the great", "Multiple linesssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        posts.add(1, dummyPost2);
        *///========================================================================================

        loadFragment(new HomeFragment());

        BottomNavigationView navigation = findViewById(R.id.mainNavView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //TODO: Finish building and test database interface
        //downloadJSON(DATABASE_URL); //CHANGE THIS TO ACTUAL IP

    }

    /*
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
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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
                    URL url = new URL(DATABASE_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //String responeMsg = con.getResponseMessage().toString();
                    //Log.e("CONN Status",responeMsg);
                    //con.setRequestMethod("GET");
                    //con.setConnectTimeout(3000);
                    //con.connect();
                    Log.e("CONN STATUS",con.toString());
                    StringBuilder sb = new StringBuilder();
                    //==================DEBUG=======================================
                    try{
                        InputStream in = new BufferedInputStream(con.getInputStream());
                        if(in.available()>-1){
                            Log.e("ConnStatus","Connection Success!");
                        }
                        else{
                            Log.e("ConnStatus","Connection Failed");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("ConnStatus","NO CONNECTION");
                    }
                    //================================================================
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    int i = 0;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                        i = i +1;
                    }
                    Log.e("MyCounter","Count is: " + Integer.toString(i));
                    Log.e("MyJSON",sb.toString());
                    Log.e("MyJSONTrimmed",sb.toString().trim());
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Android Error",e.getMessage());
                    Log.e("JSON ERROR","Unable to load JSON");
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
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String tempDispName = obj.getString("displayName");
            String tempUsername = obj.getString("username");
            String tempContent = obj.getString("content");
            Post tempPost = new Post(tempUsername,tempDispName,tempContent);
            posts.add(i,tempPost);
            post[i] = obj.getString("displayName") + " " + obj.getString("username") + " " + obj.getString("content");
        }
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        //listView.setAdapter(arrayAdapter);
        Log.e("DisplayName",post[0].toString());
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if (post.length>0){
            Toast toast = Toast.makeText(context, post[0], duration);
            toast.show();
        }else{
            Toast toast = Toast.makeText(context, "Error getting posts", duration);
            toast.show();
        }

    }
    //
    */


    //================ BOTTOM NAVIGATION SETUP =========================
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                    toolbar.setTitle("Search");
                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_add:
                    toolbar.setTitle("Add");
                    //Intent ADD = new Intent(getApplicationContext(), ADD_ITEMS_PRACTICE_ACTIVITY.class);
                    //startActivity(ADD);
                    fragment = new AddFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Notifications");
                    fragment = new NotificationsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    //======================== LOAD FRAGMENT =============================
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.e("REQUEST CODE",Integer.toString(requestCode));
        //Log.e("RESULT CODE",Integer.toString(resultCode));
        //Log.e("DATA",data.toString());
        if (resultCode == RESULT_OK && null != data) {//requestCode == RESULT_LOAD_IMAGE &&
        //if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            if(requestCode==1){
                ImageView imageView = (ImageView) findViewById(R.id.myImageView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }else if(requestCode ==2){
                CircleImageView profileImageView = findViewById(R.id.circularProfileImageView);
                profileImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }



        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                } else {
                    Log.e("PERMISSION","Permission Denied: No Access to external storage");
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }
}
