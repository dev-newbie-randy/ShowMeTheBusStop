package kr.pe.randy.showmethebusstop.data.repository

import kr.pe.randy.showmethebusstop.data.remote.NetworkResult
import retrofit2.Response

open class BaseRepository {
    suspend fun <T : Any> apiOutput(
        call: suspend () -> Response<T>,
        error: String
    ): NetworkResult<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (e: Exception) {
            return NetworkResult.Error(Exception(e.message))
        }
        return if (response.isSuccessful)
            NetworkResult.Success(response.body()!!)
        else
            NetworkResult.Error(Exception(error))
    }
}