package com.cuzztomgdev.kineduchallenge.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cuzztomgdev.kineduchallenge.databinding.ItemComicBinding
import com.cuzztomgdev.kineduchallenge.domain.model.Comic

class ComicsAdapter(
    private val onClick: (Comic) -> Unit,
    private var comics: List<Comic> = emptyList()
): RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    companion object {
        private val PARENTHESES_REGEX = "\\(.*?\\)".toRegex()
        private val requestOptions = RequestOptions().centerCrop() // To fit cover
        fun formatTitle(title: String): String {
            // To remove the year from the title and another info
            return PARENTHESES_REGEX.replace(title, "")
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list:List<Comic>){
        comics = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ComicViewHolder(
            ItemComicBinding.inflate(inflater, parent, false)
        )
    }

    override fun getItemCount(): Int = comics.size

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(comics[position], onClick)
    }

    inner class ComicViewHolder(
        private val binding: ItemComicBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: Comic, onClick: (Comic) -> Unit) {
            val context = binding.root.context
            binding.tvTitle.text = formatTitle(comic.title)
            Glide.with(context)
                .load(comic.imageUri)
                .apply(requestOptions)
                //.placeholder() TODO: add placeholder
                .into(binding.ivCover)
            binding.parent.setOnClickListener {
                onClick(comic)
            }
        }
    }
}
