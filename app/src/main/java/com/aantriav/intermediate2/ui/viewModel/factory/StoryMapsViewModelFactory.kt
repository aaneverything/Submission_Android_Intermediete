package com.aantriav.intermediate2.ui.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aantriav.intermediate2.data.repository.StoryRepository
import com.aantriav.intermediate2.di.Injection
import com.aantriav.intermediate2.ui.viewModel.StoryMapsViewModel

class StoryMapsViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryMapsViewModel::class.java)) {
            return StoryMapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryMapsViewModelFactory? = null

        fun getInstance(context: Context): StoryMapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryMapsViewModelFactory(
                    Injection.storyRepository(context),
                )
            }.also { instance = it }
    }
}