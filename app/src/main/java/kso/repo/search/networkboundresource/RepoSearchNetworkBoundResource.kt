package kso.repo.search.networkboundresource

import android.util.Log
import kotlinx.coroutines.flow.*
import kso.repo.search.model.Resource
import retrofit2.Response
import java.lang.Exception

inline fun <ApiResponse, ResultType, RequestType> repoSearchNetworkBoundResource(

    // Fetches response
    crossinline fetchRemote: suspend () -> Response<ApiResponse>,

    // Extracts data from remote response (response.body()!!.items)
    crossinline getDataFromResponse: suspend (response: Response<ApiResponse>) -> RequestType,

    // Saves remote data to local db
    crossinline saveFetchResult: suspend (RequestType) -> Unit,

    // Fetches data from local database
    crossinline fetchLocal: suspend () -> Flow<ResultType>,


    crossinline shouldFetch: () -> Boolean = { false },


    ) = flow {

    Log.e("repoSearchNBResource", "shouldFetch: ${shouldFetch()}")

    if (shouldFetch()) {
        Log.e("repoSearchNBResource", "shouldFetch: true block")
        emit(Resource.Loading)

        try {
            Log.e("repoSearchNBResource", "try block calling Api")
            // Fetch data from remote api
            val response = fetchRemote()

            // Parse data from response
            val data = getDataFromResponse(response)

            // Response validation
            if (response.isSuccessful && data != null) {
                Log.e("repoSearchNBResource", "Save data to database")
                // Save data to database
                saveFetchResult(data)
            } else {
                Log.e("repoSearchNBResource", "Save data to database")
                Log.e("repoSearchNBResource", "error = ${response.message()}")
                // Emit error
                emit(Resource.Fail(error = response.message()))
            }
        } catch (e: Exception) {
            Log.e("repoSearchNBResource", "catch bloc Api error: ${e.message}")
            emit(Resource.Fail(error = "Network error!"))

        }

    }

    // get data from local db
    emitAll(
        fetchLocal().map {
            Log.e("repoSearchNBResource", "fetchLocal()")
            Resource.Success(it)
        }
    )

}