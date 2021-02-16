package com.example.elokira

import User
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
        val emailErr = binding.emailErr
        val passwordErr = binding.passwordErr


        // TODO: Use the ViewModel
        binding.signUp.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val user: User = User(email, password)
            viewModel.signUpUser(user)
            Log.i("Sign Up Fragment", "User is ${user}")
        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                SignUpViewModel.LoginResult.EmailMissing -> {
                    Toast.makeText(
                        context, "Data is missing", Toast.LENGTH_LONG
                    ).show()
                    emailErr.error = "Email required"
                    emailErr.requestFocus()
                }
                SignUpViewModel.LoginResult.PasswordMissing -> {
                    passwordErr.error = "Password required"
                    passwordErr.requestFocus()
                }
                SignUpViewModel.LoginResult.NetworkFailure -> {
                }
//                SignUpViewModel.LoginResult.NetworkError -> {
//                    showToast(context, result.userMessage)
//                }
//                SignUpViewModel.LoginResult.Success -> {
//                    val intent = Intent(applicationContext, HomeActivity::class.java)
//                    intent.flags =
//                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    showToast(applicationContext, res.body()?.message)
//                    Log.d("kjsfgxhufb", response.body()?.status.toString())
//                    startActivity(intent)
//                    finish()
//                }
                else ->Toast.makeText(context, "Network Error", Toast.LENGTH_LONG)
            }

        }

    }


}