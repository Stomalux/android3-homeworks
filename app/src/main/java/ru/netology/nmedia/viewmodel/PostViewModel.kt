package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(application).postDao()
    )

    val data: LiveData<FeedModel> = repository.data.map { FeedModel(it, it.isEmpty()) }


    /////////////////////////////////////////////////////////////////////////
    private val _state = MutableLiveData<FeedModelState>()
    //private val _state = MutableLiveData<FeedModelState>(FeedModelState.Idle)




    val state: LiveData<FeedModelState>
        get() = _state


    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

//    fun loadPosts() {
//        viewModelScope.launch {
//            _state.value = FeedModelState.Loading
//            try {
//                repository.getAllAsync()
//                _state.value = FeedModelState.Idle
//            } catch (e: Exception) {
//                _state.value = FeedModelState.Error
//
//            }
//        }
//    }

 ////////////////////////////////////////////////////////////////////////////////////////////////
 fun loadPosts() = viewModelScope.launch {
     try {
         _state.value = FeedModelState(loading = true)
         repository.getAllAsync()
         _state.value = FeedModelState()
     } catch (e: Exception) {
         _state.value = FeedModelState(error = true)
     }
 }

//    fun loadPosts() = viewModelScope.launch {
//        try {
//            _state.value =FeedModelState.Loading// FeedModelState(loading = true)
//            repository.getAllAsync()
//            _state.value = FeedModelState.Idle //FeedModelState()
//        } catch (e: Exception) {
//            _state.value = FeedModelState.Error //FeedModelState(error = true)
//        }
//    }
    fun likeById(post: Post) {
        viewModelScope.launch { }

        // repository.likeByIdAsync(post)

        _postCreated.postValue(Unit)
        loadPosts()
    }


    fun save() {
        viewModelScope.launch {
            edited.value?.let {

                //         println("do do")
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.value = empty
        }
    }


    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
            loadPosts()
//            override fun onSuccess(posts: Unit) {
//
//            }
//
//            override fun onError(e: Exception) {
//                _data.value = (FeedModel(error = true))
//            }
//        })
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun refresh() = viewModelScope.launch {
        try {
            _state.value = FeedModelState(refreshing = true)
            repository.getAllAsync()
            _state.value = FeedModelState()
        } catch (e: Exception) {
            _state.value = FeedModelState(error = true)
        }
    }




//    fun refresh() {
//        viewModelScope.launch {
//            _state.value = FeedModelState.Refreshing
//            try {
//                repository.getAllAsync()
//                _state.value = FeedModelState.Idle
//            } catch (e: Exception) {
//                _state.value = FeedModelState.Error
//
//            }
//        }
//    }

}