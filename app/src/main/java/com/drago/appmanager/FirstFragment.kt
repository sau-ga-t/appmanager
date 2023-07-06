package com.drago.appmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drago.appmanager.databinding.FragmentFirstBinding
import com.drago.appmanager.extensions.PackageUninstaller
import com.drago.appmanager.extensions.UninstallAppContract
import com.drago.appmanager.ui.adapters.AppListAdapter
import com.drago.appmanager.viewmodels.AppsViewModel


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var appsViewModel: AppsViewModel
    private lateinit var appListAdapter: AppListAdapter
    private var indexOfUninstallingApp = 0
    private var uninstallingApps: List<String> = mutableListOf()

    private val uninstallLauncher = registerForActivityResult(
        UninstallAppContract()
    ) {
        if (indexOfUninstallingApp>=uninstallingApps.size-1){
            Toast.makeText(context, "${uninstallingApps.size} Apps Uninstalled", Toast.LENGTH_LONG)
            indexOfUninstallingApp = 0
            appsViewModel.clearSelection()
            appsViewModel.getInstalledApps()
            appListAdapter.notifyDataSetChanged()
        }else {
            uninstallApp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() {
        appListAdapter = AppListAdapter(appsViewModel, this, AppCompatResources.getDrawable(requireContext(), R.drawable.check_circle_50)!!, requireContext())
        binding.appListView.apply {
            adapter = appListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupViewModel() {
        val applicationContext = requireActivity().application
        appsViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(applicationContext)).get(AppsViewModel::class.java)
        appsViewModel.getInstalledApps().observe(viewLifecycleOwner) {
            appListAdapter.setData(it)
        }
        appsViewModel.selectedItems.observe(viewLifecycleOwner){
            if(!it.isEmpty()){
                binding.fab.isVisible
            }else{
                binding.fab.isGone
            }
        }

        binding.fab.setOnClickListener {
           uninstallingApps = appsViewModel.getSelectedPackages()
            uninstallApp()
        }
    }

    private fun uninstallApp(){
        uninstallLauncher.launch(uninstallingApps.get(indexOfUninstallingApp))
        indexOfUninstallingApp++

    }
    private fun fetchInstalledApps() {
        appsViewModel.fetchInstalledApps()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        fetchInstalledApps()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}