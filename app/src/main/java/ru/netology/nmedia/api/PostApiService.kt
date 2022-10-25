package ru.netology.nmedia.api

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit


private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()

    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface PostApiService {

    @GET("posts")
    suspend  fun getAllAsync(): List<Post>

    @GET("posts/{id}")
    suspend  fun getBiId(@Path("id") id: Long): Post

    @POST("posts")
    suspend fun save(@Body post: Post): Post

    @POST("posts/{id}/likes")
    suspend fun likeByIdAsync(@Path("id") id: Long): Post

    @DELETE("posts/{id}/likes")
    suspend fun delitLikeByIdAsync(@Path("id") id: Long): Post

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long)
}
object PostApiServiceHolder {
    val service: PostApiService by lazy {
        retrofit.create()
    }
}

