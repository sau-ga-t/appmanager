package com.drago.appmanager.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class UninstallAppContract : ActivityResultContract<String, Boolean>() {
    override fun createIntent(context: Context, packageName: String): Intent {
        val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE)
        uninstallIntent.data = Uri.parse("package:$packageName")
        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        return uninstallIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
