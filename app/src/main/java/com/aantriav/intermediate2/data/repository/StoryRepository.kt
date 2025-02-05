package com.aantriav.intermediate2.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.aantriav.intermediate2.data.local.db.StoryDatabase
import com.aantriav.intermediate2.data.remote.response.StoryDetailResponse
import com.aantriav.intermediate2.data.remote.response.StoryResponse
import com.aantriav.intermediate2.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.aantriav.intermediate2.data.Result
import com.aantriav.intermediate2.data.local.db.ListStoryItem
import com.aantriav.intermediate2.data.remote.StoryRemoteMediator
import com.aantriav.intermediate2.data.remote.response.StoryUploadResponse

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    suspend fun getAllStories(
        page: Int? = null,
        size: Int? = null,
        location: Int = 0
    ): Result<StoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStories(page, size, location)
                Log.d("StoryRepository", "getAllStories: $response")

                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun getStory(id: String): Result<StoryDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStory(id)
                Log.d("StoryRepository", "getStory: $response")

                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun getAllStoriesWithMap(
        location: Int = 1
    ): Result<StoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllStoriesWithMap(location)
                Log.d("StoryRepository", "getAllStoriesWithMap: $response")
                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun uploadStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Result<StoryUploadResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.uploadStory(description, photo, lat, lon)

                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    fun getPaginatedStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(
                database = storyDatabase,
                apiService = apiService,
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDatabase)
            }.also { instance = it }
    }
}
