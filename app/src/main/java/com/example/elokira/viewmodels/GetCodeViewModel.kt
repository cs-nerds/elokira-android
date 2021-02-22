package com.example.elokira.viewmodels

import Authenticate
import ResultObserver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class GetCodeViewModel : ViewModel() {
   init {
       Log.i("Authenticate User", "GetCodeViewModel created")
   }
    val authenticateResultEmitter = EventEmitter<ResultObserver>()
    val authenticationResult: EventSource<ResultObserver> = authenticateResultEmitter

    fun authenticateUserCode(authenticate: Authenticate){
        if(authenticate.loginCode?.isEmpty() == true){
            authenticateResultEmitter.emit(ResultObserver.CodeMissing)
        }
    }
}