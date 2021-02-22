package com.example.elokira.viewmodels

import Authenticate
import LoginRequest
import ResultObserver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class LogInViewModel : ViewModel() {

    init {
        Log.i("Login user", "View model created")
    }
    val loginResultEmitter = EventEmitter<ResultObserver>()
    val loginResult: EventSource<ResultObserver> = loginResultEmitter

    fun loginUser(loginDetails: LoginRequest){
        if(loginDetails.idNumber.isEmpty()){
            loginResultEmitter.emit(ResultObserver.IdNoMissing)
        }
    }

}