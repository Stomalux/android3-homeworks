package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import retrofit2.Callback
import ru.netology.nmedia.dto.Post

interface PostRepository {

    val data: LiveData<List<Post>>

    suspend  fun save(post: Post)

    suspend  fun removeById(id: Long)

    suspend fun likeByIdAsync(post: Post)

    suspend  fun getAllAsync(): List<Post>
//
//    interface Callback<T> {
//        fun onSuccess(posts : T) {}
//        fun onError(e: Exception) {}
//    }
}
