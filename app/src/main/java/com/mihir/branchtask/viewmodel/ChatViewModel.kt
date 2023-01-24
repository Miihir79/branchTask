package com.mihir.branchtask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.branchtask.API_FAILED
import com.mihir.branchtask.AUTH
import com.mihir.branchtask.network.AppObjectController
import com.mihir.branchtask.model.Message
import com.mihir.branchtask.model.MessageItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ChatViewModel(application: Application): AndroidViewModel(application)   {

    var apiStatus = MutableSharedFlow<String?>(replay = 0)

    val messages = MutableSharedFlow<Message?>(replay = 0)

    val message = MutableSharedFlow<MessageItem?>(replay = 0)

    fun getMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            val resp = authToken?.let { AppObjectController.service.getMessages(it) }
            if (resp != null) {
                if (resp.isSuccessful){
                    messages.emit(resp.body())
                }else{
                    apiStatus.emit(API_FAILED)
                }
            }
        }
    }

    fun sendMessages(threadId: Int, messageToSend: String){
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            val resp = authToken?.let { AppObjectController.service.sendMessage(it,threadId,messageToSend) }
            if (resp != null) {
                if (resp.isSuccessful){
                    message.emit(resp.body())
                }else{
                    apiStatus.emit(API_FAILED)
                }
            }
        }
    }

    fun reset() {
        viewModelScope.launch(Dispatchers.IO) {
            AppObjectController.remove(getApplication<Application>().applicationContext)
            val authToken = AppObjectController.read(getApplication<Application>().applicationContext, AUTH)
            authToken?.let { AppObjectController.service.resetApi(it) }
        }
    }

}