package com.simullim

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

internal class PermissionManager(
    private val activity: ComponentActivity,
) {
    private val requestPermissions =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val denied = it.filterValues { isGranted -> isGranted.not() }.keys
            if (it.values.all { isGranted -> isGranted }) _onGranted?.invoke() else _onDenied?.invoke(
                denied.toList()
            )
        }

    private var _onGranted: (() -> Unit)? = null
    private var _onShouldShowRationale: ((List<String>) -> Unit)? = null
    private var _onDenied: ((List<String>) -> Unit)? = null

    fun executeWithCheckPermissions(
        onGranted: (() -> Unit)? = null,
        onShouldShowRationale: ((List<String>) -> Unit)? = null,
        onDenied: ((List<String>) -> Unit)? = null,
        permissions: List<String> = emptyList()
    ) {
        _onGranted = onGranted
        _onShouldShowRationale = onShouldShowRationale
        _onDenied = onDenied

        val needShowRationalePermissions = getNeedShowRationalePermissions(permissions)
        when {
            isAllGranted(permissions) -> onGranted?.invoke()
            needShowRationalePermissions.isNotEmpty() -> onShouldShowRationale?.invoke(
                needShowRationalePermissions
            )
            else -> requestPermissions.launch(permissions.toTypedArray())
        }
    }

    private fun isAllGranted(permissions: List<String>) = permissions.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getNeedShowRationalePermissions(permissions: List<String>) = permissions.filter {
        ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
    }
}
