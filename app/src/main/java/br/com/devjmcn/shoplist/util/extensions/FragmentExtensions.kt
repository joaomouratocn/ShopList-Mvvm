package br.com.devjmcn.shoplist.util.extensions

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun Fragment.createDialog(viewBinding: ViewBinding):
        AlertDialog = AlertDialog.Builder(requireContext())
        .setView(viewBinding.root)
        .create()