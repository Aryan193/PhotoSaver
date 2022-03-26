package com.example.photosaver;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.MyImagesHolder>{

    //create the array that will get the data that we want to transfer to the database
    List<MyImages> imagesList=new ArrayList<>();

    //object for OnImageClickListener
    private OnImageClickListener listener;
   //setter
    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }
    //now create the setter method of the array
    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        //write this line after creating the adapter and adding it to MainActivity file
        notifyDataSetChanged();
    }
    //it is for click feature for updating the list item
    public interface OnImageClickListener{
        void onImageClick(MyImages myImages);
        //after creating this interface create an object above
    }

    //1
    public MyImages getPosition(int position){
        return imagesList.get(position);
    }

    @NonNull
    @Override
    public MyImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card,parent,false);
        return new MyImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImagesHolder holder, int position) {

        //this method body to be written after saving  the data
        MyImages myImages=imagesList.get(position);
        holder.textViewTitle.setText(myImages.getImage_title());
        holder.textViewDescription.setText(myImages.getImage_description());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(),0, myImages.getImage().length));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    //now create the class under which we will define the cardView components

    public class MyImagesHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle,textViewDescription;

        public MyImagesHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imageView);
            textViewTitle=itemView.findViewById(R.id.textViewTitle);
            textViewDescription=itemView.findViewById(R.id.textViewDescription);

            //this code is for updating and this will be coded after creating the OnImageClickListener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //getting the position
                    int position=getAdapterPosition();
                    //structuring it with with if else to check that if it it is null

                    if(listener !=null && position != RecyclerView.NO_POSITION){
                        listener.onImageClick(imagesList.get(position));
                        //after doing this go to mainActivity and send the data to the UpdateImageActivity
                    }
                }
            });
        }
    }
}
