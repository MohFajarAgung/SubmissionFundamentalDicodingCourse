package com.dicoding.sub1fundamental.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.sub1fundamental.R
import com.dicoding.sub1fundamental.data.response.DetailUserResoponse
import com.dicoding.sub1fundamental.data.retrofit.ApiConfig
import com.dicoding.sub1fundamental.database.GithubUser
import com.dicoding.sub1fundamental.database.UserRoomDatabase
import com.dicoding.sub1fundamental.databinding.ActivityDetailUserBinding
import com.dicoding.sub1fundamental.repository.UserRepository
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetaileUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )


    }


    private lateinit var favoriteViewModel: FavoriteViewModel


    private lateinit var binding: ActivityDetailUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = intent
        val name = intent.getStringExtra("name")



        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.sharedData = name
        if (name != null) {
            findUsername(name)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun findUsername(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResoponse> {
            override fun onResponse(
                call: Call<DetailUserResoponse>,
                response: Response<DetailUserResoponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        with(responseBody) {

                            setDetailData(
                                name,
                                avatarUrl,
                                followers.toString(),
                                following.toString(),
                                login,
                                id
                            )
                        }
                    } else    Toast.makeText(this@DetailUserActivity, "Data Kosong", Toast.LENGTH_SHORT)
                        .show()
                }else    Toast.makeText(this@DetailUserActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFailure(call: Call<DetailUserResoponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@DetailUserActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setDetailData(
        name: String?,
        imageUrl: String?,
        followers: String?,
        following: String?,
        login: String?,
        id: Int?
    ) {

        Glide.with(binding.root.context)
            .load(imageUrl)
            .circleCrop()
            .into(binding.image)

        binding.tvName.text = name
        binding.tvFollowing.text = following + getString(R.string.tab_text_1)
        binding.tvFollowers.text = followers + getString(R.string.tab_text_2)
        binding.tvUser.text = login

        if (id != null && login != null && imageUrl != null) {
            GlobalScope.launch {
                munculkanBtnFavorite(id, login, imageUrl)

            }
        }
    }

    private fun munculkanBtnFavorite(id: Int, login: String, imageUrl: String) {
        val database = Room.databaseBuilder(
            applicationContext,
            UserRoomDatabase::class.java,
            "github_user_database"
        ).build()

        val dao = database.userDao()
        val userRepository = UserRepository(dao)

        val favoriteViewModel = ViewModelProvider(this, FavoriteViewModelFactory(userRepository))
            .get(FavoriteViewModel::class.java)

        val count = dao.getUserCount(id) > 0
        val colorButtonOn = ContextCompat.getColor(this, R.color.button_on)
        val colorButtonOff = ContextCompat.getColor(this, R.color.gray)
        if (id != null) {
            if (count == false) {
                binding.btnFavorite.setBackgroundColor(colorButtonOff)
                binding.btnFavorite.setOnClickListener {
                    binding.btnFavorite.setBackgroundColor(colorButtonOn)
                    val user = GithubUser(id = id, username = login, avatarUrl = imageUrl)
                    favoriteViewModel.insertUser(user)
                    Toast.makeText(this@DetailUserActivity, "Ditambahkan ke daftar Favorite", Toast.LENGTH_SHORT).show()
                    recreate()
                }
            } else
                binding.btnFavorite.setOnClickListener {
                    binding.btnFavorite.setBackgroundColor(colorButtonOff)
                    val database = Room.databaseBuilder(applicationContext, UserRoomDatabase::class.java, "github_user_database").build()
                    val userDao = database.userDao()
                    val userRepository = UserRepository(userDao)
                    favoriteViewModel.deleteUser(id)
                    Toast.makeText(this@DetailUserActivity, "Dihapus dari daftar Favorite", Toast.LENGTH_SHORT).show()
                    recreate()
                }

        }
    }
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}




