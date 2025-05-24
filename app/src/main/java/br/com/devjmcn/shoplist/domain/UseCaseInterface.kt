package br.com.devjmcn.shoplist.domain

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import kotlinx.coroutines.flow.Flow

interface UseCaseInterface {
    fun shopNameIsValid(shopName:String):Boolean

    suspend fun saveShopList(shopListModel: ShopListModel):Long

    suspend fun deleteShopList(shopListModel: ShopListModel)

    suspend fun getAllShopList(): Flow<List<ShopListWithItemsModel>>

    fun getShopList(shopId:Long): Flow<ShopListWithItemsModel?>

    suspend fun saveItem(itemShopListModel: ItemShopListModel)
    suspend fun deleteItem(itemSelected: ItemShopListModel)
    fun isEmpty(text: String): Boolean

    suspend fun renameShopList(shopListModel: ShopListModel)

    suspend fun updateShopList(shopListModel: ShopListModel)

    suspend fun deleteItemShopList(itemShopListModel: ItemShopListModel)

    fun isNameValid(newShopName: String): Boolean

    fun isInvalidAmount(amountText: String): Boolean

    fun getShopListById(shopId:Long):Flow<ShopListWithItemsModel?>

    fun getAllProduct(): Flow<List<ProductModel>>

    fun getProductByName(name: String?): Flow<List<ProductModel>>

    fun getItemsList(shopId: Long): Flow<List<Long>?>

    suspend fun insertItemShopList(itemShopListModel: ItemShopListModel)

    suspend fun deleteItem(prodId: Long, shopId: Long)

    suspend fun deleteProduct(productModel: ProductModel)

    suspend fun insertProduct(editedProduct: ProductModel)
    suspend fun setPreferenceData(status: Boolean)
    fun getPreferenceData(): Flow<Boolean?>
}