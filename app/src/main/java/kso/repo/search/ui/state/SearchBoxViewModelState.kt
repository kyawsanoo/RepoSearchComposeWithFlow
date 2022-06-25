package kso.repo.search.ui.state

import kso.repo.search.model.User


data class SearchBoxViewModelState(
    val searchText: String = "",
    val users: List<User> = arrayListOf(),
    val showProgressBar: Boolean = false
) {

    companion object {
        val Empty = SearchBoxViewModelState()
    }

}