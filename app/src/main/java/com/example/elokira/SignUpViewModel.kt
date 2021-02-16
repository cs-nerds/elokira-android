package com.example.elokira

import User
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class SignUpViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    sealed class LoginResult {
        object EmailMissing : LoginResult()

        object PasswordMissing : LoginResult()

        class NetworkError(val userMessage: String) : LoginResult()

        object NetworkFailure : LoginResult()

        object Success : LoginResult()
    }


    private val loginResultEmitter = EventEmitter<LoginResult>()
    val loginResult: EventSource<LoginResult> = loginResultEmitter



    init{
        Log.i("sign up ViewModel", " View model created")
    }

    fun signUpUser(userEntry: User){
        //check if fields are empty
        if(userEntry.email.isEmpty()){
            loginResultEmitter.emit(LoginResult.EmailMissing)
        }
        if(userEntry.password.isEmpty()){
            loginResultEmitter.emit(LoginResult.PasswordMissing)
        }
    }

    fun addUser(){
        //add user to database via an API
    }

    override fun onCleared() {
        super.onCleared()
    }


}