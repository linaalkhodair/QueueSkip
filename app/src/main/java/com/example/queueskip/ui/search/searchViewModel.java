package com.example.queueskip.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class searchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public searchViewModel() {
        mText = new MutableLiveData<>();
        //   mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}


