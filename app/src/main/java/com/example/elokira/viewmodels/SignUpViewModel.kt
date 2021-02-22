package com.example.elokira.viewmodels

import User
import android.util.Log
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SignUpViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    sealed class LoginResult {
        object IdNoMissing : LoginResult()

        object NameMissing : LoginResult()

        object PhoneNoMissing: LoginResult()

        object UserExists: LoginResult()

        class NetworkError(val userMessage: String) : LoginResult()

        object NetworkFailure : LoginResult()

        object Success : LoginResult()
    }

    val loginResultEmitter = EventEmitter<LoginResult>()
    val loginResult: EventSource<LoginResult> = loginResultEmitter

    init{
        Log.i("sign up ViewModel", " View model created")
    }

        fun signUpUser(userEntry: User){


            //check if fields are empty
            if(userEntry.idNumber.isEmpty()){
                loginResultEmitter.emit(LoginResult.IdNoMissing)
            }
            if(userEntry.firstName.isEmpty()){
                loginResultEmitter.emit(LoginResult.NameMissing)
            }



        }

    override fun onCleared() {
        super.onCleared()
    }


}