package com.cuzztomgdev.kineduchallenge.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuzztomgdev.kineduchallenge.databinding.ActivityMainBinding
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.ui.home.adapter.ComicsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val comicsVM: ComicsVM by viewModels()
    private val comicsAdapter: ComicsAdapter by lazy { ComicsAdapter(::onClickComic) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setup()
    }

    private fun setup() {
        setupRV()
        initUIState()
    }
    private fun setupRV() {
        binding.rvComics.adapter = comicsAdapter
        comicsVM.getComics(0, 12)
        binding.rvComics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val visibleThreshold = layoutManager.spanCount * 3 // Ajuste dinámico

                if (!comicsVM.isLoading() && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    comicsVM.getComics(totalItemCount, 12)
                }
            }
        })

    }
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comicsVM.comicsState.collect {
                    when (it) {
                        ComicsState.Loading -> loadingState()
                        is ComicsState.Error -> errorState()
                        is ComicsState.Success -> successState(it)
                    }
                }
            }
        }
    }

    private fun loadingState() {
        Log.i("Main Activity", "Loading state")
        comicsAdapter.showLoading()
    }

    private fun errorState() {
        comicsAdapter.hideLoading()
    }

    private fun successState(state: ComicsState.Success) {
        comicsAdapter.hideLoading()
        comicsAdapter.updateList(state.comics)
    }

    private fun onClickComic(comic: Comic) {
        Log.i("Main Activity", "onClickComic: ${comic.id}")
    }
}