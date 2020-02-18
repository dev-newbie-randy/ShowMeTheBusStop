package kr.pe.randy.showmethebusstop.data.repository

import kr.pe.randy.showmethebusstop.data.remote.NetworkResult
import retrofit2.Response
import java.io.IOException

open class BaseRepository {
    suspend fun <T : Any> apiOutput(
        call: suspend () -> Response<T>,
        error: String
    ): NetworkResult<T> {
        val response = call.invoke()
        return if (response.isSuccessful)
            NetworkResult.Success(response.body()!!)
        else
            NetworkResult.Error(IOException(error))
    }
}