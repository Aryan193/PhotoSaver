package com.example.photosaver;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyImagesViewModel extends AndroidViewModel {

    private MyImagesRepository repository;//creating object for Repository class
    private LiveData<List<MyImages>> imagesList;//creating an array to store notes

    public MyImagesViewModel(@NonNull Application application) {
        super(application);
        //defining above two inside the constructor
        repository=new MyImagesRepository(application);
        imagesList= repository.getAllImages();
    }
    //defining the insert method
    public  void insert(MyImages myImages){
        repository.insert(myImages);
    }
    //defining the delete method
    public  void delete(MyImages myImages){
        repository.delete(myImages);
    }
    //defining the update method
    public  void update(MyImages myImages){
        repository.update(myImages);
    }
    public LiveData<List<MyImages>> getAllImages(){
        return imagesList;
    }
}
//Note-After creating this class we must show this view model as a refrence in the main Activity
//after creating this class go to mainActivity and write the neccessay code
