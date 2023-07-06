package com.drago.appmanager.ui.adapters

import InstalledApp
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
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
    private val context: Context
) : ListAdapter<InstalledApp, AppListAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        holder.itemView.isActivated = viewModel.getSelectedItems().contains(position)

        holder.bind(app)
    }

    inner class AppViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }
        fun bind(app: InstalledApp) {
            binding.appName.text= app.appName
            binding.packageName.text = app.packageName //getFormattedSize(app.appSize)
            binding.appName.setTypeface(null, Typeface.BOLD)

            itemView.isActivated = viewModel.getSelectedItems().contains(adapterPosition)
            val isItemSelected = viewModel.getSelectedItems().contains(adapterPosition)
            itemView.isActivated = isItemSelected

            val viewIcon:Drawable = if (isItemSelected) selectedIcon else app.appIcon
            binding.appIcon.setImageDrawable(viewIcon)

            val backgroundColorRes = if (isItemSelected) com.google.android.material.R.color.material_dynamic_primary95 else com.google.android.material.R.color.m3_sys_color_dynamic_light_background
            val backgroundColor = ContextCompat.getColor(itemView.context, backgroundColorRes)
            itemView.setBackgroundColor(backgroundColor)

        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                viewModel.toggleItemSelection(position)
                viewModel.fetchAppDetails(position)
            }        }
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
        viewModel.selectedItems.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }
}
