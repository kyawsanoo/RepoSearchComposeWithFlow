package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Repo
import kso.repo.search.model.Resource
import kso.repo.search.repository.RepoDetailRepository
import javax.inject.Inject

@HiltViewModel
class RepoDetailPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RepoDetailRepository
) :
    ViewModel() {

    private val jsonString = savedStateHandle.get<String>("repo")
    private val argRepo = jsonString?.fromJson(Repo::class.java)
    val repoResource = MutableStateFlow<Resource<Repo>>(Resource.Loading)
    private val tag: String = "RepoDetailViewModel"
    val isRefreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        repoResource.toJson()?.let { Log.e(tag, "repo:  $it" ) }
        submit()
    }
    private fun submit(){
        viewModelScope.launch {
            argRepo?.id?.let {
                argRepo.owner.login?.let { it1 ->
                    argRepo.name?.let { it2 ->
                        repository.getRepoDetailNetworkBoundResource(it, it1, it2).collect {
                            repoResource.value = it
                        }
                    }
                }
            }
        }
    }

    private fun <A> String.fromJson(type: Class<A>): A {
        return Gson().fromJson(this, type)
    }


    private fun <A> A.toJson(): String? {
        return Gson().toJson(this)
    }

    fun refresh(){
        Log.e(tag, "Refresh:")
        isRefreshing.value = true
        submit()
    }

    fun retry(){
        Log.e(tag, "Retry:")
        isRefreshing.value = true
        submit()
    }

    fun onDoneCollectResource(){
        Log.e(tag, "onDoneCollectResource()")
        isRefreshing.value = false
    }
}