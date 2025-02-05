package com.aantriav.intermediate2.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aantriav.intermediate2.data.local.db.ListStoryItem
import com.aantriav.intermediate2.databinding.StoryItemBinding
import com.aantriav.intermediate2.ui.screen.DetailActivity
import com.squareup.picasso.Picasso


class StoryAdapterPaging
    : PagingDataAdapter<ListStoryItem, StoryAdapterPaging.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.nameTextView.text = story.name
            binding.descriptionTextView.text = story.description
            Picasso.get()
                .load(story.photoUrl)
                .resize(600, 600)
                .centerCrop()
                .into(binding.storyImageView)

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailActivity::class.java).apply {
                    putExtra("STORY_ID", story.id)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}