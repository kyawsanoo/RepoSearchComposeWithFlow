package kso.repo.search.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kso.repo.search.model.User
import kso.repo.search.repository.AppRepository
import kso.repo.search.ui.state.SearchBoxViewModelState
import javax.inject.Inject

private const val TAG = "SearchBoxViewModel"

@HiltViewModel
class SearchBoxViewModel @Inject constructor(private val userRepository: AppRepository) : ViewModel() {

    private var allUsers: ArrayList<User> = ArrayList<User>()

    private val searchText: MutableStateFlow<String> = MutableStateFlow("")
    private var showProgressBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var matchedUsers: MutableStateFlow<List<User>> =  MutableStateFlow(arrayListOf())

    val userSearchModelState = combine(
        searchText,
        matchedUsers,
        showProgressBar
    ) { text, matchedUsers, showProgress ->

        SearchBoxViewModelState(
            text,
            matchedUsers,
            showProgress
        )
    }

    init {
       gitHubUsers()
    }

    @OptIn(FlowPreview::class)
    private fun gitHubUsers() {

        viewModelScope.launch {
            showProgressBar.value = true
            val users: List<User> = userRepository.getUserList()
                .map {it.data.orEmpty()}//mapping to userList
                .flatMapConcat { it.asFlow() }
                .toList()
            if (users.isNotEmpty()) {
                allUsers.addAll(users)
            }
            showProgressBar.value = false
        }

    }


    fun onSearchTextChanged(changedSearchText: String) {

        searchText.value = changedSearchText
        if (changedSearchText.isEmpty()) {
            matchedUsers.value = arrayListOf()
            return
        }
        val usersFromSearch = allUsers.filter { x ->
            x.login.startsWith(changedSearchText, true)

        }
        matchedUsers.value = usersFromSearch

    }

    fun onClearClick() {
        searchText.value = ""
        matchedUsers.value = arrayListOf()
    }


}

