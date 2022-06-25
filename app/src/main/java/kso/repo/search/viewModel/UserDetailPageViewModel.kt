package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.Resource
import kso.repo.search.repository.AppRepository
import javax.inject.Inject

@HiltViewModel
class UserDetailPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: AppRepository
) :
    ViewModel() {

    private val login = savedStateHandle.get<String>("login")

    private val _userName = MutableStateFlow(login!!)

    private val submitEvent = MutableSharedFlow<Unit>()

    private val resource = submitEvent
        .map { _userName.value }
        .flatMapLatest { userRepository.getUser(it) }
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

    init {
        if(login!=null) Log.e("UserDetail - login ->", login);
        viewModelScope.launch {
            submitEvent.emit(Unit)
        }
    }

    fun setUserName(userName: String) {
        _userName.value = userName
    }

    fun submit() {
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