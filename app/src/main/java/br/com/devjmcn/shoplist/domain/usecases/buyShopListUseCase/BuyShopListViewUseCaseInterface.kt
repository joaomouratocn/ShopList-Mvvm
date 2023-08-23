package br.com.devjmcn.shoplist.domain.usecases.buyShopListUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface BuyShopListViewUseCaseInterface {
    fun getShopList(shopId:Long): Flow<ShopListWithItemsModel?>

    suspend fun saveItem(itemShopListModel: ItemShopListModel)
    suspend fun deleteItem(itemSelected: ItemShopListModel)
    suspend fun setSumValueStatus(status:Boolean)
    fun getStatusSumValues(): Flow<Boolean?>
    fun isEmpty(text: String): Boolean
}