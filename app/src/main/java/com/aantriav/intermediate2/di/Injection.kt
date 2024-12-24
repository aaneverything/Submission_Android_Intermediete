package com.aantriav.intermediate2.di

import android.content.Context
import com.aantriav.intermediate2.data.local.db.StoryDatabase
import com.aantriav.intermediate2.data.pref.UserPreferences
import com.aantriav.intermediate2.data.remote.retrofit.ApiConfig
import com.aantriav.intermediate2.data.repository.AuthRepository
import com.aantriav.intermediate2.data.repository.StoryRepository

object Injection {

    fun authRepository(context: Context): AuthRepository {
        val pref = UserPreferences(context)
        val apiService = ApiConfig.getApiService(pref)
        return AuthRepository.getInstance(apiService)
    }

    fun storyRepository(context: Context): StoryRepository {
        val pref = UserPreferences(context)
        val apiService = ApiConfig.getApiService(pref)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, database)
    }
}
