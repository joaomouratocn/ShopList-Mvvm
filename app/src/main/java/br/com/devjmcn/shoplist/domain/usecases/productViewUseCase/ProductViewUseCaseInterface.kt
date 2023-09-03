package br.com.devjmcn.shoplist.domain.usecases.productViewUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductViewUseCaseInterface {
    fun getAllProduct(): Flow<List<ProductModel>>

    fun getProductByName(name: String?): Flow<List<ProductModel>>

    fun getItemsList(shopId: Long): Flow<List<Long>?>

    suspend fun insertItemShopList(itemShopListModel: ItemShopListModel)

    fun isEmpty(name: String): Boolean

    suspend fun deleteItem(prodId: Long, shopId: Long)

    suspend fun deleteProduct(productModel: ProductModel)

    suspend fun insertProduct(editedProduct: ProductModel)
}