package com.example.elokira

import Candidate
import ExpandableListAdapter
import ResultObserver
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.elokira.data.Position
import com.example.elokira.databinding.VotingFragmentBinding
import com.example.elokira.repositories.BuilderClass
import com.example.elokira.viewmodels.VotingViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.liveevent.observe
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
    private var positions: List<Position?> = listOf()
    val PositionsResultEmitter = EventEmitter<ResultObserver>()
    val positionResult : EventSource<ResultObserver> = PositionsResultEmitter
    val listCandidates = HashMap<Position, List<Candidate>>()
    var positionCandidate: Position? = null

    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.voting_fragment, container, false)
        binding.toolbar.setTitle(R.string.Vote)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VotingViewModel::class.java)
        val mypref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key),
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
            when(response.code()){
                200 -> {
                    positions = response.body()!!
                    val position = positions[0]?.positionId
                    positionCandidate = positions[0]
                    val url = "/elections/${args.electionId}/positions/$position"
                    val candidateResponse = getCandidates(url, bearer)
                    Log.i(
                        "Positions (${candidateResponse.code()}",
                        candidateResponse.body().toString()
                    )
                    PositionsResultEmitter.emit(ResultObserver.Success)

//                    when (candidateResponse.code()) {
//                        404 -> {
//                            val candidateResponse = candidateResponse.body()
//                            if (candidateResponse != null) {
//                                positionCandidate?.let { listCandidates.put(it, candidateResponse) }
//                            }
//                            PositionsResultEmitter.emit(ResultObserver.Success)
//                        }
//                        else -> {
//                            val candidateResponse = candidateResponse.body()
//                            if (candidateResponse != null) {
//                                positionCandidate?.let { listCandidates.put(it, candidateResponse) }
//                            }
////                            PositionsResultEmitter.emit(ResultObserver.Success)
//                        }
//                    }

                }
                else -> {
                    PositionsResultEmitter.emit(ResultObserver.NetworkFailure)
                }
            }
        }

        positionResult.observe(this){ result ->
            when(result){
                ResultObserver.Success -> {
                    val progress = binding.loadingPanel
                    progress.visibility = View.GONE

                    val positionTitles = binding.lvExp


                    val listAdapter = ExpandableListAdapter(requireContext(), listDataHeader!!, listDataChild!!)
                    Log.i("List Adapter", "${listDataHeader} ${listDataChild}")

                    positionTitles.setAdapter(listAdapter)
                }
                ResultObserver.NetworkFailure -> {
                    Toast.makeText(context, "Network Failure", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "Network Failure", Toast.LENGTH_LONG).show()
                }


            }
        }
    }

    private suspend fun getPositions(url: String, bearer: String): Response<List<Position>> = withContext(
        Dispatchers.IO
    ){
        BuilderClass.apiService.getElectionPositions(url, bearer).awaitResponse()
    }

    private suspend fun getCandidates(url: String, bearer: String): Response<List<Candidate>> = withContext(
        Dispatchers.IO
    ){
        BuilderClass.apiService.getPositionCandidates(url, bearer).awaitResponse()
    }

    private fun prepareListData() {
        listDataHeader = ArrayList<String>();
        listDataChild = HashMap<String, List<String>>();

        // Adding child data
        (listDataHeader as ArrayList<String>).add("Top 250");
        (listDataHeader as ArrayList<String>).add("Now Showing");
        (listDataHeader as ArrayList<String>).add("Coming Soon..");

        // Adding child data
        val top250 = ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        val nowShowing = ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        val comingSoon = ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild!!.put((listDataHeader as ArrayList<String>).get(0), top250); // Header, Child data
        listDataChild!!.put((listDataHeader as ArrayList<String>).get(1), nowShowing);
        listDataChild!!.put((listDataHeader as ArrayList<String>).get(2), comingSoon);
    }


}