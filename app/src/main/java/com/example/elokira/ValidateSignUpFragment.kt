package com.example.elokira

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.elokira.databinding.ValidateSignUpFragmentBinding

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
        binding= DataBindingUtil. inflate(inflater,R.layout.validate_sign_up_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ValidateSignUpViewModel::class.java)

        binding.validateSignUp.setOnClickListener {
            it.findNavController().navigate(R.id.action_validateSignUpFragment_to_getCodeFragment)
        }
        // TODO: Use the ViewModel
    }

}