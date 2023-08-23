package br.com.devjmcn.shoplist.domain.usecases.editShopListViewUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface EditShopListViewUseCaseInterface {
    suspend fun renameShopList(shopListModel: ShopListModel)

    suspend fun deleteShopList(shopListWithItemsModel: ShopListWithItemsModel?)

    suspend fun updateShopList(shopListWithItemsModel: ShopListWithItemsModel)

    suspend fun deleteItemShopList(itemShopListModel: ItemShopListModel)

    suspend fun saveItem(editedItem: ItemShopListModel)

    fun isNameValid(newShopName: String): Boolean

    fun isInvalidAmount(amountText: String): Boolean

    fun getShopListById(shopId:Long):Flow<ShopListWithItemsModel?>
}