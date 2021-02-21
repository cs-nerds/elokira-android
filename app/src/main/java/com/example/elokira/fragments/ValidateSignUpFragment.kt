package com.example.elokira.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.elokira.R
import com.example.elokira.viewmodels.ValidateSignUpViewModel
import com.example.elokira.databinding.ValidateSignUpFragmentBinding
import kotlinx.coroutines.launch

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
        names.text = "${args.firstName} ${ args.lastName}"
        idNumber.text = args.idNumber

        binding.validateSignUp.setOnClickListener {
            lifecycleScope.launch {
                
            }
            it.findNavController().navigate(R.id.action_validateSignUpFragment_to_getCodeFragment)
        }
        // TODO: Use the ViewModel
    }

}