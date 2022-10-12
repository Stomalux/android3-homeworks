package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
   // fun getAll(): List<Post>

    fun save(post: Post)
    fun removeById(id: Long)


    fun likeByIdAsync(post: Post, callback: Callback<Post>)

    fun getAllAsync(callback: Callback< List<Post>>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }

}
