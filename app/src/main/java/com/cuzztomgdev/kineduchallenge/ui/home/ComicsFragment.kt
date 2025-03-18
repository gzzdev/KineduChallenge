package com.cuzztomgdev.kineduchallenge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cuzztomgdev.kineduchallenge.databinding.FragmentComicsBinding
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.ui.common.state.ComicsState
import com.cuzztomgdev.kineduchallenge.ui.home.adapter.ComicsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComicsFragment : Fragment() {

    private val comicsVM: ComicsVM by viewModels<ComicsVM>()
    private val comicsAdapter: ComicsAdapter by lazy { ComicsAdapter(::onClickComic) }

    private var _binding: FragmentComicsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComicsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Main Activity", "onViewCreated")
        setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setup() {
        setupRV()
        initUIState()
        binding.svComics.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { comicsVM.searchComics(newText) }
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { comicsVM.searchComics(query) }
                return true
            }
        })
    }

    private fun setupRV() {
        if(binding.rvComics.adapter == null){
            binding.rvComics.adapter = comicsAdapter
        }

        binding.rvComics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                val visibleThreshold = layoutManager.spanCount * 3 // Ajuste dinámico
                Log.i("Main Activity", "onScrolled: $lastVisibleItem $visibleThreshold")

                if (!comicsVM.isLoading() && totalItemCount >= comicsVM.totalComics() && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    comicsVM.getComics(true, totalItemCount, 12)
                }
            }
        })

    }
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comicsVM.comicsState.collect {
                    when (it) {
                        ComicsState.Start -> {
                            comicsVM.getComics(false,0, 12)
                        }
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
        Toast.makeText(requireContext(), "Comics not found", Toast.LENGTH_SHORT).show()
    }

    private fun successState(state: ComicsState.Success) {
        comicsAdapter.hideLoading()
        comicsAdapter.updateList(state.comics)
    }

    private fun onClickComic(comic: Comic) {
        findNavController().navigate(
            ComicsFragmentDirections.actionComicsFragmentToComicDetailActivity(comic),
        )
    }
}