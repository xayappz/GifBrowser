package com.xay.gifbrowser.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.xay.gifbrowser.R;
import com.xay.gifbrowser.utils.Images;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyGifAdapter extends RecyclerView.Adapter<MyGifAdapter.MyViewHolder> {
ProgressDialog p;
    private Context mContext;
    int pos = 0;
    private ArrayList<Images> imagesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public void setImages(ArrayList<Images> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public MyGifAdapter(Context mContext, ArrayList<Images> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
        this.imagesList = new ArrayList<>();
        p=new ProgressDialog(mContext);
        p.setCancelable(false);
        p.setTitle("Loading");
        p.show();;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Images images = imagesList.get(position);

        // loading gifs using Glide library
        Log.e("adapterimage", images.getUrl() + ".");
        Glide.with(mContext).load(images.getUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA)).apply(new RequestOptions()
                .placeholder(R.drawable.load)
        ).
                into(holder.thumbnail);

        Glide.with(mContext).asFile()
                .load(images.getUrl())
                .apply(new RequestOptions()
                        .format(DecodeFormat.DEFAULT)
                        .override(Target.SIZE_ORIGINAL))
                .into(new Target<File>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        p.show();

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {

                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {

                        storeImage(resource, position);
                        p.dismiss();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogview = inflater.inflate(R.layout.fullview, null);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(dialogview);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                ImageView close = dialog.findViewById(R.id.close);
                ImageView fullimg = dialog.findViewById(R.id.fullimg);
                Log.e("FULLIMGG", imagesList.get(pos).getUrl());
                Glide.with(mContext).load(imagesList.get(pos).getUrl()).
                        into(fullimg);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {

        return imagesList.size();
    }

    private void storeImage(File image, int posa) {
        File pictureFile = getOutputMediaFile(posa);

        if (!readSavedGifs().contains(String.valueOf(posa))) {

            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream output = new FileOutputStream(pictureFile);
                FileInputStream input = new FileInputStream(image);

                FileChannel inputChannel = input.getChannel();
                FileChannel outputChannel = output.getChannel();

                inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                output.close();
                input.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in Downloading", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in Downloading", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private File getOutputMediaFile(int posa) {
        File mediaStorageDir = new File("/storage/emulated/0/Android/data/com.example.gifbrowser/files/.Gifs/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs())
                return null;
        }

        DateFormat df = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + posa + ".gif");
        return mediaFile;
    }

    private String readSavedGifs() {

        String FILE_name = "";
        File path = new File("/storage/emulated/0/Android/data/com.example.gifbrowser/files/.Gifs/");
        if (path.exists()) {
            String[] fileNames = path.list();
            for (int i = 0; i < fileNames.length; i++) {
                Log.e("ABSLUTE", path.getAbsolutePath());

                FILE_name = path + "/" + fileNames[i];


            }
        }
        return FILE_name;
    }
}