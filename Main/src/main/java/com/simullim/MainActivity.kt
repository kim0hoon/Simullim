package com.simullim

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.simullim.R
import com.simullim.common.TwoButtonDialog
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.main.MainScreen
import com.simullim.start.StartScreen

class MainActivity : FragmentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var showQuitDialog by remember { mutableStateOf(false) }
            SimullimTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = DarkGrey)
                )
                NavHost(
                    navController = navController, startDestination = Page.MAIN.name,
                    modifier = Modifier
                ) {
                    composable(route = Page.MAIN.name) {
                        MainScreen(
                            onClickStart = { navController.navigate(Page.START.name) },
                            onClickQuit = { showQuitDialog = true })
                    }

                    composable(route = Page.START.name) {
                        StartScreen()
                    }
                }
                if (showQuitDialog) {
                    QuitDialog(onDismiss = { showQuitDialog = false }, onConfirm = { finish() })
                }
            }
        }
    }

    @Composable
    private fun QuitDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
        TwoButtonDialog(
            title = stringResource(R.string.quit_dialog_title),
            content = stringResource(R.string.quit_dialog_content),
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}
