package kso.repo.search.viewModel

sealed class NetworkConnectionState {
    object Fetched : NetworkConnectionState()
    object Error : NetworkConnectionState()
}