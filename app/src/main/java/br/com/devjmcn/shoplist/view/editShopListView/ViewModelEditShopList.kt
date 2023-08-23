package br.com.devjmcn.shoplist.view.editShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.domain.usecases.editShopListViewUseCase.EditShopListViewUseCaseInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelEditShopList(
    private val editShopListViewUseCaseInterface: EditShopListViewUseCaseInterface,
    private val receivedShopId: Long
) : ViewModel() {
    private val _selectedShopListWithItems: MutableStateFlow<ShopListWithItemsModel?> =
        MutableStateFlow(null)
    val selectedShopListWithItems: StateFlow<ShopListWithItemsModel?> = _selectedShopListWithItems

    init {
        viewModelScope.launch {
            editShopListViewUseCaseInterface.getShopListById(shopId = receivedShopId).collect {
                _selectedShopListWithItems.value = it
            }
        }
    }

    suspend fun deleteShopList() {
        editShopListViewUseCaseInterface.deleteShopList(selectedShopListWithItems.value)
    }

    fun isNameValid(newShopName: String):Boolean {
        return editShopListViewUseCaseInterface.isNameValid(newShopName)
    }

    suspend fun updateShopList(newShopName: String) {
        selectedShopListWithItems.value?.let {
            val copyIt = it.copy(shopName = newShopName)
            editShopListViewUseCaseInterface.updateShopList(copyIt)
        }
    }

    suspend fun deleteItem(itemShopListModel: ItemShopListModel) {
        editShopListViewUseCaseInterface.deleteItemShopList(itemShopListModel)
    }

    fun isInvalidAmount(amountText: String): Boolean {
        return editShopListViewUseCaseInterface.isInvalidAmount(amountText)
    }

    suspend fun saveItem(editedItem: ItemShopListModel) {
        editShopListViewUseCaseInterface.saveItem(editedItem)
    }
}