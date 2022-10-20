package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import retrofit2.Callback
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun save(post: Post, callback: Callback<Post>)

    fun removeById(id: Long, callback: Callback<Unit>)

    fun likeByIdAsync(post: Post, callback: Callback<Post>)

    fun getAllAsync(callback: Callback< List<Post>>)

    interface Callback<T> {
        fun onSuccess(posts : T) {}
        fun onError(e: Exception) {}
    }
}
