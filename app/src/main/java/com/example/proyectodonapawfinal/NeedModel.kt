package com.example.proyectodonapawfinal

import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.Serializable

class NeedModel(
    var cloth: String? = null,
    var pet: String? = null,
    var imgURL: String? = null,
    var id: String? = null,
    var clothSize : String? = null,
    var address : String? = null,
    var userId : String? = null
) : Serializable
