package kso.repo.search.model

import android.util.Log
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(

    crossinline query: () -> Flow<ResultType>,

    crossinline fetch: suspend () -> RequestType,

    crossinline filterFetch: suspend (ResultType, RequestType) -> RequestType,


    crossinline saveFetchResult: suspend (RequestType) -> Unit,

    crossinline shouldFetch: (ResultType) -> Boolean = { true }

) = flow {

    val data = query().first()

    Log.e("NetworkBoundResource", "shouldFetch: ${shouldFetch(data)}")

    val flow = if (shouldFetch(data)) {
        Log.e("NetworkBoundResource", "shouldFetch: true block")
        emit(Resource.Loading)

        try {
            saveFetchResult(filterFetch(query().first(), fetch()))
            query().map { Resource.Success(it) }

        } catch (t: Throwable) {
            query().map { Resource.Fail(t.message.toString()) }
        }

    } else {
        Log.e("NetworkBoundResource", "shouldFetch: false block")
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}