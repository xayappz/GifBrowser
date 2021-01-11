package com.xay.gifbrowser.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xay.gifbrowser.utils.Images;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private ImagesRepositoy repositoy;

    private MutableLiveData<ArrayList<Images>> data=new MutableLiveData<>();

    public HomeViewModel()
    {

    }

    public void init() {

        if (data == null) {
            return;
        }
        repositoy = ImagesRepositoy.getInstance();
        data = repositoy.getIMAGES();
    }

    public MutableLiveData<ArrayList<Images>> getIMAGES() {
        return data;
    }


}