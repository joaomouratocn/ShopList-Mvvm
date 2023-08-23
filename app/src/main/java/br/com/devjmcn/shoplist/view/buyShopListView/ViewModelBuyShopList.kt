package br.com.devjmcn.shoplist.view.buyShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.domain.usecases.buyShopListUseCase.BuyShopListViewUseCaseInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelBuyShopList(
    private val buyShopListUseCaseInterface: BuyShopListViewUseCaseInterface,
    shopId: Long
) : ViewModel() {
    private val _selectedShopList: MutableStateFlow<ShopListWithItemsModel?> =
        MutableStateFlow(null)
    val selectedShopList: StateFlow<ShopListWithItemsModel?> = _selectedShopList

    init {
        viewModelScope.launch {
            launch {
                buyShopListUseCaseInterface.getShopList(shopId).collect {
                    _selectedShopList.value = it
                }
            }
        }
    }

    suspend fun saveItem(itemShopListModel: ItemShopListModel) {
        buyShopListUseCaseInterface.saveItem(itemShopListModel)
    }

    suspend fun deleteItem(itemSelected: ItemShopListModel) {
        buyShopListUseCaseInterface.deleteItem(itemSelected)
    }

    suspend fun setPreferenceData(status:Boolean){
        buyShopListUseCaseInterface.setSumValueStatus(status)
    }

    fun getPreferencesStatus(): Flow<Boolean?> {
        return buyShopListUseCaseInterface.getStatusSumValues()
    }

    fun isEmpty(text: String): Boolean {
        return buyShopListUseCaseInterface.isEmpty(text)
    }
}