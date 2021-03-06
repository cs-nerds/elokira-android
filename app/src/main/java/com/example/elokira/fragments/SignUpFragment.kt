package com.example.elokira.fragments

import LoginRequest
import User
import com.example.elokira.data.UserResponse
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.elokira.R
import com.example.elokira.viewmodels.SignUpViewModel
import com.example.elokira.databinding.SignUpFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.awaitResponse

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()

    }


    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var userResponse: UserResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        val idNoError = binding.idNoErr
        val firstNameErr = binding.firstNameErr



        // TODO: Use the ViewModel
        binding.signUp.setOnClickListener {
            it.hideKeyboard()
            val bar = binding.loadingPanel
            bar.visibility = View.VISIBLE
            val signUp = binding.signUp
            signUp.visibility = View.INVISIBLE
            val idNumber = binding.idNumber.text.toString().trim()
            val firstName = binding.firstName.text.toString().trim()

            val user: User = User(idNumber, firstName)
            Log.i("Sign Up Fragment", "User is ${user}")
            viewModel.signUpUser(user)

            val lifecycleScope = MainScope()

            lifecycleScope.launch{
                    val response = addUser(user)

//
                    Log.i("Log 1", response.body().toString())
                    when(response.code()){
                        200 -> {
                            Log.i("Log response", response.toString())
                            userResponse = response.body()!!
                            viewModel.loginResultEmitter.emit(SignUpViewModel.LoginResult.Success)
                        } 403 -> {
                        signUp.visibility = View.VISIBLE
                        bar.visibility = View.INVISIBLE
                        Log.d("Response error ", response.toString())
                        viewModel.loginResultEmitter.emit(SignUpViewModel.LoginResult.IdNoMissing)
                        viewModel.loginResultEmitter.emit(SignUpViewModel.LoginResult.NameMissing)
                        Toast.makeText(
                            context, "Failed, Enter correct ID number and first name as per your ID", Toast.LENGTH_LONG
                        ).show()
                    }
                        409 ->{
                            viewModel.loginResultEmitter.emit(SignUpViewModel.LoginResult.UserExists)
                        }

                        else -> Toast.makeText(context, "Unexpected, try again later", Toast.LENGTH_LONG).show()

                    }

            }




        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                SignUpViewModel.LoginResult.IdNoMissing -> {
                    Toast.makeText(
                        context, "Data is missing", Toast.LENGTH_LONG
                    ).show()
                    idNoError.error = "Enter id number as in your ID"
                    idNoError.requestFocus()
                }
                SignUpViewModel.LoginResult.NameMissing -> {
                    firstNameErr.error = "Enter first name as in your ID"
                    firstNameErr.requestFocus()
                }

                SignUpViewModel.LoginResult.NetworkFailure -> {
                    Toast.makeText(
                        context, "Failed, Enter correct ID number and first name as per your ID", Toast.LENGTH_LONG
                    ).show()
                }
                SignUpViewModel.LoginResult.UserExists -> {
                    Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT)
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLogInFragment())
                }
//                SignUpViewModel.LoginResult.NetworkError -> {
//                    showToast(context, result.userMessage)
//                }
                SignUpViewModel.LoginResult.Success -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()

                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToValidateSignUpFragment(userResponse.firstName, userResponse.lastName, userResponse.idNumber))
                }
                else ->Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show()
            }

        }

    }
    private suspend fun addUser(userEntry: User): Response<UserResponse> = withContext(Dispatchers.IO){
        //add user to database via an API
        BuilderClass.apiService.verifyUser(userEntry).awaitResponse()

    }

    fun View.hideKeyboard() {
        val imm =  getSystemService(context,
            InputMethodManager::class.java) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}