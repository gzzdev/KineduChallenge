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
        binding.rvComics.adapter = comicsAdapter
        comicsVM.getComics()
        initUIState()
    }

    private fun initUIState() {
        var offset = 0
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comicsVM.comicsState.collect {
                    offset += 1
                    Log.i("MainActivity", "State: $it $offset")
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

    }

    private fun errorState() {

    }

    private fun successState(state: ComicsState.Success) {
        comicsAdapter.updateList(state.comics)
    }

    private fun onClickComic(comic: Comic) {

    }
}