package com.simullim.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.Typography

data class NumberTextInputDialogParam(
    val initValue: Int = 0,
    val toResult: (Int) -> Int,
    val text: String? = null
)

@Composable
fun NumberTextInputDialog(
    title: String,
    vararg params: NumberTextInputDialogParam,
    confirmText: String,
    onConfirm: (Int) -> Unit,
    cancelText: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val result =
        remember { mutableStateListOf(elements = params.map { it.initValue }.toTypedArray()) }
    Dialog(onDismissRequest = onCancel) {
        RoundedParkGreenBox(modifier = modifier.background(color = DarkGrey)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    params.forEachIndexed { idx, param ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NumberTextField(
                                value = param.initValue,
                                onValueChanged = { result[idx] = param.toResult(it) },
                                modifier = Modifier.weight(1f)
                            )
                            param.text?.let { text ->
                                Text(
                                    text = text,
                                    color = Color.White,
                                    style = Typography.labelSmall,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier
                        .padding(top = 24.dp)
                ) {
                    RoundedParkGreenButton(
                        onClick = onCancel,
                        buttonText = cancelText,
                        modifier = Modifier.weight(1f)
                    )
                    RoundedParkGreenButton(onClick = {
                        val ret = result.foldIndexed(0) { idx, acc, n ->
                            acc + params[idx].toResult(n)
                        }
                        onConfirm(ret)
                    }, buttonText = confirmText, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
@Preview
private fun NumberTextInputDialogPreview() {
    NumberTextInputDialog(title = "titletest", params = List(3) {
        NumberTextInputDialogParam(
            initValue = it + 1,
            { 0 },
            "test$it"
        )
    }.toTypedArray(), "확인", {}, "취소", {})
}