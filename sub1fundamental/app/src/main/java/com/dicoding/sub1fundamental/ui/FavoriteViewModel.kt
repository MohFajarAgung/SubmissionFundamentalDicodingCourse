package com.dicoding.sub1fundamental.ui


import androidx.lifecycle.LiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.sub1fundamental.database.GithubUser

import com.dicoding.sub1fundamental.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val userRepository: UserRepository) :ViewModel() {

    fun deleteUser(userId: Int){
        viewModelScope.launch (Dispatchers.IO){
            userRepository.delete(userId)
        }
    }

    fun insertUser(user: GithubUser) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insert(user)
        }
    }

   val allUsersLiveData: LiveData<List<GithubUser>> = userRepository.allUsers


}