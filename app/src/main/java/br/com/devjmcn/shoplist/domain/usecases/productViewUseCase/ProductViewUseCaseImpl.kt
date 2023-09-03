package br.com.devjmcn.shoplist.domain.usecases.productViewUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryInterface
import br.com.devjmcn.shoplist.repository.productRepository.ProductRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ProductViewUseCaseImpl(
    private val productRepositoryInterface: ProductRepositoryInterface,
    private val itemShopListRepositoryInterface: ItemShopListRepositoryInterface
)
    : ProductViewUseCaseInterface {
    override fun getAllProduct(): Flow<List<ProductModel>> {
        return productRepositoryInterface.getAllProducts().catch {
            emit(emptyList())
        }.map { listProductModel ->
            listProductModel
        }
    }

    override fun getProductByName(name:String?): Flow<List<ProductModel>> {
        val replacedName = "%$name%"
        return productRepositoryInterface.getAllProductsByName(replacedName).catch {
            emit(emptyList())
        }.map {listProductModel ->
            listProductModel
        }
    }

    override fun getItemsList(shopId: Long): Flow<List<Long>?> {
        return itemShopListRepositoryInterface.getItemsList(shopId)
    }

    override suspend fun insertItemShopList(itemShopListModel: ItemShopListModel) {
        itemShopListRepositoryInterface.saveItemShopList(itemShopListModel)
    }

    override fun isEmpty(name: String): Boolean {
        return name.isEmpty()
    }

    override suspend fun deleteItem(prodId: Long, shopId: Long) {
        itemShopListRepositoryInterface.deleteItemShopList(prodId, shopId)
    }

    override suspend fun deleteProduct(productModel: ProductModel) {
        productRepositoryInterface.deleteProduct(productModel)
    }

    override suspend fun insertProduct(editedProduct: ProductModel) {
        productRepositoryInterface.saveProduct(editedProduct)
    }
}