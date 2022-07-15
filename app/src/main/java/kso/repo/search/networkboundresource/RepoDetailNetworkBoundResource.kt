package kso.repo.search.networkboundresource

import android.util.Log
import kotlinx.coroutines.flow.*
import kso.repo.search.model.Resource
import retrofit2.Response
import java.lang.Exception

inline fun <ApiResponse, ResultType, RequestType> repoDetailNetworkBoundResource(

    // Fetches response
    crossinline fetchRemote: suspend () -> Response<ApiResponse>,

    // Extracts data from remote response (ex.: response.body()!!)
    crossinline getDataFromResponse: suspend (response: Response<ApiResponse>) -> RequestType,

    // Saves remote data to local db
    crossinline saveFetchResult: suspend (RequestType) -> Unit,

    // Fetches data from local database
    crossinline fetchLocal: suspend () -> Flow<ResultType>,


    crossinline shouldFetch: () -> Boolean = { false },


    ) = flow {

    Log.e("NetworkBoundResource", "shouldFetch: ${shouldFetch()}")

    if (shouldFetch()) {
        Log.e("NetworkBoundResource", "shouldFetch: true block")
        emit(Resource.Loading)

        try {
            Log.e("NetworkBoundResource", "try block calling Api")
            // Fetch data from remote api
            val response = fetchRemote()

            // Parse data from response
            val data = getDataFromResponse(response)

            // Response validation
            if (response.isSuccessful && data != null) {
                Log.e("NetworkBoundResource", "Save data to database")
                // Save data to database
                saveFetchResult(data)
            } else {
                Log.e("NetworkBoundResource", "Save data to database")
                Log.e("NetworkBoundResource", "error = ${response.message()}")
                // Emit error
                emit(Resource.Fail(error = response.message()))
            }
        } catch (e: Exception) {
            Log.e("NetworkBoundResource", "catch bloc Api error: ${e.message}")
            emit(Resource.Fail(error = "Network error!"))

        }

    }

    // get data from local db
    emitAll(
        fetchLocal().map {
            Log.e("NetworkBoundResource", "fetchLocal()")
            Resource.Success(it)
        }
    )

}