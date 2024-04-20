package com.dicoding.sub1fundamental.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sub1fundamental.R
import com.dicoding.sub1fundamental.data.datastore.SettingPreferences
import com.dicoding.sub1fundamental.data.datastore.dataStore
import com.dicoding.sub1fundamental.data.response.GithubResponse
import com.dicoding.sub1fundamental.data.response.ItemsItem
import com.dicoding.sub1fundamental.data.retrofit.ApiConfig
import com.dicoding.sub1fundamental.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
        private var USER = "Arif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        menampilkan item saat button menu disentuh
        binding.btnMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_item_1 -> {
                        val intent = Intent(this@MainActivity, ThemeSetting::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }

//        ke Favorite Activity
        binding.btnFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteUsersActivity::class.java)
            startActivity(intent)
        }

//        untuk pencarian user
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    USER = searchView.text.toString()
                    findUser(USER)
                    searchView.hide()
                    false
                }
        }
        //        memanggil tema yang sudah diatur pada datastore
        getThemeSetting()

//        mengatur recycle view
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)
        findUser(USER)


    }
    fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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

    private fun findUser(username: String) {
        showLoading(true)
        binding.refresh.visibility = View.GONE
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {

                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setReviewData(responseBody.items)
                        if (responseBody.items!!.isEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "data tidak ditemukan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "data tidak ditemukan", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {

                Toast.makeText(this@MainActivity, "data tidak ditemukan", Toast.LENGTH_SHORT).show()
                showLoading(false)
                binding.refresh.visibility = View.VISIBLE
                binding.refresh.setOnClickListener {
                    binding.refresh.visibility = View.GONE
                    showLoading(true)
                    recreate()
                }
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun setReviewData(items: List<ItemsItem?>?) {
        val adapter = ItemAdapter()
        adapter.submitList(items)
        binding.rvReview.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}