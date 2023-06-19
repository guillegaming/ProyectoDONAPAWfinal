package com.example.proyectodonapawfinal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodonapawfinal.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingViewModel@Inject constructor(
    val userRepo: UserRepo
) : ViewModel()  {
    private val _state = MutableStateFlow(userStatus())
    val state = _state.asStateFlow()

    private val _state1 = MutableStateFlow(userStatus())
    val state1 = _state1.asStateFlow()

    private val _state2 = MutableStateFlow(userStatus())
    val state2 = _state2.asStateFlow()


    init {
        viewModelScope.launch {
            getInfo()
        }
    }

    private suspend fun getInfo()= viewModelScope.launch {
        userRepo.getUserInfo().collect{
            when(it){
                is Status.Loading-> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
                is  Status.Success-> {
                    _state.value = state.value.copy(
                        isLoading = false,
                        user = it.data
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

    suspend fun updateInfo(userModel: UserModel) = viewModelScope.launch {
        userRepo.updateProfile(userModel).collect{
            when(it){
                is Status.Loading-> {
                    _state1.value = state1.value.copy(
                        isLoading = true
                    )
                }
                is  Status.Success-> {
                    _state1.value = state1.value.copy(
                        isLoading = false,
                        success = it.data
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

    suspend fun updateEmail(password: String , newEmail:String) = viewModelScope.launch {
        userRepo.changeEmail(password , newEmail).collect{
            when(it){
                is Status.Loading-> {
                    _state2.value = state2.value.copy(
                        isLoading = true
                    )
                }
                is  Status.Success-> {
                    _state2.value = state2.value.copy(
                        isLoading = false,
                        success = it.data
                    )
                }
                is  Status.Error-> {
                    _state2.value = state2.value.copy(
                        error = it.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}