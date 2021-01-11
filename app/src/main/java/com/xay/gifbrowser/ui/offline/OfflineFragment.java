package com.xay.gifbrowser.ui.offline;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xay.gifbrowser.R;
import com.xay.gifbrowser.adapters.MyofflineAdap;

import java.io.File;
import java.util.ArrayList;

public class OfflineFragment extends Fragment {
    private ArrayList<String> imagesList = new ArrayList<>();
    private MyofflineAdap myofflineAdap;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.offline_layout, container, false);
        recyclerView = root.findViewById(R.id.my_off_rec);
        readSavedGifs();
        showGifs();
        return root;

    }

    private void showGifs() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView


        myofflineAdap = new MyofflineAdap(getContext(), imagesList);
        recyclerView.setAdapter(myofflineAdap); // set the Adapter to RecyclerView}

    }

    private void readSavedGifs() {
        File path = new File("/storage/emulated/0/Android/data/com.example.gifbrowser/files/.Gifs/");
        if (path.exists()) {
            String[] fileNames = path.list();
            for (int i = 0; i < fileNames.length; i++) {
                Log.e("ABSLUTE", path.getAbsolutePath());

                imagesList.add(path + "/" + fileNames[i]);


            }
        }

    }


}