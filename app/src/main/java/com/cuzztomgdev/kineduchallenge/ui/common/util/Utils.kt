package com.cuzztomgdev.kineduchallenge.ui.common.util

import com.bumptech.glide.request.RequestOptions

object Utils {
    private val PARENTHESES_REGEX = "\\(.*?\\)".toRegex()
    val requestOptions = RequestOptions().centerCrop() // To fit cover

    fun formatTitle(title: String): String {
        // To remove the year from the title and another info
        return PARENTHESES_REGEX.replace(title, "")
    }
}