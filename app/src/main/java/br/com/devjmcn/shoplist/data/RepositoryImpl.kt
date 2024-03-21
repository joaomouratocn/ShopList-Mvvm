package br.com.devjmcn.shoplist.data

import br.com.devjmcn.shoplist.data.dataSource.dao.ItemShopListDao
import br.com.devjmcn.shoplist.data.dataSource.dao.ProductDao
import br.com.devjmcn.shoplist.data.dataSource.dao.ShopListDao
import br.com.devjmcn.shoplist.data.dataSource.entitys.ProductEntity
import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
class RepositoryImpl(
    private val productDao: ProductDao,
    private val shopListDao: ShopListDao,
    private val itemShopListDao: ItemShopListDao
): RepositoryInterface {

    //====================  PRODUCT REPOSITORY IMPL  ====================//
    override suspend fun saveProduct(productModel: ProductModel) {
        productDao.saveProduct(productModel.toProductEntity())
    }

    override suspend fun deleteProduct(productModel: ProductModel) {
        productDao.deleteProduct(productModel.toProductEntity())
    }

    override fun getAllProducts(): Flow<List<ProductModel>> {
        return productDao.getAllProducts().map { listProductEntity ->
            listProductEntity?.map { it.toProductModel() } ?: emptyList()
        }
    }

    override fun getAllProductsByName(prodName: String): Flow<List<ProductModel>> {
        return productDao.getAllProductsByName(prodName).map{listProductEntity ->
            listProductEntity?.map { it.toProductModel() } ?: emptyList()
        }
    }

    override suspend fun saveShopList(shopListModel: ShopListModel): Long {
        return shopListDao.saveShopList(shopListModel.toShopListEntity())
    }

    override suspend fun updateShopList(shopListModel: ShopListModel) {
        shopListDao.updateShopList(shopListModel.toShopListEntity())
    }

    override suspend fun deleteShopList(shopListModel: ShopListModel) {
        shopListDao.deleteShopList(shopListModel.toShopListEntity())
    }

    override fun getAllShopList(): Flow<List<ShopListWithItemsModel>> {
        return shopListDao.getAllShopList().map {listShopListWithItemsNested ->
            listShopListWithItemsNested?.toListShopListWithItemsModel() ?: emptyList()
        }   
    }

    override fun getShopListById(shopId: Long): Flow<ShopListWithItemsModel?> {
        return shopListDao.getShopListById(shopId).map { shopListWithItemNested ->
            shopListWithItemNested?.toShopListWithItemsModel()
        }
    }

    //====================  ITEMSHOPLIST REPOSITORY IMPL  ====================//
    override suspend fun saveItemShopList(itemShopListModel: ItemShopListModel) {
        itemShopListDao.saveItemShopList(itemShopListModel.toItemShopListEntity())
    }

    override suspend fun deleteItemShopList(prodId: Long, shopId: Long) {
        itemShopListDao.deleteItemShopList(prodId, shopId)
    }

    override fun getItemsList(shopId: Long): Flow<List<Long>> {
        return itemShopListDao.getItemsList(shopId).map {itemsId ->
            itemsId ?: emptyList()
        }
    }
}