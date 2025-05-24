package br.com.devjmcn.shoplist.presenter.buyShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.data.RepositoryInterface
import br.com.devjmcn.shoplist.domain.UseCaseInterface
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelBuyShopList(
    private val useCaseInterface: UseCaseInterface,
    shopId: Long
) : ViewModel() {
    private val _selectedShopList: MutableStateFlow<ShopListWithItemsModel?> =
        MutableStateFlow(null)
    val selectedShopList: StateFlow<ShopListWithItemsModel?> = _selectedShopList

    init {
        viewModelScope.launch {
            launch {
                useCaseInterface.getShopList(shopId).collect {
                    _selectedShopList.value = it
                }
            }
        }
    }

    suspend fun saveItem(itemShopListModel: ItemShopListModel) {
        useCaseInterface.saveItem(itemShopListModel)
    }

    suspend fun deleteItem(itemSelected: ItemShopListModel) {
        useCaseInterface.deleteItem(itemSelected)
    }

    suspend fun setPreferenceData(status:Boolean){
        useCaseInterface.setPreferenceData(status)
    }

    fun getPreferencesStatus(): Flow<Boolean?> {
        return useCaseInterface.getPreferenceData()
    }

    fun isEmpty(text: String): Boolean {
        return useCaseInterface.isEmpty(text)
    }
}