package com.example.elokira.fragments

import MyAdapter
import ResultObserver
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.elokira.viewmodels.HomeViewModel
import com.example.elokira.R
import com.example.elokira.data.Election
import com.example.elokira.data.UserDetails
import com.example.elokira.databinding.HomeFragmentBinding
import com.example.elokira.databinding.NavHeaderBinding
import com.example.elokira.repositories.BuilderClass
import com.google.android.material.navigation.NavigationView
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.liveevent.observe
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.awaitResponse

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private var electionsList: List<Election?> = listOf()
    private var recyclerView : RecyclerView? = null
    private var adapter: MyAdapter? = null
    val electionsResultEmitter = EventEmitter<ResultObserver>()
    val electionResult : EventSource<ResultObserver> = electionsResultEmitter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel

        val mypref = activity?.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        val myToken = mypref?.getString("Authentication Code", "")
        if (myToken != null) {
            Log.i("token", myToken)
        }
        val bearer = "Bearer $myToken"


        lifecycleScope.launch {
            val response = getElection(bearer)
            Log.i("Response code: ", response.code().toString())
            Log.i("Response body: ", "${response.body()}")
            Log.i("Response body: ", response.body().toString())
            when(response.code()){
                200 ->{
                    Log.i("token", response.body()?.map { it.electionName }.toString())
                    electionsList = response.body()!!
                    electionsResultEmitter.emit(ResultObserver.Success)
                    response.body()
                }
                401 -> {
                    Log.i("token failure", "failed")
                }
                else ->{
                    Log.i("token failure", "failed")
                }
            }
        }
        electionResult.observe(this){result ->
            when(result){
                ResultObserver.Success ->{
                    val progress = binding.loadingPanel
                    progress.visibility = View.GONE

                    recyclerView = binding.recyclerView
                    adapter = MyAdapter(electionsList)
                    recyclerView!!.adapter = adapter
                }
            }

        }

    }

    private suspend fun getElection(bearer: String): Response<List<Election>> = withContext(Dispatchers.IO){
     val response = BuilderClass.apiService.getElections(bearer).awaitResponse()
        when(response.isSuccessful){
            true -> return@withContext response
            false -> electionsResultEmitter.emit(ResultObserver.NetworkError(response.message()))
        }
        return@withContext response

    }

//    fun user(bearer: String): UserDetails {
//        val response = runBlocking {
//            val response = getUser(bearer)
//            when(response.code()){
//                200 ->{
//                    Log.i("token", response.body().toString())
//                    val userDetail = response.body()
//                    response.body()
//                }
//                401 -> {
//                    Log.i("token failure", "failed")
//                }
//                else ->{
//                    Log.i("token failure", "failed")
//                }
//            }
//
//        }
//        return response as UserDetails
//    }





}