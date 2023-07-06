package com.drago.appmanager.ui.adapters

import InstalledApp
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drago.appmanager.R
import com.drago.appmanager.databinding.ItemAppBinding
import com.drago.appmanager.viewmodels.AppsViewModel


class AppListAdapter(
    private val viewModel: AppsViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val selectedIcon:Drawable,
    private val context: Context,
    private val navController: NavController
) : ListAdapter<InstalledApp, AppListAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        holder.itemView.isActivated = viewModel.getSelectedItems().value!!.contains(position)

        holder.bind(app)
    }

    inner class AppViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener{

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(app: InstalledApp) {
            binding.appName.text= app.appName
            binding.packageName.text = app.packageName //getFormattedSize(app.appSize)
            binding.appName.setTypeface(null, Typeface.BOLD)

            itemView.isActivated = viewModel.getSelectedItems().value!!.contains(adapterPosition)
            val isItemSelected = viewModel.getSelectedItems().value!!.contains(adapterPosition)
            itemView.isActivated = isItemSelected

            val viewIcon:Drawable = if (isItemSelected) selectedIcon else app.appIcon
            binding.appIcon.setImageDrawable(viewIcon)

            val backgroundColorRes = if (isItemSelected) com.google.android.material.R.color.material_dynamic_primary95 else com.google.android.material.R.color.m3_sys_color_dynamic_light_background
            val backgroundColor = ContextCompat.getColor(itemView.context, backgroundColorRes)
            itemView.setBackgroundColor(backgroundColor)

        }

        override fun onClick(v: View?) {
            val bundle = Bundle()
            val app = getItem(adapterPosition)
            bundle.putStringArray("PACKAGE_NAME_CONSTANT", arrayOf(app.packageName,app.appName,app.appIcon.toString()))
            navController.navigate(R.id.action_FirstFragment_to_AppDetailsFragment, bundle)
            }

        override fun onLongClick(v: View?): Boolean {val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                viewModel.toggleItemSelection(position)
            }
            return true
        }


    }

    private class AppDiffCallback : DiffUtil.ItemCallback<InstalledApp>() {
        override fun areItemsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: InstalledApp, newItem: InstalledApp): Boolean {
            return oldItem == newItem
        }
    }
    fun setData(installedApps: List<InstalledApp>) {
        submitList(installedApps)
    }
    fun getFormattedSize(appSize:Long): String{
        return android.text.format.Formatter.formatFileSize(context, appSize)
    }
    init {
        viewModel.getSelectedItems().observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }
}
