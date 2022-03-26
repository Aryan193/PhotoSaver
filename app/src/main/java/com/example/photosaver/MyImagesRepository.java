package com.example.photosaver;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDao myImagesDao;//created the object from the interface MyImagesDao
    private LiveData<List<MyImages>> imagesList;
    ExecutorService executors= Executors.newSingleThreadExecutor();//using this class we can do four operations on a different thread

    //constructor
    public MyImagesRepository (Application application){
        //we cannot call the abstract method from anywhere else because it has no body
        //but since we have created the instance of NoteDatabase in the class NoteDatabase .so,room created neccessary code
        //and so we can call the noteDoa method here

        MyImagesDatabase database=MyImagesDatabase.getInstance(application);
        myImagesDao= database.myImagesDao();
        imagesList= myImagesDao.getAllImages();
    }

    public void insert(MyImages myImages){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.insert(myImages);
            }
        });

    }
    public void delete(MyImages myImages){

        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.delete(myImages);
            }
        });
    }
    public void update(MyImages myImages){

        executors.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDao.update(myImages);
            }
        });
    }
    public LiveData<List<MyImages>> getAllImages(){
        return imagesList;
    }
}

//for insert,delete,update we need to create backgrounds, because room database does not allow us
//to do data database operations with the main threads .if we will do that the application will crash

//to do that we can also use the method (.allowMainThreadQueries()) inside synchronised method in noteDatabase class
// but it's not  the recommended one because it may cause some problem with the user interface

// so to do that without main thread we will use Executer class
