package com.example.elokira.viewmodels

import VerifiedUser
import android.util.Log
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class ValidateSignUpViewModel : ViewModel() {
    init {
        Log.i("Verify response", "ValidateSignUpViewModelCreated")
    }
    sealed class VerifiedResponse{
        object PhoneNoMissing: ValidateSignUpViewModel.VerifiedResponse()

        class NetworkError(val userMessage: String) : ValidateSignUpViewModel.VerifiedResponse()

        object NetworkFailure : ValidateSignUpViewModel.VerifiedResponse()

        object Success : ValidateSignUpViewModel.VerifiedResponse()
    }

    val verifiedResponseEmitter = EventEmitter<VerifiedResponse>()
    val verifiedResponse: EventSource<VerifiedResponse> = verifiedResponseEmitter

    fun verifyUser(verifiedUser: VerifiedUser) {
        if(verifiedUser.phoneNumber.isEmpty()){
            verifiedResponseEmitter.emit(VerifiedResponse.PhoneNoMissing)
        }
    }

}