package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeByIdAsync(post: Post, callback: PostRepository.Callback<Post>) {
        val id = post.id
        if (post.likedByMe) {
            val request: Request = Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            callback.onSuccess(post)
                        } catch (e: Exception) {
                            callback.onError(e)
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }
                })

        } else {
            val request: Request = Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        callback.onError(e)
                    }
                })
        }
    }

    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        println("Вод в репозиторий импл")
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        println("Создан запрос")
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println(" override fun onResponse(call: Call, response: Response)")
                   try {
                        callback.onSuccess(post)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })

    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Post>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    println(" override fun onResponse(call: Call, response: Response)")
//                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        val post1 = Post(
                            id = 0,
                            content = "",
                            author = "",
                            likedByMe = false,
                            likes = 0,
                            published = ""
                        )
                        callback.onSuccess(post1)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })

    }
}
