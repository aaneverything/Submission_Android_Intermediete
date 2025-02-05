package com.aantriav.intermediate2.data.remote.response

data class StoryResponse(
    val listStory: List<ListStoryItem>,
    val error: Boolean,
    val message: String
)

data class ListStoryItem(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double,
    val id: String,
    val lat: Double
)
