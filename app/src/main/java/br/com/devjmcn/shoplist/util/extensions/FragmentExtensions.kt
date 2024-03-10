package br.com.devjmcn.shoplist.util.extensions

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.devjmcn.shoplist.R

fun Fragment.createDialog(
    viewBinding: ViewBinding? = null,
    title:String? = null,
    message:String? = null,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    actionPositiveButton: (() -> Unit)? = null
):AlertDialog{
    return if(viewBinding != null){
        AlertDialog.Builder(requireContext())
            .setView(viewBinding.root)
            .create()
    }else{
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText, null)
            .setPositiveButton(positiveButtonText) { _, _ -> actionPositiveButton?.invoke() }
            .create()
    }
}