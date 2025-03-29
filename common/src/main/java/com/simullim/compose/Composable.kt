package com.simullim.compose

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.common.R
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.Grey81
import com.simullim.compose.ui.theme.ParkGreen
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.compose.ui.theme.Typography

@Composable
fun RoundedParkGreenButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    innerPaddingHorizontal: Dp = 0.dp,
    innerPaddingVertical: Dp = 0.dp
) {
    val color = if (isEnabled) ParkGreen else Grey81
    OutlinedButton(
        onClick = onClick, modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        enabled = isEnabled,
        border = BorderStroke(2.dp, color),
    ) {
        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            color = color,
            modifier = Modifier.padding(
                horizontal = innerPaddingHorizontal,
                vertical = innerPaddingVertical
            )
        )
    }
}

@Composable
fun CheckableRoundedParkGreenButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    isChecked: Boolean = true,
    innerPaddingHorizontal: Dp = 0.dp,
    innerPaddingVertical: Dp = 0.dp
) {
    val color = if (isChecked) ParkGreen else Grey81
    OutlinedButton(
        onClick = onClick, modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, color),
    ) {
        Text(
            text = buttonText,
            textAlign = TextAlign.Center,
            color = color,
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
    Column(Modifier.background(Color.DarkGray)) {
        RoundedParkGreenButton({}, buttonText = "test")
        RoundedParkGreenButton({}, buttonText = "test", isEnabled = false)
    }
}

@Composable
fun RoundedParkGreenBox(
    width: Dp = 2.dp,
    radius: Dp = 10.dp,
    modifier: Modifier = Modifier,
    boxScope: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = width,
                color = ParkGreen,
                shape = RoundedCornerShape(radius)
            )
            .then(modifier)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonHeader(
    title: String,
    modifier: Modifier = Modifier,
    leftIcon: CommonHeaderIcon? = null,
    rightIcon: CommonHeaderIcon? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(dimensionResource(R.dimen.header_height))
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
        ) {
            leftIcon?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        painter = painterResource(it.drawableRes),
                        tint = { Color.White },
                        contentDescription = null
                    )
                }
            }
        }

        Text(
            text = title,
            style = Typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )

        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
        ) {
            rightIcon?.let {
                IconButton(onClick = it.onClick) {
                    Icon(
                        painter = painterResource(it.drawableRes),
                        tint = { Color.White },
                        contentDescription = null
                    )
                }
            }
        }
    }
}

data class CommonHeaderIcon(
    @DrawableRes val drawableRes: Int,
    val onClick: () -> Unit
)

@Composable
@Preview(showBackground = true)
fun CommonHeaderPreview() {
    Box(modifier = Modifier.background(DarkGrey)) {
        CommonHeader(
            "title 12312897391823791",
            leftIcon = CommonHeaderIcon(R.drawable.baseline_arrow_back_ios_new_24, {}),
            rightIcon = CommonHeaderIcon(R.drawable.baseline_arrow_back_ios_new_24, {})
        )
    }
}

@Composable
fun NumberTextField(
    value: Int,
    onValueChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Typography.bodyLarge,
    range: IntRange = Int.MIN_VALUE..Int.MAX_VALUE
) {
    RoundedParkGreenBox(modifier = modifier) {
        BasicTextField(
            value = value.toString(),
            onValueChange = {
                onValueChanged((it.toIntOrNull() ?: 0).coerceIn(range))
            },
            textStyle = textStyle.copy(color = Color.White, textAlign = TextAlign.Center),
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        )
    }
}

@Composable
@Preview
private fun NumberTextFieldPreview() {
    NumberTextField(value = 123, {}, modifier = Modifier.height(48.dp))
}