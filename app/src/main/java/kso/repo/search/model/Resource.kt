package kso.repo.search.model

sealed class Resource<out T> {
    object Start : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    data class Success<out T>(
        val value: T
    ) : Resource<T>()

    data class Fail(
        val error: String
    ) : Resource<Nothing>()

    val isLoading get() = this is Loading
    val isFail get() = this is Fail
    val errorMessage get() = (this as? Fail)?.error
    val data get() = (this as? Success)?.value
}