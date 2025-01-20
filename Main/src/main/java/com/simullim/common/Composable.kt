package com.simullim.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simullim.compose.ui.theme.ParkGreen

@Composable
fun MenuButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick, modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, ParkGreen)
    ) {
        Text(text = buttonText, textAlign = TextAlign.Center, color = ParkGreen)
    }
}

@Composable
@Preview(showBackground = true)
private fun MenuButtonPreview() {
    MenuButton({}, buttonText = "test")
}
