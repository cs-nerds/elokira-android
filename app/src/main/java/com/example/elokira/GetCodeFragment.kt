package com.example.elokira

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.elokira.databinding.GetCodeFragmentBinding

class GetCodeFragment : Fragment() {

    companion object {
        fun newInstance() = GetCodeFragment()
    }

    private lateinit var viewModel: GetCodeViewModel
    private lateinit var binding: GetCodeFragmentBinding

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


        binding.getCode.setOnClickListener {
            val id = binding.codeIdNumber.text.toString()
            val idError = binding.codeIdErr
            if(id.isEmpty()){
                idError.error = "Code missing"
                idError.requestFocus()
            }else{
                val preferences = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE) ?: return@setOnClickListener
                with(preferences.edit()){
                    putString("Authentication Code", id)
                    apply()
                }
                it.findNavController().navigate(R.id.action_getCodeFragment_to_homeFragment)
            }


        }
        // TODO: Use the ViewModel
    }

}