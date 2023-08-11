package com.demo.lloydstest.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.lloydstest.data.net.ApiProcessor
import com.demo.lloydstest.data.net.RetrofitApi
import com.demo.lloydstest.data.repository.Repository
import com.demo.lloydstest.models.Country
import com.demo.lloydstest.models.ValidateResponse
import com.demo.lloydstest.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ViewModelPhoneVerify @Inject constructor(val repository: Repository) : ViewModel() {

    val countriesStateFlow:MutableStateFlow<ArrayList<Country>> =  MutableStateFlow(arrayListOf())

//    val countrySelected:MutableStateFlow<Int> =  MutableStateFlow(0)

    var open:MutableStateFlow<Boolean> =  MutableStateFlow(false)

    var selectedIndex by mutableStateOf(-1)
    var phoneNumber by  mutableStateOf("")

    var verifyPhoneDetails:MutableStateFlow<ValidateResponse?> =  MutableStateFlow(null)

    var sharedFlowMessage: MutableSharedFlow<String> =  MutableSharedFlow()

    init {
        hitCountryCodesData()
    }

    private fun openDialog() {
        open.value = true
    }

    private fun closeDialog() {
        open.value= false
    }

    fun hitCountryCodesData() =
        viewModelScope.launch {
            kotlin.runCatching {
                repository.makeCall(
                    requestProcessor = object : ApiProcessor<Response<Map<String, Country>>> {
                        override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<Map<String, Country>> {
                            openDialog()
                            return retrofitApi.countries(header = Constants.API_KEY)
                        }

                        override fun onError(message: String, responseCode: Int) {
                            super.onError(message, responseCode)
                            viewModelScope.launch(Dispatchers.Main) {
                                sharedFlowMessage.emit(message)
                                closeDialog()
                            }
                        }

                        override fun onResponse(res: Response<Map<String, Country>>) {
                            closeDialog()
                            val data = ArrayList<Country>()
                            res.body()?.let { it ->
                                it.map { item ->
                                    data.add(
                                        Country(
                                            item.key,
                                            item.value.countryName,
                                            item.value.diallingCode
                                        )
                                    )
                                }
                            }
                            if(data.isNotEmpty()) {
                                countriesStateFlow.value = data
                            }
                        }
                    }
                )
            }.onFailure {
                it.printStackTrace()
                viewModelScope.launch(Dispatchers.Default) {
                    closeDialog()
                    sharedFlowMessage.emit("Some error occurred")
                }
            }
        }

    /**
     * API call of phone number validation
     * **/
     fun hitValidatePhoneNumber(phoneNumber:String) = viewModelScope.launch {
        repository.makeCall(
            requestProcessor = object : ApiProcessor<Response<ValidateResponse>> {
                override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<ValidateResponse> {
                    openDialog()
                    return retrofitApi.validateNumber(
                        header = Constants.API_KEY,
                        number = phoneNumber
                    )
                }
                override fun onError(message: String, responseCode: Int) {
                    super.onError(message, responseCode)
                    viewModelScope.launch(Dispatchers.Default) {
                        closeDialog()
                        sharedFlowMessage.emit(message)
                    }

                }

                override fun onResponse(res: Response<ValidateResponse>) {
                    closeDialog()
                    viewModelScope.launch(Dispatchers.Default) {
                        res.body()?.let {
                            verifyPhoneDetails.emit(it)
                        }
                    }
                }
            }
        )
    }

}