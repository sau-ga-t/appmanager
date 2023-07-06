package com.drago.appmanager.data

import InstalledApp
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import com.drago.appmanager.models.PackageDetails
import java.io.File
import java.lang.Exception

class PackageClient(private val packageManager: PackageManager):PackageService {
    override fun getInstalledApps(): List<InstalledApp> {
        val appList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        return  appList.filter { it.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) == 0  }.map {
            val packageName: String = it.packageName
            val appName: String = it.applicationInfo.loadLabel(packageManager).toString()
            val appIcon: Drawable = it.applicationInfo.loadIcon(packageManager)
            val version: String = it.versionName
            val appSize:Long = getPackageSize(it)
            InstalledApp(appName, packageName, appIcon, version,appSize)
        }.sortedBy { it.appName }
    }
    private fun getPackageSize(packageInfo: PackageInfo):Long{
        val appInfo = packageInfo.applicationInfo
        val apkPath = appInfo.sourceDir
        val apkFile = File(apkPath)
        return apkFile.length()
    }

    override fun getPackageDetails(packageName: String): PackageDetails {
        val permissions = getPackagePermissions(packageName)
        val permList = mutableListOf<String>()
            permissions.forEach {
                permList.add(it)
            }
         return PackageDetails(permList)
    }

    private fun getPackagePermissions(packageName: String): List<String> {
        val permissions = mutableListOf<String>()

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
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
        }catch (e:Exception){
            e.message?.let { permissions.add(it) }
        }
        return permissions


    }

}