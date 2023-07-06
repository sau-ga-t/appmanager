package com.drago.appmanager.extensions
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

class PackageUninstaller(private val activity: Activity, private val appList:List<String>) {
    private var currentIndex = 0
    private var uninstallCallback: ((Boolean) -> Unit)? = null

    fun uninstallPackages(callback: (Boolean) -> Unit) {
        uninstallCallback = callback
        currentIndex = 0
        initiateUninstallation()
    }

    private fun initiateUninstallation() {
        if (currentIndex < appList.size) {
            val packageName = appList[currentIndex]
            val uninstallIntent = Intent(Intent.ACTION_UNINSTALL_PACKAGE).apply {
                data = Uri.parse("package:$packageName")
            }
            activity.startActivityForResult(uninstallIntent, UNINSTALL_REQUEST_CODE)
        } else {
            uninstallCallback?.invoke(true)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("PACKAGE_UNINSTALLATION", "handleActivityResult: "+"SUCCESS")
            } else {
                Log.d("PACKAGE_UNINSTALLATION", "handleActivityResult: "+"FAILED")
            }
            currentIndex++
            initiateUninstallation()
        }
    }

    companion object {
        private const val UNINSTALL_REQUEST_CODE = 1
    }
}
