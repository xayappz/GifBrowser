package com.xay.gifbrowser.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.xay.gifbrowser.R;
import com.xay.gifbrowser.adapters.MyGifAdapter;
import com.xay.gifbrowser.ui.offline.NoInternet;
import com.xay.gifbrowser.utils.FragmentChangeer;
import com.xay.gifbrowser.utils.Images;
import com.xay.gifbrowser.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Images> imagesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private MyGifAdapter myGifAdapter;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.my_recycler_view);
        if (!InternetConnection.checkConnection(getActivity())) {
            FragmentChangeer.Frags(getActivity(), new NoInternet());


        }
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        new ImagesRepositoy(getActivity());
        homeViewModel.init();

        homeViewModel.getIMAGES().observe(getActivity(), new Observer<ArrayList<Images>>() {
            @Override
            public void onChanged(ArrayList<Images> s) {
                myGifAdapter.setImages(s);
                myGifAdapter.notifyDataSetChanged();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        myGifAdapter = new MyGifAdapter(getContext(), homeViewModel.getIMAGES().getValue());
        recyclerView.setAdapter(myGifAdapter); // set the Adapter to RecyclerView

        return root;
    }
}