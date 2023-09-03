package br.com.devjmcn.shoplist.repository.shopListRepository

import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface ShopListRepositoryInterface {
    suspend fun saveShopList(shopListModel: ShopListModel):Long

    suspend fun updateShopList(shopListModel: ShopListModel)

    suspend fun deleteShopList(shopListModel: ShopListModel)

    fun getAllShopList(): Flow<List<ShopListWithItemsModel>>

    fun getShopListById(shopId:Long): Flow<ShopListWithItemsModel?>
}