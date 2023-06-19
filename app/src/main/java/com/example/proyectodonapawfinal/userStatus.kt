package com.example.proyectodonapawfinal

data class userStatus(
    val error:String? = null,
    val isLoading:Boolean = false,
    val success:String? = null,
    val listDonair :List<NeedModel>? = null,
    val user:UserModel? = null


)