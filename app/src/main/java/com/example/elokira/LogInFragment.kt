package com.example.elokira

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.elokira.databinding.LogInFragmentBinding

class LogInFragment : Fragment() {

    companion object {
        fun newInstance() = LogInFragment()
    }

    private lateinit var viewModel: LogInViewModel
    private lateinit var binding: LogInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.log_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogInViewModel::class.java)
        // TODO: Use the ViewModel
        binding.getCode.setOnClickListener {
            it.findNavController().navigate(R.id.action_logInFragment_to_getCodeFragment)
        }
    }

}