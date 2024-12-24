package com.aantriav.intermediate2

import com.aantriav.intermediate2.data.local.db.ListStoryItem
import java.util.UUID
import kotlin.random.Random

object DataDummy {

    fun generatedDummyStoryResponse(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = mutableListOf()
        for (index in 0 until 100) {
            val randomDay = (index % 28) + 1 // Random day within a month
            val randomHour = (index % 24)   // Random hour of the day
            val randomMinute = (index % 60) // Random minute of the hour

            val storyItem = ListStoryItem(
                photoUrl = "https://random-image-pepebigotes.vercel.app/api/random-image?random=$index&type=varied",
                createdAt = "2024-${(index % 12) + 1}-$randomDay$randomHour:$randomMinute:00Z", // Varied month, day, and time
                name = "Unique Story $index",
                description = if (index % 2 == 0) "Even Story Description $index" else "Odd Story Description $index",
                lon = Random.nextDouble(-180.0, 180.0), // Longitude (-180 to 180)
                id = UUID.randomUUID().toString(),
                lat = Random.nextDouble(-90.0, 90.0)    // Latitude (-90 to 90)
            )
            storyList.add(storyItem)
        }
        return storyList
    }
}
