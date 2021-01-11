package com.xay.gifbrowser.adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xay.gifbrowser.R;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyofflineAdap extends RecyclerView.Adapter<MyofflineAdap.MyViewHolder> {

    private Context mContext;

    private ArrayList<String> imagesList;
    ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.imgg);
        }
    }


    public MyofflineAdap(Context mContext, ArrayList<String> imagesList) {
        this.mContext = mContext;
        this.imagesList = imagesList;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("SAVEDDDD", imagesList + "");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_custom, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String images = imagesList.get(position);

        Log.e("adaptersavedimage", images + ".");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
//
//        progressDialog.show();
//        Glide.with(mContext).load("file://" + images).
//                into(holder.thumbnail);

        holder.thumbnail.setImageURI(Uri.fromFile(new File(images)));

        progressDialog.dismiss();

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.fullview, null);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(view);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                ImageView close = dialog.findViewById(R.id.close);
                ImageView fullimg = dialog.findViewById(R.id.fullimg);
                ShareGif(images);
                //    holder.thumbnail.setImageURI( Uri.fromFile(new File(images)));

                Glide.with(mContext).load("file://" + images).
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


    private void ShareGif(String img) {
        String s = img;
        String result[] = s.split("/");

        String returnValue = result[result.length - 1];
        Log.e("imgname", returnValue);
        String baseDir = "/storage/emulated/0/Android/data/com.example.gifbrowser/files/.Gifs/";
        String fileName = returnValue;

        File sharingGifFile = new File(baseDir, fileName);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/gif");
        Uri uri = Uri.fromFile(sharingGifFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share image"));

    }


}