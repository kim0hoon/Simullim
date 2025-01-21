package com.simullim.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.ParkGreen
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.compose.ui.theme.Typography

@Composable
fun RoundedParkGreenButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    innerPaddingHorizontal: Dp = 0.dp,
    innerPaddingVertical: Dp = 0.dp
) {
    OutlinedButton(
        onClick = onClick, modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, ParkGreen)
    ) {
        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            color = ParkGreen,
            modifier = Modifier.padding(
                horizontal = innerPaddingHorizontal,
                vertical = innerPaddingVertical
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun RoundedParkGreenButtonPreview() {
    RoundedParkGreenButton({}, buttonText = "test")
}

@Composable
fun RoundedParkGreenBox(
    width: Dp = 2.dp,
    radius: Dp = 10.dp,
    modifier: Modifier = Modifier,
    boxScope: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = width,
                color = ParkGreen,
                shape = RoundedCornerShape(radius)
            )
            .background(color = DarkGrey)
    ) {
        boxScope()
    }
}

@Preview(showBackground = true)
@Composable
private fun RoundedParkGreenBoxPreview() {
    RoundedParkGreenBox {
        Spacer(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun TwoButtonDialog(
    title: String,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissText: String,
    confirmText: String
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        RoundedParkGreenBox(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                Text(text = title, color = Color.White, style = Typography.titleLarge)
                Text(
                    text = content,
                    color = Color.White,
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    RoundedParkGreenButton(
                        onClick = onDismiss,
                        buttonText = dismissText,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RoundedParkGreenButton(
                        onClick = onConfirm,
                        buttonText = confirmText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TwoButtonDialogPreview() {
    SimullimTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            TwoButtonDialog("title", "content", {}, {}, "취소", "확인")
        }
    }
}