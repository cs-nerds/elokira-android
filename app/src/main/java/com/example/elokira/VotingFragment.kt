package com.example.elokira

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.elokira.data.Position
import com.example.elokira.databinding.VotingFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.example.elokira.viewmodels.VotingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.awaitResponse

class VotingFragment : Fragment() {

    companion object {
        fun newInstance() = VotingFragment()
    }

    private lateinit var viewModel: VotingViewModel
    private lateinit var binding: VotingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.voting_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VotingViewModel::class.java)
        val mypref = activity?.getSharedPreferences(getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val myToken = mypref?.getString("Authentication Code", "")
        if (myToken != null) {
            Log.i("token", myToken)
        }
        val bearer = "Bearer $myToken"

        val args = VotingFragmentArgs.fromBundle(requireArguments())
        val url = "/elections/${args.electionId}/positions"
        Log.i("Url", url)
        lifecycleScope.launch {
            val response = getPositions(url, bearer)
            Log.i("Positions (${response.code()}", response.body().toString())
        }
    }

    private suspend fun getPositions(url: String, bearer:String): Response<List<Position>> = withContext(
        Dispatchers.IO){
        BuilderClass.apiService.getElectionPositions(url, bearer).awaitResponse()
    }

}