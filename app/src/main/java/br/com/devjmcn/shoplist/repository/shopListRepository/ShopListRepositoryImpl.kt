package br.com.devjmcn.shoplist.repository.shopListRepository

import br.com.devjmcn.shoplist.domain.mapper.toShopListEntity
import br.com.devjmcn.shoplist.domain.mapper.toShopListWithItemsModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.room.dao.ShopListDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopListRepositoryImpl(
    private val shopListDao: ShopListDao,
    ):ShopListRepositoryInterface {
    override suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return shopListDao.saveShopList(shopListModel.toShopListEntity())
    }

    override suspend fun updateShopList(shopListModel: ShopListModel) {
        shopListDao.updateShopList(shopListModel.toShopListEntity())
    }

    override suspend fun deleteShopList(shopListModel: ShopListModel) {
        shopListDao.deleteShopList(shopListModel.toShopListEntity())
    }

    override fun getAllShopList(): Flow<List<ShopListWithItemsModel>?> {
        return shopListDao.getAllShopList().map {listShopListWithItemNested ->
            listShopListWithItemNested?.map {
                it.toShopListWithItemsModel()
            }
        }
    }

    override fun getShopListById(shopId: Long): Flow<ShopListWithItemsModel?> {
        return shopListDao.getShopListById(shopId).map { shopListWithItemNested ->
            shopListWithItemNested?.toShopListWithItemsModel()
        }
    }
}