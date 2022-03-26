package com.example.photosaver;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AddImageActivity extends AppCompatActivity {

    private ImageView imageViewAddImage;
    private EditText editTextAddTitle,editTextAddDescription;
    private Button buttonSave;

     //this ActivityResultLauncher is for the requested permission
    ActivityResultLauncher<Intent>activityResultLauncherForSelectImage;

    //this is for converting images to bit map
    private Bitmap selectedImage;
    //this is for using the makeSmall Method that we have made for decreasing the dimensions
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getSupportActionBar().setTitle("Add Image");

        // Registration Process For ActivityResultLauncher
        registerActivityForSelectImage(); //this method has already been created below


        imageViewAddImage=findViewById(R.id.imageView2);
        editTextAddTitle=findViewById(R.id.editTextTitle);
        editTextAddDescription=findViewById(R.id.editTextDescription);
        buttonSave=findViewById(R.id.button);



        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(AddImageActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    //this below code will ask user for permission if it has not been granted
                    //we have used String[] here so that we can request multiple permission
                    ActivityCompat.requestPermissions(AddImageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else{
                    //if user have given permission than here we can access media folder
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    //ActivityResultLauncher
                    activityResultLauncherForSelectImage.launch(intent);
                }

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an if else structure
                // so that if user click the save button without selecting the images the application does not crashes
                if (selectedImage ==null){
                    Toast.makeText(AddImageActivity.this,"Please Select Image",Toast.LENGTH_SHORT).show();
                }
                else {
                    //take the title description and image when clicking save
                    String title = editTextAddTitle.getText().toString();
                    String description = editTextAddDescription.getText().toString();
                    //we cannot take the image directly.So,convert it to byte and for this we will use ByteArrayOutputStream Class
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    //if we use only .compress method it  will only compress the image
                    //so for that we need to create method to cut the dimensions which is created at the end (makeSmall)
                    scaledImage = makeSmall(selectedImage, 300);
                    //compress the image
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    //now create array of byte types and convert the image to byteArray
                    byte[] image = outputStream.toByteArray();

                    //sending the data to mainActivity to save
                    Intent intent=new Intent();
                    intent.putExtra("title",title);
                    intent.putExtra("description",description);
                    intent.putExtra("image",image);
                    setResult(RESULT_OK,intent);
                    finish();


                }
            }
        });
    }
    //method made for registering the ActivityResultLauncher
    public void registerActivityForSelectImage(){
        activityResultLauncherForSelectImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                //here we will check that wether has chosen image or not
                int resultCode=result.getResultCode();
                // taking the data that is result of intent
                Intent data=result.getData();
                //now check the result is equal to the result code
                if(resultCode ==RESULT_OK && data !=null){

                    try {
                        //to convert the picture to bitmap
                        selectedImage= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                        //now place the selected image in imageView
                        imageViewAddImage.setImageBitmap(selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    //if user has allowed the permission than this method will access the media folder

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==1 && grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            //ActivityResultLauncher
            activityResultLauncherForSelectImage.launch(intent);
        }
    }
    //this method is for decreasing the dimensions of the image
    public Bitmap makeSmall(Bitmap image,int maxSize){
        int width=image.getWidth();
        int height= image.getHeight();

        //this is for cutting the length of the width and height of the image
        float ratio=(float) width/(float) height;
        if(ratio>1){
            width=maxSize;
            height=(int) (width/ratio);

        }
        else{
            height=maxSize;
            width=(int) (height*ratio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);

    }
}
//do not forget to set MainActivity as Parent Activity in manifest file