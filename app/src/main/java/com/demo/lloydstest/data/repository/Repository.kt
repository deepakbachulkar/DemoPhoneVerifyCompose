package com.demo.lloydstest.data.repository

import com.demo.lloydstest.data.net.ApiProcessor
import com.demo.lloydstest.data.net.RetrofitApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val retrofitApi: RetrofitApi,
) {
    fun <T> makeCall(
        requestProcessor: ApiProcessor<Response<T>>,
    ) {
        getResponseFromCall(requestProcessor)
    }

    private fun <T> getResponseFromCall(
        requestProcessor: ApiProcessor<Response<T>>,
    ) {
        try {
            val dataResponse: Flow<Response<Any>> = flow {
                val response = requestProcessor.sendRequest(retrofitApi) as Response<Any>
                emit(response)
            }.flowOn(Dispatchers.IO)

            //UI
            CoroutineScope(Dispatchers.Default).launch {
                dataResponse.catch { exception ->
                    exception.printStackTrace()
                    requestProcessor.onError("Something Went Wrong, Please Try Again", 2)
                }.collect { response ->
                    when {
                        response.isSuccessful -> {
                            /**Success*/
                            requestProcessor.onResponse(response as Response<T>)
                        }
                        else -> {
                            /**ClientErrors*/
                            kotlin.runCatching {
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = try {
                                    JSONObject(errorBody ?: "").getString("message")
                                } catch (e: JSONException) {
                                    "Something Went Wrong, Please Try Again"
                                }
                                requestProcessor.onError(errorMessage, response.code())
                            }.onFailure {
                                requestProcessor.onError("Something Went Wrong, Please Try Again.", response.code())
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
