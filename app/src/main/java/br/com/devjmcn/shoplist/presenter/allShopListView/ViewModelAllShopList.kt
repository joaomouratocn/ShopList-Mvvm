package br.com.devjmcn.shoplist.presenter.allShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.UseCaseInterface
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelAllShopList(private val useCaseInterface: UseCaseInterface)
    : ViewModel() {

    private val _allShopList:MutableStateFlow<List<ShopListWithItemsModel>?> = MutableStateFlow(null)
    val allShopList:StateFlow<List<ShopListWithItemsModel>?> = _allShopList

    init {
        viewModelScope.launch {
            useCaseInterface.getAllShopList().collect{
                _allShopList.value = it
            }
        }
    }

    suspend fun deleteShopList(shopListModel: ShopListModel) {
        useCaseInterface.deleteShopList(shopListModel)
    }

    fun isNameValid(shopName: String): Boolean {
        return useCaseInterface.shopNameIsValid(shopName)
    }

    suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return useCaseInterface.saveShopList(shopListModel)
    }
}