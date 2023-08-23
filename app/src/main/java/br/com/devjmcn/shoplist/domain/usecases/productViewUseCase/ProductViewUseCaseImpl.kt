package br.com.devjmcn.shoplist.domain.usecases.productViewUseCase

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.repository.itemShopListRepository.ItemShopListRepositoryInterface
import br.com.devjmcn.shoplist.repository.productRepository.ProductRepositoryInterface
import br.com.devjmcn.shoplist.util.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductViewUseCaseImpl(
    private val productRepositoryInterface: ProductRepositoryInterface,
    private val itemShopListRepositoryInterface: ItemShopListRepositoryInterface
)
    : ProductViewUseCaseInterface {
    override fun getAllProduct(): Flow<ResponseStatus<List<ProductModel>?>> {
        return productRepositoryInterface.getAllProducts().map { listProductModel ->
            if (listProductModel.isNullOrEmpty()){
                ResponseStatus.EmptyData
            }else{
                ResponseStatus.ShowData(listProductModel)
            }
        }
    }

    override fun getProductByName(name:String?): Flow<ResponseStatus<List<ProductModel>?>> {
        val replacedName = "%$name%"
        return productRepositoryInterface.getAllProductsByName(replacedName).map {listProductModel ->
            if (listProductModel.isNullOrEmpty()){
                ResponseStatus.EmptyData
            }else{
                ResponseStatus.ShowData(listProductModel)
            }
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