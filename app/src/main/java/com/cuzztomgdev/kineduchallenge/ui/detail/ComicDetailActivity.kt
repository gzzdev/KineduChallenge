package com.cuzztomgdev.kineduchallenge.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.cuzztomgdev.kineduchallenge.databinding.ActivityComicDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.cuzztomgdev.kineduchallenge.R
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.ui.common.state.ComicsState
import com.cuzztomgdev.kineduchallenge.ui.common.state.CreatorState
import com.cuzztomgdev.kineduchallenge.ui.common.util.Utils.requestOptions
import com.cuzztomgdev.kineduchallenge.ui.home.adapter.ComicsAdapter

@AndroidEntryPoint
class ComicDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComicDetailBinding
    private val args: ComicDetailActivityArgs by navArgs()
    private val comicDetailVM: ComicDetailVM by viewModels()
    private val comicsAdapter: ComicsAdapter by lazy { ComicsAdapter(::onClickComic) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComicDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        comicDetailVM.comic = args.comic
        setup()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setup() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        bindComic()
        initUIState()
        setupRV()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun bindComic() {
        if (!comicDetailVM.comic.imageUri.contains("not_available"))
            Glide.with(this)
                .load(comicDetailVM.comic.imageUri)
                .apply(requestOptions)
                .placeholder(getDrawable(R.drawable.cover_placeholder))
                .into(binding.ivCover)
        binding.tvTitle.text = comicDetailVM.comic.title
        binding.tvDescription.text = comicDetailVM.comic.description.ifEmpty {
            getString(R.string.no_description)
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comicDetailVM.creatorsState.collect {
                    when (it) {
                        CreatorState.Start -> {
                            if (comicDetailVM.comic.creators.isNotEmpty()) {
                                comicDetailVM.creatorInfo()
                            } else {
                                errorState()
                            }
                        }
                        CreatorState.Loading -> loadingState()
                        is CreatorState.Success -> successState(it)
                        is CreatorState.Error -> errorState()
                    }
                }
            }
        }

    }

    private fun loadingState() {
        binding.cvCreator.isVisible = false
    }

    private fun errorState() {
        binding.cvCreator.isVisible = true
        binding.progressBar.isVisible = false
        binding.tvNotFound.isVisible = true
        Toast.makeText(this, "Creator not found", Toast.LENGTH_SHORT).show()
    }

    private fun setupRV() {
        if(binding.rvComics.adapter == null){
            binding.rvComics.adapter = comicsAdapter
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comicDetailVM.comicsState.collect {
                    when (it) {
                        ComicsState.Start -> {}
                        ComicsState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is ComicsState.Error -> {
                            binding.progressBar.isVisible = false
                            binding.tvNotFound.isVisible = true
                            Toast.makeText(
                                this@ComicDetailActivity,
                                "More comics not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is ComicsState.Success -> {
                            binding.progressBar.isVisible = false
                            comicsAdapter.updateList(it.comics)
                        }
                    }
                }
            }
        }

    }

    private fun onClickComic(comic: Comic) {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun successState(state: CreatorState.Success) {
        val creator = state.creator
        binding.cvCreator.isVisible = true
        binding.tvCreatorName.text = creator.name
        binding.tvCreatorRole.text = creator.role
        Log.i("ComicDetailActivity", "successState: $creator")
        if (creator.imageUri.isNotEmpty() && !creator.imageUri.contains("image_not_available")) {
            Glide.with(this)
                .load(creator.imageUri)
                .apply(requestOptions)
                .placeholder(getDrawable(R.drawable.baseline_person_pin_24))
                .error(getDrawable(R.drawable.baseline_person_pin_24))
                .into(binding.ivCreator)
        }
    }
}