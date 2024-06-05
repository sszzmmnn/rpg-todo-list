package com.example.rpgtodolist;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatsViewModel extends ViewModel {

    final MutableLiveData<int[]> statsArray = new MutableLiveData<>();

    public void setStatsArray(int[] array) {
        statsArray.setValue(array);
    }

    public LiveData<int[]> getStatsArray() {
        return statsArray;
    }
}
