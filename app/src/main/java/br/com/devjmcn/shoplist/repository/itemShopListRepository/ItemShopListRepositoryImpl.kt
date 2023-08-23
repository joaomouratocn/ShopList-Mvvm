package br.com.devjmcn.shoplist.repository.itemShopListRepository

import br.com.devjmcn.shoplist.domain.mapper.toItemShopListEntity
import br.com.devjmcn.shoplist.domain.mapper.toShopListWithItemsModel
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.room.dao.ItemShopListDao
import br.com.devjmcn.shoplist.repository.room.dao.ShopListDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemShopListRepositoryImpl(private val itemShopListDao: ItemShopListDao, private val shopListDao: ShopListDao)
    : ItemShopListRepositoryInterface{

    override suspend fun saveItemShopList(itemShopListModel: ItemShopListModel) {
        itemShopListDao.saveItemShopList(itemShopListModel.toItemShopListEntity())
    }

    override suspend fun deleteItemShopList(prodId: Long, shopId: Long) {
        itemShopListDao.deleteItemShopList(prodId, shopId)
    }

    override fun getShopList(shopId: Long): Flow<ShopListWithItemsModel?> {
        return shopListDao.getShopListById(shopId).map {
            it?.toShopListWithItemsModel()
        }
    }

    override fun getItemsList(shopId: Long): Flow<List<Long>?> {
        return itemShopListDao.getItemsList(shopId)
    }
}