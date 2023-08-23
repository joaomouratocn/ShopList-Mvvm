package br.com.devjmcn.shoplist.repository.itemShopListRepository

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface ItemShopListRepositoryInterface {
    suspend fun saveItemShopList(itemShopListModel: ItemShopListModel)

    suspend fun deleteItemShopList(prodId:Long, shopId: Long)

    fun getShopList(shopId:Long): Flow<ShopListWithItemsModel?>

    fun getItemsList(shopId: Long): Flow<List<Long>?>
}