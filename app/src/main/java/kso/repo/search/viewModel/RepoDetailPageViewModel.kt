package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class RepoDetailPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) :
    ViewModel() {

    private val jsonString = savedStateHandle.get<String>("repo")
    private val argRepo = jsonString?.fromJson(Repo::class.java)
    val repo = MutableStateFlow(argRepo)
    private val tag: String = "RepoDetailViewModel"


    init {
        repo.toJson()?.let { Log.e(tag, "repo:  $it" ) };
    }

    private fun <A> String.fromJson(type: Class<A>): A {
        return Gson().fromJson(this, type)
    }


    private fun <A> A.toJson(): String? {
        return Gson().toJson(this)
    }
}