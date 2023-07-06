package com.drago.appmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.drago.appmanager.viewmodels.AppDetailsViewModel


class TabContentFragment : Fragment() {

    private lateinit var textView: TextView
    private lateinit var permissionListView: ListView
    private lateinit var appDetailsViewModel: AppDetailsViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tab_content, container, false)
        textView = view.findViewById(R.id.tab_number)
        permissionListView = view.findViewById(R.id.permissionList)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDetailsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(AppDetailsViewModel::class.java)
        val tabNumber = arguments?.getInt(ARG_TAB_NUMBER, 0) ?: 0
        val packageName = arguments?.getString(ARG_TAB_LIST, "") ?: ""

        appDetailsViewModel.permissions.observe(viewLifecycleOwner){
            val arrayAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, it)
            permissionListView.adapter = arrayAdapter
        }
        textView.text = "Tab $tabNumber Content"

        appDetailsViewModel.fetchPackagePermissions(packageName)
    }

    companion object {
        private const val ARG_TAB_NUMBER = "tab_number"
        private const val ARG_TAB_LIST= "permission_list"

        fun newInstance(tabNumber: Int): TabContentFragment {
            val fragment = TabContentFragment()
            val args = Bundle()
            args.putInt(ARG_TAB_NUMBER, tabNumber)
            fragment.arguments = args
            return fragment
        }
        fun newInstance(packageName: String): TabContentFragment {
            val fragment = TabContentFragment()
            val args = Bundle()
            args.putString(ARG_TAB_LIST,packageName)
            fragment.arguments = args
            return fragment
        }
    }
}
