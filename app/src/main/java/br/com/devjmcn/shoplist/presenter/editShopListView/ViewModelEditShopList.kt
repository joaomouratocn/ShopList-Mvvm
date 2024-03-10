package br.com.devjmcn.shoplist.presenter.editShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.UseCaseInterface
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelEditShopList(
    private val useCaseInterface: UseCaseInterface,
    private val receivedShopId: Long
) : ViewModel() {
    private val _selectedShopListWithItems: MutableStateFlow<ShopListWithItemsModel?> =
        MutableStateFlow(null)
    val selectedShopListWithItems: StateFlow<ShopListWithItemsModel?> = _selectedShopListWithItems

    init {
        viewModelScope.launch {
            useCaseInterface.getShopListById(shopId = receivedShopId).collect {
                it?.let {
                    _selectedShopListWithItems.value = it
                }
            }
        }
    }

    suspend fun deleteShopList() {
        selectedShopListWithItems.value?.let {
            val shopList = ShopListModel(it.shopId, it.shopName, it.shopDate)
            useCaseInterface.deleteShopList(shopList)
        }
    }

    fun isNameValid(newShopName: String): Boolean {
        return useCaseInterface.isNameValid(newShopName)
    }

    suspend fun renameShopList(newShopName: String) {
        selectedShopListWithItems.value?.let {
            val shopList =
                ShopListModel(shopId = it.shopId, shopName = newShopName, shopDate = it.shopDate)
            useCaseInterface.updateShopList(shopList)
        }
    }

    suspend fun deleteItem(itemShopListModel: ItemShopListModel) {
        useCaseInterface.deleteItemShopList(itemShopListModel)
    }

    fun isInvalidAmount(amountText: String): Boolean {
        return useCaseInterface.isInvalidAmount(amountText)
    }

    suspend fun saveItem(itemShopListModel: ItemShopListModel) {
        useCaseInterface.saveItem(itemShopListModel)
    }
}