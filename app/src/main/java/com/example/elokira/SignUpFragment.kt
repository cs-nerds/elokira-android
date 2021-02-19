package com.example.elokira

import User
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.elokira.databinding.SignUpFragmentBinding
import com.zhuinden.liveevent.observe

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: SignUpFragmentBinding

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
            val idNumber = binding.idNumber.text.toString()
            val firstName = binding.firstName.text.toString()

            val user: User = User(idNumber, firstName)
            viewModel.signUpUser(user)
            Log.i("Sign Up Fragment", "User is ${user}")

            it.findNavController().navigate(R.id.action_signUpFragment_to_validateSignUpFragment)

        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                SignUpViewModel.LoginResult.IdNoMissing -> {
                    Toast.makeText(
                        context, "Data is missing", Toast.LENGTH_LONG
                    ).show()
                    idNoError.error = "Email required"
                    idNoError.requestFocus()
                }
                SignUpViewModel.LoginResult.NameMissing -> {
                    firstNameErr.error = "Password required"
                    firstNameErr.requestFocus()
                }

//                SignUpViewModel.LoginResult.NetworkFailure -> {
//                    Toast.makeText(
//                        context, "Network Failure", Toast.LENGTH_LONG
//                    ).show()
//                }
////                SignUpViewModel.LoginResult.NetworkError -> {
////                    showToast(context, result.userMessage)
////                }
//                SignUpViewModel.LoginResult.Success -> {
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
////                    findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)
//
//                }
                else ->Toast.makeText(context, "Network Error", Toast.LENGTH_LONG)
            }

        }

    }


}