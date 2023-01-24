package com.mihir.branchtask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.branchtask.API_FAILED
import com.mihir.branchtask.API_SUCCESS
import com.mihir.branchtask.network.AppObjectController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application): AndroidViewModel(application)  {

    var apiStatus = MutableSharedFlow<String?>(replay = 0)
    var authToken = MutableSharedFlow<String?>(replay = 0)

    fun login(userEmail:String, pass:String){
        viewModelScope.launch(Dispatchers.IO) {
            val resp = AppObjectController.service.getLoginCredentials(userEmail,pass)
            if (resp.isSuccessful){
                authToken.emit(resp.body()?.get("auth_token"))
                apiStatus.emit(API_SUCCESS)
            }else{
                apiStatus.emit(API_FAILED)
            }
        }
    }

}