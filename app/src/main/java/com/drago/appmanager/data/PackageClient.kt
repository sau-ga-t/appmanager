package com.drago.appmanager.data

import InstalledApp
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.File

class PackageClient(private val packageManager: PackageManager):PackageService {
    override fun getInstalledApps(): List<InstalledApp> {
        val appList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        return  appList.filter { it.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) == 1  }.map {
            val packageName: String = it.packageName
            val appName: String = it.applicationInfo.loadLabel(packageManager).toString()
            val appIcon: Drawable = it.applicationInfo.loadIcon(packageManager)
            val version: String = it.versionName
            val appSize:Long = getPackageSize(it)
            InstalledApp(appName, packageName, appIcon, version,appSize)
        }.sortedBy { it.appName }
    }
    fun getPackageSize(packageInfo: PackageInfo):Long{
        val appInfo = packageInfo.applicationInfo
        val apkPath = appInfo.sourceDir
        val apkFile = File(apkPath)
        return apkFile.length()
    }

    override fun getPackageDetails(packageName: String): String {
        val permissions = getPackagePermissions(packageName)
        if (!permissions.isEmpty()){
            permissions.forEach {
                Log.d("PERMISSION_ALL", "getPermission: $it")
            }
            return permissions.first()
        }else{
            return "No Permissions required"
        }
    }

    private fun getPackagePermissions(packageName: String): List<String> {
        val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val permissions = mutableListOf<String>()
        val requestedPermissions = packageInfo.requestedPermissions
        if (requestedPermissions != null) {
            for (permission in requestedPermissions) {
                try {
                    val permissionInfo = packageManager.getPermissionInfo(permission, 0)
                    permissions.add(permissionInfo.loadLabel(packageManager).toString())
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
        return permissions
    }

}