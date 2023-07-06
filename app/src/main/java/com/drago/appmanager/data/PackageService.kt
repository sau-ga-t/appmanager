package com.drago.appmanager.data

import InstalledApp
import android.content.pm.PackageInfo
import com.drago.appmanager.models.PackageDetails

interface PackageService {
    fun getInstalledApps(): List<InstalledApp>
    fun getPackageDetails(packageName:String): PackageDetails
}