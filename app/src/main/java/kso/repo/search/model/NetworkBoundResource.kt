package kso.repo.search.model

import android.util.Log
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(


    crossinline query: () -> Flow<ResultType>,

    crossinline fetch: suspend () -> RequestType,

    crossinline filterFetch: suspend (ResultType, RequestType) -> RequestType,

    crossinline saveFetchResult: suspend (RequestType) -> Unit,

    crossinline shouldFetch: () -> Boolean = { false },


    ) = flow {

    Log.e("NetworkBoundResource", "shouldFetch: ${shouldFetch()}")

    val flow = if (shouldFetch()) {
        Log.e("NetworkBoundResource", "shouldFetch: true block")
        emit(Resource.Loading)

        try {
            Log.e("NetworkBoundResource", "shouldFetch: true try block")
            saveFetchResult(filterFetch(query().first(), fetch()))
            query().map { Resource.Success(it) }

        } catch (t: Throwable) {
            Log.e("NetworkBoundResource", "shouldFetch: true catch block")
            query().map { Resource.Fail(t.message.toString()) }
        }

    } else {
        Log.e("NetworkBoundResource", "shouldFetch: false block")
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}