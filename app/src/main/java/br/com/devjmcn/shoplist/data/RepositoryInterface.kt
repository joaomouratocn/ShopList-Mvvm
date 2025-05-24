package br.com.devjmcn.shoplist.data

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    //====================  PRODUCT REPOSITORY INTERFACE  ====================//
    suspend fun saveProduct(productModel: ProductModel)

    suspend fun deleteProduct(productModel: ProductModel)

    fun getAllProducts(): Flow<List<ProductModel>>

    fun getAllProductsByName(prodName:String): Flow<List<ProductModel>>

    //====================  SHOPLIST REPOSITORY INTERFACE  ====================//

    suspend fun saveShopList(shopListModel: ShopListModel):Long

    suspend fun updateShopList(shopListModel: ShopListModel)

    suspend fun deleteShopList(shopListModel: ShopListModel)

    fun getAllShopList(): Flow<List<ShopListWithItemsModel>>

    fun getShopListById(shopId:Long): Flow<ShopListWithItemsModel?>

    //====================  ITEMSHOPLIST REPOSITORY INTERFACE  ====================//
    suspend fun saveItemShopList(itemShopListModel: ItemShopListModel)

    suspend fun deleteItemShopList(prodId:Long, shopId: Long)


    fun getItemsList(shopId: Long): Flow<List<Long>>
}