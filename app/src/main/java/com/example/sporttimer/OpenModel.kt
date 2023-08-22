package com.example.sporttimer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class OpenModel: ViewModel() {

    //Для MainWindow
    val allTime: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val switch1Status: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val workTime: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val restTime: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


}