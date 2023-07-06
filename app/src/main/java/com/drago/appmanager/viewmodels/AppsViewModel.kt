package com.drago.appmanager.viewmodels

import InstalledApp
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.drago.appmanager.R
import com.drago.appmanager.data.PackageClient
import com.drago.appmanager.repository.PackageRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsViewModel(application: Application) : AndroidViewModel(application) {
    private val installedApps = MutableLiveData<List<InstalledApp>>()
    private val selectedItems  = MutableLiveData<MutableList<Int>>()
    var packageRepository: PackageRepository
    private val packageStorage:MutableLiveData<String> = MutableLiveData()

    init {
        selectedItems.value = mutableListOf()
        val packageManager = application.packageManager
        val packageService = PackageClient(packageManager)
        packageRepository = PackageRepository(packageService)
    }
    fun getInstalledApps(): LiveData<List<InstalledApp>> {
        return installedApps
    }
    fun toggleItemSelection(position: Int) {
        val selectedPositions = selectedItems.value ?: mutableListOf()
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
        } else {
            selectedPositions.add(position)
        }
        selectedItems.value = selectedPositions
    }

    fun getSelectedItems(): LiveData<List<Int>> {
        return selectedItems as LiveData<List<Int>>
    }

    fun clearSelection() {
        selectedItems.value?.clear()
    }
    fun getSelectedPackages(): List<String>{
        val installedAppsState:List<InstalledApp> = getInstalledApps().value!!
        val selectedPackages = mutableListOf<String>()
        selectedItems.value!!.forEach {
            selectedPackages.add(installedAppsState.get(it).packageName)
        }
        return selectedPackages
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun fetchInstalledApps() {
        GlobalScope.launch(Dispatchers.IO) {
            val apps = packageRepository.getInstalledPackages()
            withContext(Dispatchers.Main) {
                installedApps.value = apps
            }
        }
    }
}