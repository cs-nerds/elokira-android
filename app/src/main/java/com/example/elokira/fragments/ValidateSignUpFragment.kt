package com.example.elokira.fragments

import Authenticate
import VerifiedUser
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.elokira.R
import com.example.elokira.viewmodels.ValidateSignUpViewModel
import com.example.elokira.databinding.ValidateSignUpFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class ValidateSignUpFragment : Fragment() {

    companion object {
        fun newInstance() = ValidateSignUpFragment()
    }

    private lateinit var viewModel: ValidateSignUpViewModel
    private lateinit var binding: ValidateSignUpFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil. inflate(inflater,
            R.layout.validate_sign_up_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ValidateSignUpViewModel::class.java)

        val args = ValidateSignUpFragmentArgs.fromBundle(requireArguments())
        val names = binding.names
        val idNumber = binding.validIdNo
        val phoneNoErr = binding.phoneNoErr
        names.text = "${args.firstName} ${ args.lastName}"
        idNumber.text = args.idNumber

        binding.validateSignUp.setOnClickListener {

            val phoneNumber = binding.phoneNumber.text.toString()
            val verifiedUser = VerifiedUser(args.firstName,args.lastName, args.idNumber, phoneNumber)
            viewModel.verifyUser(verifiedUser)
            Log.i("Verified user is", verifiedUser.toString())

            val lifecycleScope = MainScope()
            lifecycleScope.launch {
                val response = addPhoneNumber(verifiedUser)
                Log.i("Verified login response code ${response.code()}", response.body().toString())

                val login = response.body()
                Log.i("Verified login message", login.toString())
                when(response.code()){
                    201 -> {
                        Log.i("Verified login message with code 201 =  ${response.code()}", response.toString())
                        viewModel.verifiedResponseEmitter.emit(ValidateSignUpViewModel.VerifiedResponse.PhoneNoMissing)
                    }
                    409 -> {
                        Log.i("Verified Login message with code 409 = ${response.code()}", response.message())
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                        viewModel.verifiedResponseEmitter.emit(ValidateSignUpViewModel.VerifiedResponse.UserExists)
                    }
                    else -> {
                        Log.i("Verified Login with any other code ${response.code()}", response.message().toString() )
                        viewModel.verifiedResponseEmitter.emit(ValidateSignUpViewModel.VerifiedResponse.NetworkFailure)
                    }
                }
            }

        }
      viewModel.verifiedResponse.observe(this){result ->
          when(result){
              ValidateSignUpViewModel.VerifiedResponse.PhoneNoMissing -> {
                  Toast.makeText(
                      context, "Data is missing", Toast.LENGTH_LONG
                  ).show()
                  phoneNoErr.error="Phone number required"
                  phoneNoErr.requestFocus()
              }
              ValidateSignUpViewModel.VerifiedResponse.NetworkFailure -> {
                  Toast.makeText(context, "Failed, try again", Toast.LENGTH_LONG).show()
              }
              ValidateSignUpViewModel.VerifiedResponse.UserExists -> {
                  Toast.makeText(context, "res", Toast.LENGTH_SHORT).show()
                  findNavController().navigate(ValidateSignUpFragmentDirections.actionValidateSignUpFragmentToLogInFragment2())
              }
              ValidateSignUpViewModel.VerifiedResponse.Success -> {
                  Toast.makeText(context, "Check sms for code", Toast.LENGTH_LONG).show()
                  findNavController().navigate(ValidateSignUpFragmentDirections.actionValidateSignUpFragmentToGetCodeFragment())
              }
              else -> Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show()
          }
      }
    }

    private suspend fun addPhoneNumber(verifiedUser: VerifiedUser): Response<Authenticate> = withContext(Dispatchers.IO){
        BuilderClass.apiService.createUser(verifiedUser).awaitResponse()
    }

}