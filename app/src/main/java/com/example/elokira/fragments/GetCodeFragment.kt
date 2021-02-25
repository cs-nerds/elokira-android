package com.example.elokira.fragments

import AuthToken
import Authenticate
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.elokira.viewmodels.GetCodeViewModel
import com.example.elokira.R
import com.example.elokira.databinding.GetCodeFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class GetCodeFragment : Fragment() {

    companion object {
        fun newInstance() = GetCodeFragment()

    }

    private lateinit var viewModel: GetCodeViewModel
    private lateinit var binding: GetCodeFragmentBinding
    private lateinit var authTokenJWT: AuthToken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.get_code_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GetCodeViewModel::class.java)
        val idError = binding.codeIdErr
        val args = GetCodeFragmentArgs.fromBundle(requireArguments())

        binding.enterCode.setOnClickListener {
            it.hideKeyboard()
            val code = binding.code.text.toString().trim()
            val user = Authenticate(args.loginId, code)
            viewModel.authenticateUserCode(user)
            Log.i("Authentication details", user.toString())

            lifecycleScope.launch {
                val response = authenticateUser(user)
                Log.i("Authentication response with code ${response.code()}", response.body().toString() )

                val code = response.code()
                when(code){
                    200 -> {
                        Log.i("Authentication success with code ${response.code()}", response.body().toString())
                        authTokenJWT= response.body()!!
                        viewModel.authenticateResultEmitter.emit(ResultObserver.Success)
                    }
                    401 -> {
                        Log.i("Authentication success with code ${response.code()}", response.body().toString())
                        viewModel.authenticateResultEmitter.emit(ResultObserver.NetworkFailure)
                    }
                    else ->{
                        Log.i("Authentication success with code ${response.code()}", response.body().toString())
                        viewModel.authenticateResultEmitter.emit(ResultObserver.NetworkFailure)
                    }

                }
            }

        }
        viewModel.authenticationResult.observe(this){result ->
            when(result){
                ResultObserver.CodeMissing -> {
                    idError.error = "Enter correct code"
                    idError.requestFocus()
                }
                ResultObserver.Success ->{
                    val preferences = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return@observe
                    with(preferences.edit()){
                        putString("Authentication Code", authTokenJWT.token)
                        apply()
                    }
                    findNavController().navigate(GetCodeFragmentDirections.actionGetCodeFragmentToHomeFragment())
                }
                ResultObserver.NetworkFailure -> {
                    idError.error = "Enter correct code"
                    idError.requestFocus()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()

                }

            }
        }
        // TODO: Use the ViewModel
    }

    private suspend fun authenticateUser(user: Authenticate): Response<AuthToken> = withContext(Dispatchers.IO){
        BuilderClass.apiService.authenticateUser(user).awaitResponse()
    }

    fun View.hideKeyboard() {
        val imm =  ContextCompat.getSystemService(
            context,
            InputMethodManager::class.java
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}