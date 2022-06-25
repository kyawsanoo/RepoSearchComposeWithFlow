package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Resource
import kso.repo.search.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class RepoDetailPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: AppRepository
) :
    ViewModel() {

    private val login = savedStateHandle.get<String>("login")
    private val _userName = MutableStateFlow(login!!)

    private val repoName = savedStateHandle.get<String>("repoName")
    private val _repoName = MutableStateFlow(repoName!!)

    private val submitEvent = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val resource = submitEvent
        .map { _userName.value }
        .flatMapLatest { userRepository.getRepoDetail(it, _repoName.value) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading)

    val isLoading = resource.map {
        it.isLoading
    }

    val isFail = resource.map {
        it.isFail
    }

    val data = resource.map {
        it.data
    }

    val errorMessage = resource.map {
        when(it.errorMessage.orEmpty().contains("java.net.UnknownHostException")){
            true -> "Please check your connection and retry"
            else -> {
                it.errorMessage.orEmpty()
            }
        }
    }

    init {
        if(login!=null) Log.e("RepoDetail - login ->", login);
        if(repoName!=null) Log.e("RepoDetail - repo ->", repoName)

        viewModelScope.launch {
            submitEvent.emit(Unit)
        }
    }

    fun retry() {
        viewModelScope.launch {
            submitEvent.emit(Unit)
        }
    }

}