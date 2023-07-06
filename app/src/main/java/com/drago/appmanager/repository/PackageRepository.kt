package com.drago.appmanager.repository

import InstalledApp
import com.drago.appmanager.data.PackageService

class PackageRepository(private val packageService: PackageService) {
    fun getInstalledPackages(): List<InstalledApp> {
        return packageService.getInstalledApps()
    }
    fun getPackageDetails(packageName:String): String {
        return packageService.getPackageDetails(packageName)
    }
}
