package br.com.devjmcn.shoplist.view.productsView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.databinding.DialogEditItemBinding
import br.com.devjmcn.shoplist.databinding.DialogNewProductBinding
import br.com.devjmcn.shoplist.databinding.FragmentProductsBinding
import br.com.devjmcn.shoplist.domain.mapper.setCategoryMap
import br.com.devjmcn.shoplist.domain.mapper.toItemShopListModel
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.util.extensions.createDialog
import br.com.devjmcn.shoplist.util.extensions.upFirstChar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class FragmentProduct : Fragment() {
    private val args: FragmentProductArgs by navArgs()

    private val binding by lazy { FragmentProductsBinding.inflate(layoutInflater) }

    private val productViewModel: ViewModelProduct by
    viewModel(qualifier = named("PRODUCT_VIEW_MODEL")) { parametersOf(args.shopId) }

    private val productItemDecoration = ProductItemDecoration()

    private val addAdapterProductAdd by lazy {
        AdapterProductAdd(object : AdapterProductAdd.OnClickProductAddEvent {
            override fun insertItem(productModel: ProductModel) {
                lifecycleScope.launch {
                    productViewModel.saveItem(productModel.toItemShopListModel(args.shopId))
                }
            }

            override fun removeItem(productModel: ProductModel) {
                lifecycleScope.launch {
                    productViewModel.deleteItem(prodId = productModel.prodId, shopId = args.shopId)
                }
            }

            override fun optItem(productModel: ProductModel) {
                showDialogEditItem(productModel.toItemShopListModel(args.shopId))
            }
        })
    }

    private val editProductAdapter by lazy {
        AdapterProductEdit(object : AdapterProductEdit.OnEditProductClickEvent {
            override fun deleteProduct(selectedProduct: ProductModel) {
                lifecycleScope.launch {
                    productViewModel.deleteProduct(selectedProduct)
                }
            }

            override fun editProduct(selectedProduct: ProductModel) {
                showDialogNewProduct(selectedProduct)
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
        configureToolbarMenu()
        configureAdapter()
        configureFlowData()
    }

    private fun invalidMenu() {
        (requireActivity() as AppCompatActivity).invalidateOptionsMenu()
    }

    private fun configureAdapter() {
        lifecycleScope.launch {
            productViewModel.editProductMode.collect { status ->
                binding.apply {
                    if (status) {
                        recycleProducts.adapter = editProductAdapter
                        recycleProducts.removeItemDecoration(productItemDecoration)
                    } else {
                        recycleProducts.adapter = addAdapterProductAdd
                        recycleProducts.addItemDecoration(productItemDecoration)
                    }
                }
            }
        }
    }

    private fun configureFlowData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    productViewModel.allProducts.collect {listProduct ->
                        when (listProduct) {
                            null -> {
                                showViews(progressLoadingVisibility = VISIBLE)
                            }

                            emptyList<ProductModel>() -> {
                                showViews(txvNoProductVisibility = VISIBLE)
                                addAdapterProductAdd.submitList(listProduct)
                            }

                            else -> {
                                showViews()
                                editProductAdapter.submitList(listProduct)
                                productItemDecoration.loadCategory(listProduct.setCategoryMap())
                                addAdapterProductAdd.submitList(listProduct)
                            }
                        }
                    }
                }
                launch {
                    productViewModel.listAlreadyProducts.collect {
                        it?.let { it1 -> addAdapterProductAdd.loadAlreadyProducts(it1) }
                    }
                }
            }
        }
    }

    private fun configureToolbarMenu() {
        (requireActivity() as AppCompatActivity).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                lifecycleScope.launch {
                    productViewModel.editProductMode.collect { status ->
                        setMenu(status, menuInflater, menu)
                    }
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_add_new_product -> {
                        showDialogNewProduct(null)
                        true
                    }

                    R.id.item_edit_product -> {
                        productViewModel.editProductMode(true)
                        invalidMenu()
                        true
                    }

                    R.id.item_done -> {
                        productViewModel.editProductMode(false)
                        invalidMenu()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setMenu(
        status: Boolean,
        menuInflater: MenuInflater,
        menu: Menu
    ) {
        if (status) {
            menuInflater.inflate(R.menu.menu_edit_product, menu)
            val menuItem = menu.findItem(R.id.item_search_add_product)
            val searchView: SearchView = menuItem?.actionView as SearchView

            searchView.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        lifecycleScope.launch {
                            productViewModel.getProductsByName(newText)
                        }
                        return true
                    }
                })
            }
        } else {
            menuInflater.inflate(R.menu.menu_add_product, menu)
            val menuItem = menu.findItem(R.id.item_search_add_product)
            val searchView: SearchView = menuItem?.actionView as SearchView

            searchView.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        lifecycleScope.launch {
                            productViewModel.getProductsByName(newText)
                        }
                        return true
                    }
                })
            }
        }
    }

    private fun showViews(
        progressLoadingVisibility: Int = GONE,
        txvNoProductVisibility: Int = GONE
    ) {
        binding.apply {
            pbrLoading.visibility = progressLoadingVisibility
            txvNoProduct.visibility = txvNoProductVisibility
        }
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
                if (productViewModel.isInvalidAmount(edtAmountItem.text.toString()))
                    tilAmountItem.error = getString(R.string.str_invalid_field)
                else {
                    val editedItem = itemShopListModel.copy(
                        amount = edtAmountItem.text.toString().toInt(),
                        typeIndex = spnType.selectedItemPosition,
                        description = edtDescription.text.toString().upFirstChar()
                    )
                    lifecycleScope.launch {
                        productViewModel.saveItem(editedItem)
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    private fun showDialogNewProduct(productModel: ProductModel?) {
        val dialogBinding = DialogNewProductBinding.inflate(layoutInflater)
        val dialog = createDialog(dialogBinding)
        with(dialogBinding) {
            productModel?.let {
                edtNameProduct.setText(productModel.prodName)

                spnCategories.setSelection(productModel.prodCategoryIndex)
            }

            btnCancel.setOnClickListener { dialog.dismiss() }

            btnSave.setOnClickListener {
                val validText = productViewModel.isNameValid(edtNameProduct.text.toString())
                if (validText) {
                    tilProdName.error = getString(R.string.str_invalid_field)
                } else if (spnCategories.selectedItemPosition == 0) {
                    (spnCategories.selectedView as TextView).error =
                        getString(R.string.str_invalid_field)
                } else {
                    val editedProduct = productModel?.copy(
                        prodName = edtNameProduct.text.toString(),
                        prodCategoryIndex = spnCategories.selectedItemPosition
                    ) ?: ProductModel(
                        prodName = edtNameProduct.text.toString().upFirstChar(),
                        prodCategoryIndex = spnCategories.selectedItemPosition
                    )
                    lifecycleScope.launch {
                        productViewModel.insertProduct(editedProduct)
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }
}