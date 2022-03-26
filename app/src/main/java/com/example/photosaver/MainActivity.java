package com.example.photosaver;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyImagesViewModel myImagesViewModel;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ActivityResultLauncher<Intent>activityResultLauncherForAddImage;
    private ActivityResultLauncher<Intent>activityResultLauncherForUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv=findViewById(R.id.rv);
        fab=findViewById(R.id.floatingActionButton);

        //register Activity for AddImage
        //this method has already been created below
        registerActivityForAddImage();

        //register Activity for AddImage
        //this method has already been created below
        registerActivityForUpdateImage();

        //below three lines are for Adapter
        rv.setLayoutManager(new LinearLayoutManager(this));
        MyImagesAdapter adapter=new MyImagesAdapter();
        rv.setAdapter(adapter);

        myImagesViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyImagesViewModel.class);
        //this method will observe the changes in the live data

        myImagesViewModel.getAllImages().observe(this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {
                //update recycler view after making the adapter
                 adapter.setImagesList(myImages);

            }
        });

        //for Floating button

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddImageActivity.class);
                //here we will again start the intent for result by using ActivityResultLauncher
                activityResultLauncherForAddImage.launch(intent);
            }
        });
        //for deleting
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            //this is for drag and drop method
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //this is for swipe

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             //firstly we need to determine that which data user wants to delete
                //adapter.getPosition  is created in Adapter class which is marked with one
                myImagesViewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(rv);//this is for attaching this method with recycler view

        //(update Portion) sending the data to updateImageActivity
        adapter.setListener(new MyImagesAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(MyImages myImages) {
                Intent intent=new Intent(MainActivity.this,UpdateImageActivity.class);
                intent.putExtra("id",myImages.getImage_id());
                intent.putExtra("title",myImages.getImage_title());
                intent.putExtra("description",myImages.getImage_description());
                intent.putExtra("image",myImages.getImage());
                //now here we are going to start intent for a result so,we will call activityResultLauncher
                activityResultLauncherForUpdateImage.launch(intent);

            }
        });
    }
    //for Updating
    public void registerActivityForUpdateImage(){
        activityResultLauncherForUpdateImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
             int resultCode= result.getResultCode();
            Intent data=result.getData();

                if(resultCode == RESULT_OK && data !=null){
                    String title=data.getStringExtra("updateTitle");
                    String description= data.getStringExtra("updateDescription");
                    byte[] image= data.getByteArrayExtra("image");
                    int id= data.getIntExtra("id",-1);

                    //now create a object for MyImages.Class
                    MyImages myImages=new MyImages(title,description,image);
                    myImages.setImage_id(id);
                    //Now,save the data using ViewObject Model
                    myImagesViewModel.update(myImages);
                    //Note after saving the data we need to show the data on the screen
                    // for that we need to write code in Adapter class in OnBindViewHolder
                }
            }
        });
    }
    //for Adding
    public void registerActivityForAddImage(){
      activityResultLauncherForAddImage=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
          @Override
          public void onActivityResult(ActivityResult result) {
           //this is for getting the sent data from AddImageActivity.class
              //Note:-Sometimes user get back without actually selecting the images so the app may crash
              //so we need to check the results and get them into if else structure
              int resultCode=result.getResultCode();
              Intent data=result.getData();
              if(resultCode == RESULT_OK && data !=null){
                  String title=data.getStringExtra("title");
                  String description= data.getStringExtra("description");
                  byte[] image= data.getByteArrayExtra("image");

                  //now create a object for MyImages.Class
                  MyImages myImages=new MyImages(title,description,image);
                  //Now,save the data using ViewObject Model
                  myImagesViewModel.insert(myImages);
                  //Note after saving the data we need to show the data on the screen
                  // for that we need to write code in Adapter class in OnBindViewHolder
              }
          }
      });
    }
    }
