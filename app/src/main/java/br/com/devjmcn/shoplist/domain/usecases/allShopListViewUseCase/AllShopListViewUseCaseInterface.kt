package br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase

import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.util.ResponseStatus
import kotlinx.coroutines.flow.Flow

interface AllShopListViewUseCaseInterface {
    fun shopNameIsValid(shopName:String):Boolean

    suspend fun saveShopList(shopListModel: ShopListModel):Long

    suspend fun deleteShopList(shopListModel: ShopListModel)

    fun getAllShopList(): Flow<ResponseStatus<List<ShopListWithItemsModel>?>>
}