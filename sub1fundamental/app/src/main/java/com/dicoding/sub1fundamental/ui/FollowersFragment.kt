package com.dicoding.sub1fundamental.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sub1fundamental.data.response.ItemsItem
import com.dicoding.sub1fundamental.data.retrofit.ApiConfig
import com.dicoding.sub1fundamental.databinding.FragmentFollowersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var receivedData: String? = null

    private lateinit var binding: FragmentFollowersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    fun setData(data: String) {
        receivedData = data
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val receivedData = sharedViewModel.sharedData
        if (receivedData != null) {
            findFollowers(receivedData)
        }
        return binding.root
    }

    companion object {
        private const val TAG = "FragmentFollowers"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun findFollowers(username: String) {
       showLoading(true)
        val followers = ApiConfig.getApiService().getFollowers(username)
        followers.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    if (responseBody != null) {
                        setFollowersData(responseBody)
                    }
                } else {
                    Log.e(FollowersFragment.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(FollowersFragment.TAG, "onFailure: ${t.message}")
                showLoading(false)
            }
        })
    }

    private fun setFollowersData(items: List<ItemsItem?>?) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.tvFollowers.layoutManager = layoutManager
        val adapter = ItemAdapter()
        adapter.submitList(items)
        binding.tvFollowers.adapter = adapter


    }
}

