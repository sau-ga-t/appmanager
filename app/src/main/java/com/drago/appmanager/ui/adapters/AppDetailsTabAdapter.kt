package com.drago.appmanager.ui.adapters

import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.drago.appmanager.TabContentFragment

class AppDetailsTabAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val packageName:String) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if (position==0){
            TabContentFragment.newInstance(packageName)
        }else
            TabContentFragment.newInstance(position + 1)
    }
}
