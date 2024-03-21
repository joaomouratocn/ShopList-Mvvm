package br.com.devjmcn.shoplist.presenter.buyShopListView

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
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.DialogAddPriceBinding
import br.com.devjmcn.shoplist.databinding.DialogConfirmDeleteBinding
import br.com.devjmcn.shoplist.databinding.DialogNotifySumValuesBinding
import br.com.devjmcn.shoplist.databinding.FragmentBuyShopListBinding
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.util.TextWatcherNumberFormat
import br.com.devjmcn.shoplist.util.extensions.createDialog
import br.com.devjmcn.shoplist.util.extensions.toBigDecimalFormat
import br.com.devjmcn.shoplist.util.numberFormat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named


class FragmentBuyShopList : Fragment() {
    private val args: FragmentBuyShopListArgs by navArgs()

    val binding by lazy {
        FragmentBuyShopListBinding.inflate(layoutInflater)
    }

    private val adapterBuyShopList by lazy {
        AdapterBuyShopList(object : AdapterBuyShopList.OnBuyShopListOnClickEvent {
            override fun deleteItem(itemSelected: ItemShopListModel) {
                showDialogDeleteItem(itemSelected)
            }

            override fun checkItem(itemSelected: ItemShopListModel) {
                lifecycleScope.launch {
                    viewModelBuyShopList.getPreferencesStatus().first()?.let { preferences ->
                        if (preferences) {
                            showDialogLoadPrice(itemSelected)
                        } else {
                            viewModelBuyShopList.saveItem(itemSelected)
                        }
                    }
                }
            }

            override fun unCheckItem(itemSelected: ItemShopListModel) {
                lifecycleScope.launch {
                    viewModelBuyShopList.saveItem(itemSelected)
                }
            }
        })
    }

    private val viewModelBuyShopList: ViewModelBuyShopList
            by viewModel(qualifier = named("BUY_SHOP_VIEW_MODEL")) { parametersOf(args.shopId) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModelBuyShopList.getPreferencesStatus().first().let { preferences ->
                if (preferences == null) {
                    showDialogNotifySumValues()
                }
            }
        }
        initConfig()
    }

    private fun initConfig() {
        binding.recycleBuyProducts.adapter = adapterBuyShopList
        configureToolbarMenu()
        binding.linearClick.setOnClickListener { goToAddProduct() }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelBuyShopList.selectedShopList.collect { shopListWithItemsModel ->
                    shopListWithItemsModel?.let {
                        updateView(shopListWithItemsModel)
                    }
                }
            }
        }
    }

    private fun configureToolbarMenu() {
        (activity as AppCompatActivity).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_view_buy_shop_list, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                lifecycleScope.launch {
                    menu.let {
                        val itemEnable = it.findItem(R.id.item_enable_sum_values)
                        val itemDisable = it.findItem(R.id.item_disable_sum_values)
                        viewModelBuyShopList.getPreferencesStatus().collect { status ->
                            status?.let {
                                if (status) {
                                    itemDisable.isVisible = true
                                    itemEnable.isVisible = false
                                } else {
                                    itemDisable.isVisible = false
                                    itemEnable.isVisible = true
                                }
                            }
                        }
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_add_product -> {
                        goToAddProduct()
                    }

                    R.id.item_enable_sum_values -> {
                        replaceMenu(true)
                        true
                    }

                    R.id.item_disable_sum_values -> {
                        replaceMenu(false)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun goToAddProduct(): Boolean {
        val direction =
            FragmentBuyShopListDirections.actionFragmentBuyShopListToProductsFragment(
                args.shopId
            )
        findNavController().navigate(direction)
        return true
    }

    private fun updateView(
        shopListWithItemsModel: ShopListWithItemsModel
    ) {
        (activity as AppCompatActivity).supportActionBar?.title = shopListWithItemsModel.shopName
        if (shopListWithItemsModel.listItems.isEmpty()) {
            binding.imgNoItems.visibility = VISIBLE
            binding.txvEmptyList.visibility = VISIBLE
            binding.prbAmountItems.progress = 0
            binding.txvPartialValue.text = getString(R.string.str_zero_value)
            adapterBuyShopList.submitList(emptyList())
        } else {
            lifecycleScope.launch {
                viewModelBuyShopList.getPreferencesStatus().collect { preferences ->
                    preferences?.let {
                        if (preferences) {
                            binding.txvPartialText.visibility = VISIBLE
                            binding.txvPartialValue.visibility = VISIBLE

                            val sumValues =
                                shopListWithItemsModel.listItems.filter { item -> item.isOk }
                                    .sumOf { it.amount.toBigDecimal() * it.price }

                            binding.txvPartialValue.text = numberFormat.format(sumValues)
                        } else {
                            binding.txvPartialText.visibility = GONE
                            binding.txvPartialValue.visibility = GONE
                        }
                    }
                }
            }
            binding.imgNoItems.visibility = GONE
            binding.txvEmptyList.visibility = GONE
            binding.prbAmountItems.max = shopListWithItemsModel.listItems.size
            binding.prbAmountItems.progress =
                shopListWithItemsModel.listItems.filter { item -> item.isOk }.size
            adapterBuyShopList.submitList(shopListWithItemsModel.listItems)
        }
    }


    private fun replaceMenu(status: Boolean) {
        lifecycleScope.launch { viewModelBuyShopList.setPreferenceData(status) }
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
    }

    private fun showDialogLoadPrice(itemSelected: ItemShopListModel) {
        val dialogView = DialogAddPriceBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)

        with(dialogView) {
            txvItemName.text = itemSelected.prodName
            edtAmountItem.setText(itemSelected.amount.toString())
            edtPriceItem.addTextChangedListener(TextWatcherNumberFormat(editText = edtPriceItem))

            edtPriceItem.setText(numberFormat.format(itemSelected.price))

            btnCancel.setOnClickListener { dialog.dismiss() }

            btnSave.setOnClickListener {
                if (viewModelBuyShopList.isEmpty(edtAmountItem.text.toString())) {
                    Toast.makeText(requireContext(),
                        getString(R.string.str_field_amount_invalid), Toast.LENGTH_SHORT).show()
                } else if (viewModelBuyShopList.isEmpty(edtPriceItem.text.toString())) {
                    Toast.makeText(requireContext(),
                        getString(R.string.str_item_price_field_invalid), Toast.LENGTH_SHORT).show()
                } else {
                    val amount = edtAmountItem.text.toString().toInt()
                    val price = edtPriceItem.text.toString().toBigDecimalFormat()

                    val editedItem = itemSelected.copy(amount = amount, price = price)

                    lifecycleScope.launch {
                        viewModelBuyShopList.saveItem(editedItem)
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDialogDeleteItem(itemSelected: ItemShopListModel) {
        val dialogView = DialogConfirmDeleteBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)

        with(dialogView) {
            txtTitle.text = getString(R.string.str_delete)
            txtTitle.text = getString(R.string.str_really_want_delete, itemSelected.prodName)

            btnCancel.setOnClickListener { dialog.dismiss() }
            btnConfirm.setOnClickListener {
                lifecycleScope.launch {
                    viewModelBuyShopList.deleteItem(itemSelected)
                }
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private suspend fun showDialogNotifySumValues() {
        val dialogView = DialogNotifySumValuesBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogView)
        with(dialogView) {
            txtTitle.text = getString(R.string.str_message)
            txtMsgSumValues.text = getString(R.string.str_by_default)
            btnOk.setOnClickListener { dialog.dismiss() }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        viewModelBuyShopList.setPreferenceData(true)
    }
}