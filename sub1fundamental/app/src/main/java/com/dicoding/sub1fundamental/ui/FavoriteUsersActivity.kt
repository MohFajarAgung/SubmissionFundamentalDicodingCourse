package com.dicoding.sub1fundamental.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.dicoding.sub1fundamental.R
import com.dicoding.sub1fundamental.data.response.ItemsItem
import com.dicoding.sub1fundamental.database.UserRoomDatabase
import com.dicoding.sub1fundamental.databinding.ActivityFavoriteUsersBinding
import com.dicoding.sub1fundamental.repository.UserRepository

class FavoriteUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUsersBinding

    private lateinit var favoriteViewModel: FavoriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val database = Room.databaseBuilder(
            applicationContext,
            UserRoomDatabase::class.java,
            "github_user_database"
        ).build()
        val userDao = database.userDao()
        val userRepository = UserRepository(userDao)
        favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory(userRepository))
            .get(FavoriteViewModel::class.java)

        favoriteViewModel.allUsersLiveData.observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }

            if(items.size==0){
                binding.tvFavorite.text = getString(R.string.favkosong)
                Toast.makeText(this@FavoriteUsersActivity, "Kosong!!", Toast.LENGTH_SHORT).show()
            }
            val adapter = ItemAdapter()
            showLoading(true)
            Handler().postDelayed({
                val recyclerView = binding.rvFavorite
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
                adapter.submitList(items)
                showLoading(false)
            }, 300) //
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}