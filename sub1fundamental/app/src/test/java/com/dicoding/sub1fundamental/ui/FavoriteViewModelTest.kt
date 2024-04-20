package com.dicoding.sub1fundamental.ui

import com.dicoding.sub1fundamental.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FavoriteViewModelTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var favoriteViewModel: FavoriteViewModel
    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        favoriteViewModel = FavoriteViewModel(userRepository)
    }
    @Test
    fun deleteUser()= runBlocking{
        val userId = 6605876
        favoriteViewModel.deleteUser(userId)

        Mockito.verify(userRepository).delete(userId)
    }
}