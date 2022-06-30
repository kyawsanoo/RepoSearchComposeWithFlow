package kso.repo.search.ui.state

import kso.repo.search.model.Repo


data class RepoSearchBoxViewModelState(
    val searchText: String = "",
    val users: List<Repo> = listOf(),
    val showProgressBar: Boolean = false
) {

    companion object {
        val Empty = RepoSearchBoxViewModelState()
    }

}