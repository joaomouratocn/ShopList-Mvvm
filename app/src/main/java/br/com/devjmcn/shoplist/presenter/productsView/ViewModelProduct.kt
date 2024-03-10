package br.com.devjmcn.shoplist.presenter.productsView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.UseCaseInterface
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelProduct(
    private val useCaseInterface: UseCaseInterface,
    private val shopId: Long
) : ViewModel() {
    private val _editProductMode:MutableStateFlow<Boolean> = MutableStateFlow(false)
    val editProductMode:StateFlow<Boolean> = _editProductMode

    private val _allProducts: MutableStateFlow<List<ProductModel>?> = MutableStateFlow(null)
    val allProducts: StateFlow<List<ProductModel>?> = _allProducts

    private val _listAlreadyProducts:MutableStateFlow<List<Long>?> = MutableStateFlow(null)
    val listAlreadyProducts:StateFlow<List<Long>?> = _listAlreadyProducts

    init {
        viewModelScope.launch{
            launch {
                useCaseInterface.getAllProduct().collect {
                    _allProducts.value = it
                }
            }
            launch {
                useCaseInterface.getItemsList(shopId).collect{
                    _listAlreadyProducts.value = it
                }
            }
        }
    }

    suspend fun getProductsByName(name:String?){
        useCaseInterface.getProductByName(name).collect{
            _allProducts.value = it
        }
    }

    fun isInvalidAmount(name: String): Boolean {
        return useCaseInterface.isEmpty(name)
    }

    fun isNameValid(name: String): Boolean {
        return useCaseInterface.isEmpty(name)
    }

    suspend fun saveItem(editedItem: ItemShopListModel) {
        useCaseInterface.insertItemShopList(editedItem)
    }

    suspend fun deleteItem(prodId: Long, shopId: Long) {
        useCaseInterface.deleteItem(prodId, shopId)
    }

    suspend fun insertProduct(editedProduct: ProductModel) {
        useCaseInterface.insertProduct(editedProduct)
    }

    suspend fun deleteProduct(productModel: ProductModel) {
        useCaseInterface.deleteProduct(productModel)
    }

    fun editProductMode(set:Boolean){
        _editProductMode.value = set
    }
}