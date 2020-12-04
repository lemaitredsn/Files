package ru.lemaitre.filelesson

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel : ViewModel() {


    private val repository = Repository()

    private val loadingMutableLiveData = MutableLiveData(false)
    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData


    private var stateMutableLiveData = MutableLiveData<String>()
    val stateLiveData: LiveData<String>
        get() = stateMutableLiveData


    fun downloadFile(link: String) {
        viewModelScope.launch {
            loadingMutableLiveData.postValue(true)
            try {
                repository.downloadFileExample(link = link)
                //if success saveInSharedPreferences
                repository.saveInSharedPreference(link = link)
            } catch (t: Throwable) {
                Log.e("TAG", "exception ${t.message}", t)
            } finally {
                loadingMutableLiveData.postValue(false)
                stateMutableLiveData.postValue(repository.getState())
            }
        }
    }

    fun checkOnFirstLaunch(){
        repository.checkFirstLaunch()
    }

}