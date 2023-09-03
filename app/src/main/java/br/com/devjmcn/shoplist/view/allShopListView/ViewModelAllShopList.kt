package br.com.devjmcn.shoplist.view.allShopListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase.AllShopListViewUseCaseInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelAllShopList(private val allShopListViewUseCaseInterface: AllShopListViewUseCaseInterface)
    : ViewModel() {

    private val _allShopList:MutableStateFlow<List<ShopListWithItemsModel>?> = MutableStateFlow(null)
    val allShopList:StateFlow<List<ShopListWithItemsModel>?> = _allShopList

    init {
        viewModelScope.launch {
            allShopListViewUseCaseInterface.getAllShopList().collect{
                _allShopList.value = it
            }
        }
    }

    suspend fun deleteShopList(shopListModel: ShopListModel) {
        allShopListViewUseCaseInterface.deleteShopList(shopListModel)
    }

    fun isNameValid(shopName: String): Boolean {
        return allShopListViewUseCaseInterface.shopNameIsValid(shopName)
    }

    suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return allShopListViewUseCaseInterface.saveShopList(shopListModel)
    }
}