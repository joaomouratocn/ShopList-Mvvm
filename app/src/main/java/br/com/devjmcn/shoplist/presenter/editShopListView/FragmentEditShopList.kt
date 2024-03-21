package br.com.devjmcn.shoplist.presenter.editShopListView

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
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
import br.com.devjmcn.shoplist.databinding.DialogConfirmDeleteBinding
import br.com.devjmcn.shoplist.databinding.DialogEditItemBinding
import br.com.devjmcn.shoplist.databinding.DialogNewShopListBinding
import br.com.devjmcn.shoplist.databinding.FragmentEditShopListBinding
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
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

        binding.fabAddItem.setOnClickListener { goToAddProduct() }
        binding.linearClick.setOnClickListener { goToAddProduct() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelEditShopList.selectedShopListWithItems.collect { selectedShopListWithItems ->
                    selectedShopListWithItems?.let {
                        (activity as AppCompatActivity).supportActionBar?.title = it.shopName
                        if (selectedShopListWithItems.listItems.isEmpty()) {
                            updateView(VISIBLE)
                            editShopListAdapter.submitList(emptyList())
                        } else {
                            updateView()
                            editShopListAdapter.submitList(selectedShopListWithItems.listItems)
                        }
                    }
                }
            }
        }
    }

    private fun updateView(visibility: Int = GONE) {
        binding.txvNoItems.visibility = visibility
        binding.imgNoItems.visibility = visibility
    }

    private fun configureToolbarMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_view_edit_shop_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_rename_shop_list -> {
                        showEditNameShopListDialog()
                        true
                    }

                    R.id.item_delete_shop_list -> {
                        viewModelEditShopList.selectedShopListWithItems.value?.let {shopListWithItemsModel ->
                            val shopListModel = ShopListModel(
                                shopId = shopListWithItemsModel.shopId,
                                shopName = shopListWithItemsModel.shopName,
                                shopDate = shopListWithItemsModel.shopDate
                            )
                            showDeleteListDialog(shopListModel)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeleteListDialog(shopListModel: ShopListModel) {
        val dialogView = DialogConfirmDeleteBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)

        with(dialogView) {
            txtTitle.text = getString(R.string.str_delete)
            txtMsgConfirmDelete.text =
                getString(
                    R.string.str_really_want_delete,
                    viewModelEditShopList.selectedShopListWithItems.value?.shopName
                )
            btnCancel.setOnClickListener { dialog.dismiss() }
            btnConfirm.setOnClickListener {
                lifecycleScope.launch {
                    viewModelEditShopList.deleteShopList(shopListModel)
                    navController.popBackStack()
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun goToAddProduct() {
        val directions = FragmentEditShopListDirections
            .actionEditShopListFragmentToProductsFragment(args.shopId)
        navController.navigate(directions)
    }

    private fun showDialogConfirmDeleteItem(itemShopListModel: ItemShopListModel) {
        val dialogView = DialogConfirmDeleteBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)

        with(dialogView) {
            txtTitle.text = getString(R.string.str_delete)
            txtMsgConfirmDelete.text =
                getString(R.string.str_really_want_delete, itemShopListModel.prodName)

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnConfirm.setOnClickListener {
                lifecycleScope.launch {
                    viewModelEditShopList.deleteItem(itemShopListModel)
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
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
                        viewModelEditShopList.renameShopList(newShopName)
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

    private fun showDialogEditItem(itemShopListModel: ItemShopListModel) {
        val dialogView = DialogEditItemBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)

        with(dialogView) {
            txvProdName.text = itemShopListModel.prodName
            edtAmountItem.setText(itemShopListModel.amount.toString())
            spnType.setSelection(itemShopListModel.typeIndex)
            edtDescription.setText(itemShopListModel.description)

            btnCancel.setOnClickListener { dialog.dismiss() }

            btnSave.setOnClickListener {
                if (viewModelEditShopList.isInvalidAmount(edtAmountItem.text.toString()))
                    Toast.makeText(requireContext(), getText(R.string.str_field_amount_invalid), Toast.LENGTH_SHORT).show()
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
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
}