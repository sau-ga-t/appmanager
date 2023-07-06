package com.drago.appmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drago.appmanager.databinding.FragmentAppDetailsBinding
import com.drago.appmanager.models.PackageDetails
import com.drago.appmanager.ui.adapters.AppDetailsTabAdapter
import com.drago.appmanager.viewmodels.AppDetailsViewModel
import com.drago.appmanager.viewmodels.AppsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class AppDetailsFragment : Fragment() {

    private var _binding: FragmentAppDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var packageName:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        packageName = requireArguments().getStringArray("PACKAGE_NAME_CONSTANT")!![0]
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar!!.title = requireArguments().getStringArray("PACKAGE_NAME_CONSTANT")!![1]
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragmentAdapter = AppDetailsTabAdapter(childFragmentManager, lifecycle, packageName)
        binding.viewPager.adapter = fragmentAdapter
        binding.viewPager.isUserInputEnabled = true
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position==0){
                tab.text = "Permissions"
            }else if (position==1){
                tab.text="Activities"
            }else{
                tab.text="Miscellaneous"
            }
        }.attach()
    }
}
