package com.example.proyectodonapawfinal

import com.example.proyectodonapawfinal.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
@HiltViewModel
class LoginViewModel @Inject constructor(
    val userRepo: UserRepo
) : ViewModel() {

    private val _state = MutableStateFlow(userStatus())
    val state = _state.asStateFlow()

    suspend fun login(email :String,password:String) = viewModelScope.launch {
        userRepo.signInUser(email, password).collect{
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
}