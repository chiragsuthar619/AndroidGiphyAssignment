package com.example.testoftest.dashboard.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidassignment.network.LiveDataWrapper
import com.example.testoftest.base.BaseViewModel
import com.example.testoftest.dashboard.pojo.Gifs

class Gifsviewmodel(application: Application) : BaseViewModel(application)  {

    var gifsviewmodel_list = MutableLiveData<LiveDataWrapper<Gifs>>()


    /**
     * Get UserInfo list
     * @param appliedForce: If API call is forcefully applied
     */

    fun getGifs(appliedForce : Boolean,map: HashMap<String,String>) : LiveData<LiveDataWrapper<Gifs>> {

        if(gifsviewmodel_list.value == null || appliedForce){
            gifsviewmodel_list = callAPI(webservice?.gifsinfo(map)!!)
        }
        return gifsviewmodel_list
    }



}
