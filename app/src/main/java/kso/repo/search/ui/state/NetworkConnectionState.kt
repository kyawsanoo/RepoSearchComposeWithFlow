package kso.repo.search.ui.state

sealed class NetworkConnectionState {
    object Fetched : NetworkConnectionState()
    object Error : NetworkConnectionState()
}