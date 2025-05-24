package br.com.devjmcn.shoplist.presenter.allShopListView

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.DialogConfirmDeleteBinding
import br.com.devjmcn.shoplist.databinding.DialogNewShopListBinding
import br.com.devjmcn.shoplist.databinding.FragmentAllShopListBinding
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.util.extensions.createDialog
import br.com.devjmcn.shoplist.util.extensions.upFirstChar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class FragmentAllShopList : Fragment() {
    private val navController by lazy {
        findNavController()
    }

    private val binding by lazy {
        FragmentAllShopListBinding.inflate(layoutInflater)
    }

    private val viewModelAllShopList: ViewModelAllShopList
            by viewModel(qualifier = named("ALL_SHOP_LIST_VIEW_MODEL"))

    private val adapterAllShopList by lazy {
        AdapterAllShopList(object : AdapterAllShopList.AllShopListAdapterOnClickEvent {
            override fun editShopList(shopId: Long) {
                goToEditShopList(shopId)
            }

            override fun openBuyShopList(shopId: Long) {
                val directions =
                    FragmentAllShopListDirections.actionFragmentAllShopListToFragmentBuyShopList(
                        shopId
                    )
                navController.navigate(directions)
            }

            override fun deleteShopList(shopListModel: ShopListModel) {
                showConfirmDeleteDialog(shopListModel)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initConfig()
    }

    private fun initConfig() {
        binding.apply {
            recycleAllShopList.adapter = adapterAllShopList
            efabNewShopList.setOnClickListener { showDialogNewShopList() }
            linearClick.setOnClickListener { showDialogNewShopList() }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelAllShopList.allShopList.collect { listShopListWithItems ->
                    when (listShopListWithItems) {
                        null -> {
                            showViews(progressLoadingVisibility = VISIBLE)
                        }

                        emptyList<ShopListWithItemsModel>() -> {
                            showViews(textNoListVisibility = VISIBLE, imgNoListImage = VISIBLE)
                            adapterAllShopList.submitList(emptyList())
                        }

                        else -> {
                            showViews()
                            adapterAllShopList.submitList(listShopListWithItems)
                        }
                    }
                }
            }
        }
    }

    private fun goToEditShopList(shopId: Long) {
        val directions =
            FragmentAllShopListDirections.actionFragmentAllShopListToEditShopListFragment(shopId = shopId)
        navController.navigate(directions)
    }

    private fun showConfirmDeleteDialog(shopListModel: ShopListModel) {
        val dialogView = DialogConfirmDeleteBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)
        with(dialogView){
            txtTitle.text = getString(R.string.str_delete)
            txtMsgConfirmDelete.text = getString(R.string.str_really_want_delete, shopListModel.shopName)

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnConfirm.setOnClickListener {
                lifecycleScope.launch { viewModelAllShopList.deleteShopList(shopListModel = shopListModel) }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDialogNewShopList() {
        val dialogView = DialogNewShopListBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)
        with(dialogView) {
            btnCancel.setOnClickListener { dialog.dismiss() }
            btnSave.setOnClickListener {
                val shopName = edtNewShopList.text.toString().upFirstChar()

                val isNameValid = viewModelAllShopList.isNameValid(shopName)
                if (isNameValid) {
                    lifecycleScope.launch {
                        val shopId = viewModelAllShopList.saveShopList(
                            ShopListModel(shopName = shopName)
                        )
                        goToEditShopList(shopId)
                        dialog.dismiss()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.str_invalid_field),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showViews(
        progressLoadingVisibility: Int = GONE,
        textNoListVisibility: Int = GONE,
        imgNoListImage: Int = GONE
    ) {
        binding.apply {
            pbrLoading.visibility = progressLoadingVisibility
            txvNoList.visibility = textNoListVisibility
            imgNoList.visibility = imgNoListImage
        }
    }
}