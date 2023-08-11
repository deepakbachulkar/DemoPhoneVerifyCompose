package com.demo.lloydstest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.demo.lloydstest.models.ValidateResponse
import com.demo.lloydstest.ui.theme.buttonStyle
import com.demo.lloydstest.ui.theme.labelTextStyle
import com.demo.lloydstest.ui.theme.titleTextStyle
import com.demo.lloydstest.ui.theme.valueTextStyle


@Composable
fun HeaderText(
    text: String,
    modifier: Modifier = Modifier.background(color = Color.DarkGray),
    textAlign: TextAlign = TextAlign.Start
) {
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = titleTextStyle,
            textAlign = textAlign
        )
    }
}

@Composable
fun LabelText(
    text: String,
    textColor:Color= Color.Black,
    modifier: Modifier = Modifier,
    style: TextStyle = labelTextStyle,
    textAlign: TextAlign = TextAlign.Start) {
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = text,
            style = style.copy(color=textColor),
            textAlign = textAlign
        )
    }
}

@Composable
fun BottomButton(
    text:String,
    onClick: () -> Unit,) {
    Button(
        modifier = Modifier.padding(20.dp, 40.dp).fillMaxSize().height(55.dp),
        onClick = onClick,
    ){
        Text(text = text, style = buttonStyle)
    }

}

@Composable
fun PhoneDetailsView(
    text: ValidateResponse,
    styleLabel: TextStyle = labelTextStyle,
    styleValue: TextStyle = valueTextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Column(
        modifier = modifier.wrapContentWidth().padding(40.dp, 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        ) {
        Text(
            text = "Phone Number Info",
            style = styleLabel,
            textAlign = textAlign,
        )
        Text(
            text = text.countryName,
            style = styleValue,
            textAlign = textAlign,
        )
        Text(
            text = text.internationalFormat,
            style = styleValue,
            textAlign = textAlign,
        )
        Text(
            text = "Carrier: ${text.carrier}",
            style = styleValue,
            textAlign = textAlign,
        )
        Text(
            text = "Location: ${text.location}",
            style = styleValue,
            textAlign = textAlign,
        )
    }
}
