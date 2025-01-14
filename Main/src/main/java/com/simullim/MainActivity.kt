package com.simullim

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import com.simullim.compose.ui.theme.SimullimTheme
import com.simullim.main.MainFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimullimTheme {
                AndroidFragment<MainFragment>()
            }
        }
    }
}
