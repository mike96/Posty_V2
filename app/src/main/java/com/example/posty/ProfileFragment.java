package com.example.posty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private static int RESULT_LOAD_IMAGE = 2;
    private static String saveFileName = "SaveName.txt";
    private static String savePicName = "SavePic.txt";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //final String saveFileName = "SaveName.txt";

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        //=======================Initialize - For Debugging=============================================
        initializeProfile();
        //===========================================================================================

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try{
            Context fileContext = view.getContext();
            FileInputStream inputStream = fileContext.openFileInput(saveFileName);
            int c;
            String reference = "|";
            String username = "";
            String displayName = "";
            String single;
            int selector = 0;
            while ((c = inputStream.read()) != -1){
                single = Character.toString((char) c);
                if(single.equals(reference)){
                    selector = selector + 1;
                    single = "";
                }
                if(selector == 0){
                    username = username + single;
                }
                else if (selector == 1){
                    displayName = displayName + single;
                }else{
                    //imageString = imageString + single;
                    //TODO: Decode from Base64 to Bitmap
                }
            }
            EditText usernameET = view.findViewById(R.id.usernameET);
            EditText displayNameET = view.findViewById(R.id.displayNameET);


            usernameET.setText(username);
            displayNameET.setText(displayName);

        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            Context fileContext = view.getContext();
            FileInputStream picInputStream = fileContext.openFileInput(savePicName);
            int c;

            Log.e("Loading Picture","Checkpoint 1");

            //loadImageFromStorage("/data/user/0/com.example.posty/app_imageDir");
            /*
            try {
                String path = "/data/user/0/com.example.posty/app_imageDir";
                File f=new File(path, "profile.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                CircleImageView img= view.findViewById(R.id.circularProfileImageView);
                img.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            */

            String imageString = "";
            String single;
            while ((c = picInputStream.read()) != -1) {
                single = Character.toString((char) c);
                imageString = imageString + single;
            }

            /*
            if(imageString != ""){
                byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                Log.e("Loading Picture","CheckPoint 2");
                Log.e("BITMAP",decodedByte.toString());

                CircleImageView circleImageView = view.findViewById(R.id.circularProfileImageView);
                circleImageView.setImageBitmap(decodedByte);
            }
            */

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button changePic = view.findViewById(R.id.changeProfilePicBtn);
        changePic.setOnClickListener(new View.OnClickListener() {
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

        //TODO: Build Section
        Button updateBtn = (Button)view.findViewById(R.id.updateProfileButton);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameET = view.findViewById(R.id.usernameET);
                EditText displayNameET = view.findViewById(R.id.displayNameET);

                String username = usernameET.getText().toString();
                String displayName = displayNameET.getText().toString();
                String image_str = "";
                String bitmapString = "";

                CircleImageView imageView = view.findViewById(R.id.circularProfileImageView);
                try{
                    Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                    saveToInternalStorage(image);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    //FileOutputStream stream = new FileOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
                    byte [] byte_arr = stream.toByteArray();
                    image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                    //bitmapString = image.toString();
                    byte[] bytes = image_str.getBytes("UTF-8");
                    Log.e("Image Bitmap",image_str);
                    Log.e("IMAGE Bytes",bytes.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }


                String saveString = username+"|"+displayName;

                //TODO: check database to confirm username not taken

                Context fileContext = v.getContext();
                try {
                    FileOutputStream outputStream = fileContext.openFileOutput(saveFileName, Context.MODE_PRIVATE);
                    outputStream.write(saveString.getBytes("UTF-8"));
                    Log.e("Display Name Bytes",saveString.getBytes("UTF-8").toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream outputStream = fileContext.openFileOutput(savePicName, Context.MODE_PRIVATE);
                    outputStream.write(image_str.getBytes("UTF-8"));
                    Log.e("Display Bytes",image_str.getBytes("UTF-8").toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("UPDATE STATUS","COMPLETE");


            }
        });

//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void initializeProfile(){
        String saveString = "";
        Context fileContext = getContext();
        try {
            FileOutputStream outputStream = fileContext.openFileOutput(savePicName, Context.MODE_PRIVATE);
            outputStream.write(saveString.getBytes("UTF-8"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("Directory",directory.getAbsolutePath().toString());
        //return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            View v = getView();
            CircleImageView img= v.findViewById(R.id.circularProfileImageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
