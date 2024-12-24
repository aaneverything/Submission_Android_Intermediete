package com.aantriav.intermediate2.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.aantriav.intermediate2.R
import com.aantriav.intermediate2.databinding.ActivityDetailBinding
import com.aantriav.intermediate2.ui.viewModel.DetailViewModel
import com.aantriav.intermediate2.ui.viewModel.factory.DetailViewModelFactory
import com.aantriav.intermediate2.utils.dateFormatter
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels {
        DetailViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbarDetail.toolbar)
        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        binding.toolbarDetail.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val storyId = intent.getStringExtra("STORY_ID")

        if (storyId != null) {
            detailViewModel.getStory(storyId)
        } else {
           Toast.makeText(this, getString(R.string.invalid_story_id), Toast.LENGTH_SHORT).show()
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {

        detailViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })

        detailViewModel.story.observe(this, Observer { storyDetail ->
            if (storyDetail != null) {
                binding.apply {
                    Picasso.get()
                        .load(storyDetail.story.photoUrl)
                        .into(binding.previewImageView)
                    binding.nameTextView.text = storyDetail.story.name
                    binding.clockTextView.text = dateFormatter(storyDetail.story.createdAt)
                    binding.descriptionTextView.text = storyDetail.story.description
                }
            }
        })

        detailViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
             Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.detailLayout.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.detailLayout.visibility = View.VISIBLE
    }
}