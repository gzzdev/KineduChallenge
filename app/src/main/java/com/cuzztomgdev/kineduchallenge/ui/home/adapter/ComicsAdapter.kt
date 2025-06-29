package com.cuzztomgdev.kineduchallenge.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cuzztomgdev.kineduchallenge.R
import com.cuzztomgdev.kineduchallenge.databinding.ItemComicBinding
import com.cuzztomgdev.kineduchallenge.databinding.ItemLoadingBinding
import com.cuzztomgdev.kineduchallenge.domain.model.Comic
import com.cuzztomgdev.kineduchallenge.ui.home.ViewHolderType
import com.cuzztomgdev.kineduchallenge.ui.common.util.Utils.formatTitle
import com.cuzztomgdev.kineduchallenge.ui.common.util.Utils.requestOptions
class ComicsAdapter(
    private val onClick: (Comic) -> Unit,
    private var comics: MutableList<Comic> = mutableListOf()
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isLoading = false

    fun updateList(newComics: List<Comic>){
        val diffCallback = ComicDiffCallback(comics, newComics)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        comics.clear()
        comics.addAll(newComics)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (ViewHolderType.entries[viewType]) {
            ViewHolderType.COMIC -> {
                val binding = ItemComicBinding.inflate(inflater, parent, false)
                ComicViewHolder(binding)
            }
            ViewHolderType.LOADING -> {
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                LoadingViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < comics.size) ViewHolderType.COMIC.ordinal else ViewHolderType.LOADING.ordinal
    }

    override fun getItemCount(): Int = comics.size + if (isLoading) 1 else 0


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ComicViewHolder) {
            holder.bind(comics[position], onClick)
        }
    }

    inner class ComicViewHolder(
        private val binding: ItemComicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(comic: Comic, onClick: (Comic) -> Unit) {
            val context = binding.root.context
            binding.root.animation =
                AnimationUtils.loadAnimation(context, R.anim.comic_transation)
            binding.tvTitle.text = formatTitle(comic.title)
            Glide.with(context).clear(binding.ivCover)
            if (!comic.imageUri.contains("not_available")){
                Glide.with(context)
                    .load(comic.imageUri)
                    .apply(requestOptions)
                    .placeholder(context.getDrawable(R.drawable.cover_placeholder))
                    .into(binding.ivCover)
            }

            binding.parent.setOnClickListener {
                onClick(comic)
            }
        }
    }

    inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun showLoading() {
        isLoading = true
        notifyItemInserted(comics.size)
    }

    fun hideLoading() {
        isLoading = false
        notifyItemRemoved(comics.size)
    }
}
