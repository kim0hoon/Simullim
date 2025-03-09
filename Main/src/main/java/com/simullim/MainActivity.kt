package com.simullim

import android.Manifest
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
import com.example.music_picker.MusicPickerResultContract
import com.example.simullim.R
import com.simullim.compose.TwoButtonDialog
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.main.MainScreen
import com.simullim.service.PlayServiceManager
import com.simullim.start.StartScreen
import timber.log.Timber

internal class MainActivity : FragmentActivity(), MainEventReceiver {
    private val mainViewModel by viewModels<MainViewModel>()
    private val playlistResult = registerForActivityResult(MusicPickerResultContract()) {
        //TODO playitem
        Timber.d("playlist : $it")
    }
    private val playServiceManager = PlayServiceManager(
        lifecycleOwner = this,
        context = this,
        onGpsDataEmitted = {
            Timber.d("data emitted : $it")
        },
        onErrorEvent = {})
    private val locationPermissionManager =
        PermissionManager(
            activity = this,
            onGranted = {
                playServiceManager.startService()
            },
            onShouldShowRationale = {
                Timber.d("onShouldShow")
            },
            onDenied = {
                Timber.d("onDenied : $it")
            },
            permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFlow()
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
                        StartScreen(mainViewModel = mainViewModel)
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
            onConfirm = onConfirm,
            dismissText = stringResource(R.string.cancel),
            confirmText = stringResource(R.string.confirm)
        )
    }

    private fun initFlow() {
        mainViewModel.mainEventFlow.collectOnLifecycle(lifecycleOwner = this) {
            handleEvent(it)
        }
    }

    override fun onPlay() {
        locationPermissionManager.executeWithCheckPermissions()
    }

    override fun onSetPlaylist() {
        playlistResult.launch(Unit)
    }
}
