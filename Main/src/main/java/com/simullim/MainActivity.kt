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
import com.simullim.common.PlaylistModel
import com.simullim.compose.TwoButtonDialog
import com.simullim.compose.ui.theme.DarkGrey
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.debugtest.DebugTestScreen
import com.simullim.main.MainScreen
import com.simullim.playinfo.PlayInfoScreen
import com.simullim.playinfo.PlayInfoViewModel
import com.simullim.playinfo.model.PlayInfoModel
import com.simullim.playsetting.PlaySettingScreen
import com.simullim.playsetting.PlaySettingViewModel
import com.simullim.service.PlayServiceManager
import com.simullim.service.model.PlayServiceInputModel
import timber.log.Timber

internal class MainActivity : FragmentActivity(), MainEventReceiver {
    private val mainViewModel by viewModels<MainViewModel>()
    private val playSettingViewModel by viewModels<PlaySettingViewModel>()
    private val playInfoViewModel by viewModels<PlayInfoViewModel>()
    private val playlistResult = registerForActivityResult(MusicPickerResultContract()) {
        val playlistModel = it?.map { musicModel ->
            PlaylistModel.Playlist(
                title = musicModel.title,
                durationMills = musicModel.durationMillis ?: 0,
                url = musicModel.uriString
            )
        } ?: emptyList()
        playSettingViewModel.setPlaylist(playlistModel)
    }
    private val playServiceManager = PlayServiceManager(
        lifecycleOwner = this,
        context = this,
        onGpsDataEmitted = {
            Timber.d("gpsData emitted : $it")
            playInfoViewModel.setPlayInfo(
                PlayInfoModel(
                    timeSec = millsToSec(it.totalTimeMills).toInt(),
                    totalDistanceMeter = it.totalDistanceMeter.toInt(),
                    averageVelocity = meterPerSecToSecPerKiloMeter(it.averageVelocity).toInt(),
                    velocity = meterPerSecToSecPerKiloMeter(it.currentVelocity).toInt()
                )
            )
        })
    private val locationPermissionManager = PermissionManager(activity = this)


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
                            onClickStart = { navController.navigate(Page.PLAY_SETTING.name) },
                            onClickQuit = { showQuitDialog = true },
                            onClickDebugTest = { navController.navigate(Page.DEBUG_TEST.name) })
                    }

                    composable(route = Page.PLAY_SETTING.name) {
                        PlaySettingScreen(
                            mainViewModel = mainViewModel,
                            playSettingViewModel = playSettingViewModel,
                            onClickStart = { playServiceModel ->
                                mainViewModel.sendMainEvent(
                                    MainEvent.Play(
                                        playServiceInputModel = playServiceModel,
                                        onGranted = {
                                            navController.navigate(
                                                Page.PLAY_INFO.name
                                            )
                                        })
                                )
                            },
                            onClickBack = navController::popBackStack
                        )
                    }
                    composable(route = Page.PLAY_INFO.name) {
                        PlayInfoScreen(
                            playInfoViewModel = playInfoViewModel,
                            onClickBack = navController::popBackStack
                        )
                    }
                    composable(route = Page.DEBUG_TEST.name) {
                        DebugTestScreen(mainViewModel = mainViewModel)
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

    override fun onPlay(playServiceInputModel: PlayServiceInputModel, onGranted: (() -> Unit)?) {
        locationPermissionManager.executeWithCheckPermissions(
            onGranted = {
                onGranted?.invoke()
                playServiceManager.startService()
                playServiceManager.play(playServiceInputModel = playServiceInputModel)
            },
            onShouldShowRationale = {
                //TODO 안내 추가
            },
            onDenied = {
                //TODO 거절한 케이스
            },
            permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION)
        )
    }

    override fun onSetPlaylist() {
        playlistResult.launch(Unit)
    }

    override fun onPlayPause() {
        playServiceManager.pause()
    }

    override fun onPlayStop() {
        playServiceManager.stop()
    }

    override fun onPlayResume() {
        playServiceManager.resume()
    }
}
