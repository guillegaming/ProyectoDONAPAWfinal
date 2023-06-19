package com.example.proyectodonapawfinal.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyectodonapawfinal.NeedModel
import com.example.proyectodonapawfinal.Status
import com.example.proyectodonapawfinal.UserModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class UserRepo @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firebaseDatabase: FirebaseDatabase,
    private val storage: FirebaseStorage
) {

    suspend fun signInUser(email: String, password: String): Flow<Status<String>> {
        val mutableStateFlow = MutableStateFlow<Status<String>>(Status.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) mutableStateFlow.value = Status.Success("Login Success")
            else mutableStateFlow.value =
                Status.Error(it.exception?.localizedMessage.toString())
        }
        return mutableStateFlow
    }

    suspend fun registerUser(user: UserModel, password: String): Flow<Status<String>> {
        // todo write the logic to register a user and return error msg if exists and
        val mutableStateFlow = MutableStateFlow<Status<String>>(Status.Loading)
        firebaseAuth.createUserWithEmailAndPassword(user.email!!, password)
            .addOnCompleteListener {
                Log.e("registerUser: ", user.toString())
                if (it.isSuccessful) {
                    mutableStateFlow.value = Status.Success("Sign Up Success")
                    firebaseDatabase.getReference("Users").child(firebaseAuth.uid.toString())
                        .setValue(user)


                } else mutableStateFlow.value =
                    Status.Error(it.exception?.localizedMessage.toString())
            }
        return mutableStateFlow
    }

    private fun uploadImg(imgUri: Uri ,  need: NeedModel) {
        storage.getReference("coverImg/" + UUID.randomUUID()).putFile(imgUri)
            .onSuccessTask { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    firebaseDatabase.getReference("donaries")
                        .child(need.id!!)
                        .child("imgURL").setValue(it.toString())
                    Log.e( "uploadImg: ",it.toString() )
                }.addOnFailureListener {

                }
            }.addOnFailureListener {
                Log.e( "uploadImg: ",it.message.toString() )
            }
    }

    suspend fun uploadNeedToFirebase(need: NeedModel, imgUri: Uri): Flow<Status<String>> {
        val mutableStateFlow = MutableStateFlow<Status<String>>(Status.Loading)
        withContext(Dispatchers.IO) {
            need.id = firebaseAuth.currentUser!!.uid + Calendar.getInstance().timeInMillis
            firebaseDatabase.getReference("donaries")
                .child(need.id!!)
                .setValue(need).addOnSuccessListener {
                    mutableStateFlow.value = Status.Success("Upload Success")
                }.addOnFailureListener {
                    mutableStateFlow.value = Status.Error(it.message.toString())
                }
            uploadImg(imgUri ,need)
        }
        return mutableStateFlow


    }


    fun getNeed(): Flow<Status<List<NeedModel>>> {
        val result: MutableStateFlow<Status<List<NeedModel>>> =
            MutableStateFlow(Status.Loading)

        firebaseDatabase.getReference("donaries")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<NeedModel>()

                    snapshot.children.forEach {
                        list.add(it.getValue(NeedModel::class.java)!!)
                    }
                        result.value = Status.Success(list.toList())

                    Log.e("onDataChange: ", snapshot.value.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("onCancelled: ", error.message)
                    result.value = Status.Error( error.message)
                }

            })
        return result
    }


    suspend fun getUserInfo(): Flow<Status<UserModel?>> {

        val mutableStateFlow = MutableStateFlow<Status<UserModel?>>(Status.Loading)

        firebaseDatabase.getReference("Users").child(firebaseAuth.uid.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        mutableStateFlow.value =
                            Status.Success(snapshot.getValue(UserModel::class.java))
                        Log.e("onDataChange: ", snapshot.value.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mutableStateFlow.value = Status.Error( error.message)
                }

            })
        return mutableStateFlow
    }

    suspend fun updateProfile(value: UserModel): Flow<Status<String>> {
        val result: MutableStateFlow<Status<String>> = MutableStateFlow(Status.Loading)
        delay(200)
        firebaseDatabase.getReference("Users/" + firebaseAuth.currentUser?.uid)
            .setValue(value).addOnSuccessListener {
                result.value = Status.Success(" updated successfully")
            }.addOnFailureListener {
                result.value = Status.Error("Couldn't update try again later")
            }
        return result
    }

    suspend fun forgetPassword(email: String):Flow<Status<String>>{
        val result: MutableStateFlow<Status<String>> = MutableStateFlow(Status.Loading)
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            result.value = Status.Success("Check your email")
        }.addOnFailureListener {
            result.value = Status.Error(it.message.toString())
        }
        return result
    }

    suspend fun changeEmail(password: String,newEmail:String ):Flow<Status<String>>{
        val result: MutableStateFlow<Status<String>> = MutableStateFlow(Status.Loading)
        Log.e( "changeEmail: ",firebaseAuth.currentUser?.email!!.toString() )
        val credential = EmailAuthProvider.getCredential(firebaseAuth.currentUser?.email!!, password)
        firebaseAuth.currentUser?.reauthenticate(credential)?.addOnSuccessListener {
            firebaseAuth.currentUser?.updateEmail(newEmail)?.addOnSuccessListener {
                firebaseDatabase.getReference("Users/" + firebaseAuth.currentUser?.uid).child("email")
                    .setValue(newEmail)
                result.value = Status.Success("Email changed successfully")
            }?.addOnFailureListener {
                result.value = Status.Error(it.message.toString())
                Log.e( "changeEmail: ",it.message.toString() )

            }
        }?.addOnFailureListener {
            result.value = Status.Error(it.message.toString())
            Log.e( "changeEmail: ",it.message.toString() )

        }
        return result
    }
}

