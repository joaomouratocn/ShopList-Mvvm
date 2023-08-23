package br.com.devjmcn.shoplist.view.editShopListView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.DialogEditItemBinding
import br.com.devjmcn.shoplist.databinding.DialogNewShopListBinding
import br.com.devjmcn.shoplist.databinding.FragmentEditShopListBinding
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.util.extensions.createDialog
import br.com.devjmcn.shoplist.util.extensions.upFirstChar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class FragmentEditShopList : Fragment() {
    private val args: FragmentEditShopListArgs by navArgs()

    private val navController by lazy { findNavController() }

    private val binding by
    lazy { FragmentEditShopListBinding.inflate(layoutInflater) }

    private val viewModelEditShopList: ViewModelEditShopList
            by viewModel(qualifier = named("EDIT_SHOP_LIST_VIEW_MODEL")) { parametersOf(args.shopId) }

    private val editShopListAdapter by lazy {
        AdapterEditShopList(object : AdapterEditShopList.OnButtonClickEvent {

            override fun editItemClickEvent(itemShopListModel: ItemShopListModel) {
                showDialogEditItem(itemShopListModel)
            }

            override fun deleteItemClickEvent(itemShopListModel: ItemShopListModel) {
                showDialogConfirmDeleteItem(itemShopListModel)
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
        binding.txvNoItems.visibility = GONE
        configureToolbarMenu()

        binding.recycleEditShopList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = editShopListAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelEditShopList.selectedShopListWithItems.collect { selectedShopListWithItems ->
                    selectedShopListWithItems?.let {
                        (activity as AppCompatActivity).supportActionBar?.title = it.shopName
                        if (selectedShopListWithItems.listItems.isEmpty()) {
                            binding.txvNoItems.visibility = VISIBLE
                            editShopListAdapter.submitList(emptyList())
                        } else {
                            binding.txvNoItems.visibility = GONE
                            editShopListAdapter.submitList(selectedShopListWithItems.listItems)
                        }
                    }
                }
            }
        }
    }

    private fun configureToolbarMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_view_edit_shop_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_add_product -> {
                        goToAddProduct()
                        true
                    }

                    R.id.item_rename_shop_list -> {
                        showEditNameShopListDialog()
                        true
                    }

                    R.id.item_delete_shop_list -> {
                        viewModelEditShopList.selectedShopListWithItems.value?.let { shopListWithItemsModel ->
                            showDeleteListDialog(shopListWithItemsModel)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeleteListDialog(shopListWithItemsModel: ShopListWithItemsModel) =
        createDialog(
            title = getString(R.string.str_delete), message = getString(
                R.string.str_really_want_delete,
                shopListWithItemsModel.shopName
            ),
            positiveButtonText = getString(R.string.str_delete),
            negativeButtonText = getString(R.string.str_cancel)
        ) {
            lifecycleScope.launch {
                viewModelEditShopList.deleteShopList()
                navController.popBackStack()
            }
        }.show()

    private fun goToAddProduct() {
        val directions = FragmentEditShopListDirections
            .actionEditShopListFragmentToProductsFragment(args.shopId)
        navController.navigate(directions)
    }

    private fun showDialogConfirmDeleteItem(itemShopListModel: ItemShopListModel) {
        createDialog(
            title = getString(R.string.str_delete), message = getString(
                R.string.str_really_want_delete,
                itemShopListModel.prodName
            ),
            positiveButtonText = getString(R.string.str_delete),
            negativeButtonText = getString(R.string.str_cancel)
        ) {
            lifecycleScope.launch {
                viewModelEditShopList.deleteItem(itemShopListModel)
            }
        }.show()
    }

    private fun showEditNameShopListDialog() {
        val dialogView = DialogNewShopListBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)
        with(dialogView) {
            viewModelEditShopList.selectedShopListWithItems.value?.let {
                edtNewShopList.setText(it.shopName)
            }
            btnCancel.setOnClickListener { dialog.dismiss() }
            btnSave.setOnClickListener {
                val newShopName = edtNewShopList.text.toString().upFirstChar()

                if (viewModelEditShopList.isNameValid(newShopName)) {
                    lifecycleScope.launch {
                        viewModelEditShopList.updateShopList(newShopName)
                        dialog.dismiss()
                    }
                } else {
                    tilNameShopList.error = getString(R.string.str_invalid_field)
                }
            }
        }
        dialog.show()
    }

    private fun showDialogEditItem(itemShopListModel: ItemShopListModel) {
        val bindingDialog = DialogEditItemBinding.inflate(layoutInflater)
        val dialog = createDialog(bindingDialog)

        with(bindingDialog) {
            txvProdName.text = itemShopListModel.prodName
            edtAmountItem.setText(itemShopListModel.amount.toString())
            spnType.setSelection(itemShopListModel.typeIndex)
            edtDescription.setText(itemShopListModel.description)

            btnCancel.setOnClickListener { dialog.dismiss() }

            btnSave.setOnClickListener {
                if (viewModelEditShopList.isInvalidAmount(edtAmountItem.text.toString()))
                    tilAmountItem.error = getString(R.string.str_invalid_field)
                else {
                    val editedItem = itemShopListModel.copy(
                        amount = edtAmountItem.text.toString().toInt(),
                        typeIndex = spnType.selectedItemPosition,
                        description = edtDescription.text.toString().upFirstChar()
                    )
                    lifecycleScope.launch {
                        viewModelEditShopList.saveItem(editedItem)
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }
}