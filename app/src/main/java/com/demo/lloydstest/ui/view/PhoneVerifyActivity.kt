package com.demo.lloydstest.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.demo.lloydstest.R
import com.demo.lloydstest.ui.components.BottomButton
import com.demo.lloydstest.ui.components.HeaderText
import com.demo.lloydstest.ui.components.LabelText
import com.demo.lloydstest.ui.components.LargeDropdownMenu
import com.demo.lloydstest.ui.components.PhoneDetailsView
import com.demo.lloydstest.ui.theme.LLOYDSTestTheme
import com.demo.lloydstest.ui.theme.labelTextStyle
import com.demo.lloydstest.ui.utils.DialogBoxLoading
import com.demo.lloydstest.ui.utils.Tag
import com.demo.lloydstest.ui.utils.Tag.COUNTRY_CODE
import com.demo.lloydstest.ui.utils.Tag.PHONE_NUMBER
import com.demo.lloydstest.ui.utils.hideKeyboard
import com.demo.lloydstest.ui.utils.isNetworkAvailable
import com.demo.lloydstest.utils.Utils
import com.demo.lloydstest.viewModel.ViewModelPhoneVerify
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhoneVerifyActivity : ComponentActivity() {
    private val vm: ViewModelPhoneVerify by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LLOYDSTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(state = rememberScrollState(), enabled = true)
                    ) {
                        HeaderText(application.resources.getString(R.string.phone_number_verify))
                        LabelText(application.resources.getString(R.string.select_country), textAlign = TextAlign.Start)
                        vm.countriesStateFlow.collectAsState().value.let {
                            if(it.isNotEmpty())
                                LargeDropdownMenu(
                                    label = application.resources.getString(R.string.country_code),
                                    items = it,
                                    selectedIndex = vm.selectedIndex,
                                    onItemSelected = { index, _ -> vm.selectedIndex = index },
                                    modifier = Modifier.testTag(COUNTRY_CODE)
                                )
                        }
                        LabelText("Enter Phone Number")
                        PhoneNumberTextField()
                        vm.open.collectAsState().value.let {
                            if(it) DialogBoxLoading()
                        }
                        BottomButton(application.resources.getString(R.string.verify)) {
                            this@PhoneVerifyActivity.hideKeyboard()
                            if(application.isNetworkAvailable()){
                                if(vm.selectedIndex>-1) {
                                    val code = vm.countriesStateFlow.value[vm.selectedIndex].let {
                                        it.diallingCode?.substring(1)
                                    }
                                    if(Utils.validationInput(code?:"", vm.phoneNumber)){
                                        vm.hitValidatePhoneNumber("${code}${vm.phoneNumber}")
                                    }else {
                                        Toast.makeText(this@PhoneVerifyActivity, application.resources.getString(R.string.please_enter_phone_number), Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    Toast.makeText(this@PhoneVerifyActivity, application.resources.getString(R.string.please_select_country_code) , Toast.LENGTH_LONG).show()
                                }
                            }else{
                                lifecycleScope.launch(Dispatchers.Default) {
                                    vm.sharedFlowMessage.emit(application.getString(R.string.network_not_available))
                                }
                            }
                        }
                        vm.verifyPhoneDetails.collectAsState().value?.let {
                            if(it.valid){
                                MessageText(application.resources.getString(R.string.phone_number_is_valid), textColor = Color(0XFF0277BD))
                                PhoneDetailsView(it)
                            }else{
                                MessageText(application.resources.getString(R.string.phone_number_is_not_valid), textColor = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            vm.sharedFlowMessage.collect{
                if(it.isNotEmpty()){
                    Toast.makeText(this@PhoneVerifyActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(application.isNetworkAvailable())
            vm.hitCountryCodesData()
        else{
            lifecycleScope.launch(Dispatchers.Default) {
                vm.sharedFlowMessage.emit(application.getString(R.string.network_not_available))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhoneNumberTextField() {
        TextField(
            value = vm.phoneNumber,
            onValueChange = {
                vm.phoneNumber = it
            },
            label = { Text(text = application.resources.getString(R.string.phone_number)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .fillMaxSize().testTag(PHONE_NUMBER),
        )
    }

    @Composable
    fun MessageText(
        text: String,
        modifier: Modifier = Modifier.testTag(Tag.VERIFY_MESSAGE),
        textColor:Color= Color.Black,
        style: TextStyle = labelTextStyle,
        textAlign: TextAlign = TextAlign.Start) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = text,
                style = style.copy(color=textColor),
                textAlign = textAlign
            )
        }
    }

}



