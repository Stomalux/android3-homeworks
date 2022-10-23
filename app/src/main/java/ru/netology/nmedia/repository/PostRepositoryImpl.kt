package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import okhttp3.MediaType.Companion.toMediaType
//import java.nio.file.Files
//import java.nio.file.Paths
//import java.nio.file.StandardCopyOption
//import java.util.concurrent.TimeUnit

import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.dto.Post
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApiServiceHolder


class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {

        PostApiServiceHolder.service.getAllAsync()
            .enqueue(object : Callback<List<Post>> {

                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }


    override fun save(post: Post, callback: PostRepository.Callback<Post>) {

        PostApiServiceHolder.service.save(post)
            .enqueue(object : Callback<Post> {

                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("body is null"))
                        return
                    }
                    callback.onSuccess(body)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }

    override fun removeById(
        id: Long,
        callback: PostRepository.Callback<Unit>
    ) {     //PostRepository.Callback<Unit>) {

        PostApiServiceHolder.service.removeById(id)
            .enqueue(object : Callback<Unit> {

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })
    }


    override fun likeByIdAsync(post: Post, callback: PostRepository.Callback<Post>) {

        val id = post.id
        if (!post.likedByMe) {

            PostApiServiceHolder.service.likeByIdAsync(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val body = response.body() ?: run {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })
        } else {
            PostApiServiceHolder.service.DelitLikeByIdAsync(id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        if (!response.isSuccessful) {
                            callback.onError(RuntimeException(response.message()))
                            return
                        }
                        val body = response.body() ?: run {
                            callback.onError(RuntimeException("body is null"))
                            return
                        }
                        callback.onSuccess(body)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })

        }
    }

}


//    override fun likeByIdAsync(post: Post, callback: PostRepository.Callback<Post>) {
//        val id = post.id
//        if (post.likedByMe) {
//            val request: Request = Request.Builder()
//                .delete()
//                .url("${BASE_URL}/api/posts/$id/likes")
//                .build()
//
//            client.newCall(request)
//                .enqueue(object : Callback {
//                    override fun onResponse(call: Call, response: Response) {
//                        try {
//                            callback.onSuccess(post)
//                        } catch (e: Exception) {
//                            callback.onError(e)
//                        }
//                    }
//
//                    override fun onFailure(call: Call, e: IOException) {
//                        callback.onError(e)
//                    }
//                })
//
//        } else {
//            val request: Request = Request.Builder()
//                .post("".toRequestBody())
//                .url("${BASE_URL}/api/posts/$id/likes")
//                .build()
//
//            client.newCall(request)
//                .enqueue(object : Callback {
//                    override fun onResponse(call: Call, response: Response) {
//                    }
//
//                    override fun onFailure(call: Call, e: IOException) {
//                        callback.onError(e)
//                    }
//                })
//        }
//    }
//
//
//
//
//
//    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
//        println("Вод в репозиторий импл")
//        val request: Request = Request.Builder()
//            .post(gson.toJson(post).toRequestBody(jsonType))
//            .url("${BASE_URL}/api/slow/posts")
//            .build()
//
//        println("Создан запрос")
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    println(" override fun onResponse(call: Call, response: Response)")
//                   try {
//                        callback.onSuccess(post)
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//            })
//
//    }
//
//    override fun removeById(id: Long, callback: PostRepository.Callback<Post>) {
//        val request: Request = Request.Builder()
//            .delete()
//            .url("${BASE_URL}/api/slow/posts/$id")
//            .build()
//
//        client.newCall(request)
//            .enqueue(object : Callback {
//                override fun onResponse(call: Call, response: Response) {
//                    println(" override fun onResponse(call: Call, response: Response)")
//
//                    try {
//                        val post1 = Post(
//                            id = 0,
//                            author = "",
//                            authorAvatar= "",
//                            content = "",
//                            likedByMe = false,
//                            likes = 0,
//                            published = ""
//                        )
//                        callback.onSuccess(post1)
//                    } catch (e: Exception) {
//                        callback.onError(e)
//                    }
//                }
//
//                override fun onFailure(call: Call, e: IOException) {
//                    callback.onError(e)
//                }
//            })
//
//    }
//}
