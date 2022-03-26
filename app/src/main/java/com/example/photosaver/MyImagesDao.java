package com.example.photosaver;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyImagesDao {
    @Insert
    void insert(MyImages myImages);

    @Delete
    void delete(MyImages myImages);

    @Update
    void update(MyImages myImages);



    @Query("SELECT * FROM my_images ORDER BY image_id ASC")

        //so to write the  return type  and method name under this query
        //List<Note> will be the return type
        //LiveData will observe the database and reflect the changes to the recyclerView

    LiveData<List<MyImages>> getAllImages();
}
