package com.example.elokira.fragments

import Authenticate
import LoginRequest
import ResultObserver
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.elokira.viewmodels.LogInViewModel
import com.example.elokira.R
import com.example.elokira.databinding.LogInFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.awaitResponse

class LogInFragment : Fragment() {

    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: LogInFragmentBinding
    private lateinit var loginResponse: Authenticate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.log_in_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        val idNoError = binding.idNoLoginErr
        // TODO: Use the ViewModel
        binding.getCode.setOnClickListener {
            val idNumber = LoginRequest(binding.idNumberLogin.text.toString().trim())
            viewModel.loginUser(idNumber)

            lifecycleScope.launch {
                val response = loginUser(idNumber)

                loginResponse = Authenticate("", "")

                Log.i("Login Request response is ", response.code().toString())
                when(response.code()){
                    201 -> {
                        loginResponse = response.body()!!
                        Log.i("Login Request response with code ${response.code()}", loginResponse.toString())
                        viewModel.loginResultEmitter.emit(ResultObserver.Success)
                    }
                    404 ->{
                        Log.i("Login Request response with code ${response.code()}", response.body().toString())
                        viewModel.loginResultEmitter.emit(ResultObserver.IdNoMissing)
                    }
                    else -> {
                        Log.i("Login Request response with code ${response.code()}", response.body().toString())
                        viewModel.loginResultEmitter.emit(ResultObserver.IdNoMissing)
                    }
                }
            }

        }

        viewModel.loginResult.observe(this){result ->
            when(result){
                ResultObserver.IdNoMissing ->{
                    idNoError.error = "Enter id number as in your ID"
                    idNoError.requestFocus()
                    val textView = binding.idNotFound
                    textView.visibility = View.VISIBLE
                    val text = clickableString()
                    textView.setText(text)
                    textView.movementMethod = LinkMovementMethod.getInstance()
                    textView.highlightColor = Color.TRANSPARENT

                }
                ResultObserver.Success ->{
                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToGetCodeFragment(loginResponse.loginId))
                }
            }

        }
    }

    private suspend fun loginUser(loginUserId: LoginRequest): Response<Authenticate> = withContext(Dispatchers.IO){
        BuilderClass.apiService.loginUser(loginUserId).awaitResponse()
    }

    private fun clickableString(): SpannableString{
        val spannableString = SpannableString("ID number not found: Sign in")
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(textView: View) {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 21, 28, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE )
        return spannableString
    }

//    fun returnResponse(loginID: LoginRequest): Deferred<Response<Authenticate>> = lifecycleScope.async{
//        val response = loginUser(loginID)
//
//        loginResponse = response.body().also {
//            if(response.code() == 200){
//                if (it != null) {
//                    loginResponse = it
//                }
//            }
//        }!!
//        Log.i("Login Request response is ", response.code().toString())
//        when(response.code()){
//            201 -> {
//                Log.i("Login Request response with code ${response.code()}", loginResponse.toString())
//                viewModel.loginResultEmitter.emit(ResultObserver.Success)
//                return@async loginUser(loginID)
//            }
//            404 ->{
//                Log.i("Login Request response with code ${response.code()}", response.body().toString())
//                viewModel.loginResultEmitter.emit(ResultObserver.IdNoMissing)
//            }
//            else -> {
//                Log.i("Login Request response with code ${response.code()}", response.body().toString())
//                viewModel.loginResultEmitter.emit(ResultObserver.IdNoMissing)
//            }
//        }
//    }



}