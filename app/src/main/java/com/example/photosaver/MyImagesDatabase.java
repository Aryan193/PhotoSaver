package com.example.photosaver;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MyImages.class},version = 1)//this will link the note table to the database
public abstract class MyImagesDatabase extends RoomDatabase {
    private static MyImagesDatabase instance;//this instance will enable us to use this NoteDatabase from anywhere
    public abstract MyImagesDao myImagesDao();


    //this will create a instance of roomdatabase(NoteDatabase)
    //synchronized : only one thread can access this method and is safe
    //fallbacktodestructivemigration : to handle versions
    public static synchronized MyImagesDatabase getInstance(Context context){
        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext()
                    , MyImagesDatabase.class, "my_images_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}
