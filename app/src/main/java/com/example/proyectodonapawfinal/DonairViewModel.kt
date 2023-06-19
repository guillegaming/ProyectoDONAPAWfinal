package com.example.proyectodonapawfinal

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodonapawfinal.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonairViewModel@Inject constructor(
    val userRepo: UserRepo
) : ViewModel() {

    private val _state = MutableStateFlow(userStatus())
    val state = _state.asStateFlow()

    private val _state1 = MutableStateFlow(userStatus())
    val state1 = _state1.asStateFlow()

    suspend fun donair(need:NeedModel , imgUri :Uri) = viewModelScope.launch {
        userRepo.uploadNeedToFirebase(need, imgUri).collect{
            when(it){
                is Status.Loading-> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
                is  Status.Success-> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        success = it.data
                    )
                }
                is  Status.Error-> {
                    _state.value = state.value.copy(
                        error = it.message,
                        isLoading = false
                    )
                }
            }

        }
    }
    init {
        viewModelScope.launch {
            getAllDonair()
        }
    }

    suspend fun getAllDonair() = viewModelScope.launch {
        userRepo.getNeed().collect{
            when(it){
                is Status.Loading-> {
                    _state1.value = state1.value.copy(
                        isLoading = true
                    )
                }
                is  Status.Success-> {
                    _state1.value = state1.value.copy(
                        isLoading = false,
                        listDonair = it.data
                    )
                }
                is  Status.Error-> {
                    _state1.value = state1.value.copy(
                        error = it.message,
                        isLoading = false
                    )
                }
            }

        }
    }


}