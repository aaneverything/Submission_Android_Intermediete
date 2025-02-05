package com.aantriav.intermediate2.data.remote.retrofit

import com.aantriav.intermediate2.data.remote.response.LoginResponse
import com.aantriav.intermediate2.data.remote.response.RegisterResponse
import com.aantriav.intermediate2.data.remote.response.StoryDetailResponse
import com.aantriav.intermediate2.data.remote.response.StoryResponse
import com.aantriav.intermediate2.data.remote.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int = 0
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStory(
        @Path("id") id: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): StoryUploadResponse

    @GET("stories")
    suspend fun getAllStoriesWithMap(
        @Query("location") location: Int = 1
    ): StoryResponse
}