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
class registerViewModel @Inject constructor(
    val userRepo: UserRepo
) : ViewModel() {

    private val _state = MutableStateFlow(userStatus())
    val state = _state.asStateFlow()

    suspend fun register(user :UserModel,password:String) = viewModelScope.launch {
        userRepo.registerUser(user, password).collect{
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