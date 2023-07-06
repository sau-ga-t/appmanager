package com.drago.appmanager.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.drago.appmanager.data.PackageClient
import com.drago.appmanager.repository.PackageRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppDetailsViewModel(application: Application):AndroidViewModel(application) {
    val permissions = MutableLiveData<List<String>>()
    lateinit var packageRepository: PackageRepository
    init {
        val packageManager = application.packageManager
        val packageService = PackageClient(packageManager)
        packageRepository = PackageRepository(packageService)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchPackagePermissions(packageName:String){
        GlobalScope.launch {
            val packageDetails = packageRepository.getPackageDetails(packageName)
            withContext(Dispatchers.Main){
                permissions.value = packageDetails.permissions
            }
        }
    }
}