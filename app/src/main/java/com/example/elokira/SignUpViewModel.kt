package com.example.elokira

import BuilderClass
import User
import android.util.Log
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class SignUpViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    sealed class LoginResult {
        object IdNoMissing : LoginResult()

        object NameMissing : LoginResult()

        object PhoneNoMissing: LoginResult()

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
        if(userEntry.idNumber.isEmpty()){
            loginResultEmitter.emit(LoginResult.IdNoMissing)
        }
        if(userEntry.firstName.isEmpty()){
            loginResultEmitter.emit(LoginResult.NameMissing)
        }
        if(userEntry.phoneNumber.isEmpty()){
            loginResultEmitter.emit(LoginResult.PhoneNoMissing)
        }

        GlobalScope.launch(Dispatchers.IO){
            val isSuccessful = BuilderClass.apiService.createVoter(userEntry).awaitResponse().let { response ->
                if(response.isSuccessful){
                    loginResultEmitter.emit(LoginResult.Success)
                    return@let true
                }
                else{
//                    when (response.code()){
//                    }

                    Log.d("Response check", response.code().toString())
                    Log.d("res error ", response.errorBody().toString()  )
                    loginResultEmitter.emit(LoginResult.NetworkFailure)
                    return@let false
                }

            }


        }

    }

    fun addUser(){
        //add user to database via an API
    }

    override fun onCleared() {
        super.onCleared()
    }


}