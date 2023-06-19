package com.example.proyectodonapawfinal.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Provides
    fun firebaseAuthProvider(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    fun firebaseDatabaseProvider(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun firebaseStorageProvider(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}