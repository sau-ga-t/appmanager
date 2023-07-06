package com.drago.appmanager.repository

import InstalledApp
import com.drago.appmanager.data.PackageService
import com.drago.appmanager.models.PackageDetails

class PackageRepository(private val packageService: PackageService) {
    fun getInstalledPackages(): List<InstalledApp> {
        return packageService.getInstalledApps()
    }
    fun getPackageDetails(packageName:String): PackageDetails {
        return packageService.getPackageDetails(packageName)
    }
}
