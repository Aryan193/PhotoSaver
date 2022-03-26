package com.example.photosaver;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_images") //this is room annotation process this will enable the room database
public class MyImages {

    @PrimaryKey(autoGenerate = true)//id to be primary key and by autoGenerate= true database will assign the id itself
    public int image_id;
    public String image_title;
    public String image_description;
    public byte[] image;

    //for id we have not created constructor because it will be generated automatically
    public MyImages(String image_title, String image_description, byte[] image) {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image = image;
    }

    //generating getter for all four


    public int getImage_id() {
        return image_id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public byte[] getImage() {
        return image;
    }

    //generating setter for int image_id;

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
