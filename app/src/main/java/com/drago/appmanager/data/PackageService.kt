package com.drago.appmanager.data

import InstalledApp
import android.content.pm.PackageInfo

interface PackageService {
    fun getInstalledApps(): List<InstalledApp>
    fun getPackageDetails(packageName:String): String
}