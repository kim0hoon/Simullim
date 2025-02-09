package com.simullim

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal class PermissionManager(
    private val activity: ComponentActivity,
    private val onGranted: () -> Unit,
    private val onShouldShowRationale: (List<String>) -> Unit,
    private val onDenied: (List<String>) -> Unit,
    private val permissions: List<String>
) {
    private val requestPermissions =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val denied = it.filterValues { isGranted -> isGranted.not() }.keys
            if (it.values.all { isGranted -> isGranted }) onGranted() else onDenied(denied.toList())
        }

    fun executeWithCheckPermissions() {
        val needShowRationalePermissions = getNeedShowRationalePermissions()
        when {
            isAllGranted() -> onGranted()
            needShowRationalePermissions.isNotEmpty() -> onShouldShowRationale(
                needShowRationalePermissions
            )
            else -> requestPermissions.launch(permissions.toTypedArray())
        }
    }

    private fun isAllGranted() = permissions.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getNeedShowRationalePermissions() = permissions.filter {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
    }
}
