package com.demo.lloydstest.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.demo.lloydstest.models.ValidateResponse
import com.demo.lloydstest.ui.theme.labelTextStyle
import com.demo.lloydstest.ui.theme.valueTextStyle
import com.demo.lloydstest.ui.utils.Tag

@Composable
fun PhoneDetailsView(
    text: ValidateResponse,
    styleLabel: TextStyle = labelTextStyle,
    styleValue: TextStyle = valueTextStyle,
    modifier: Modifier = Modifier.testTag(Tag.PHONE_VERIFY_DETAILS),
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
