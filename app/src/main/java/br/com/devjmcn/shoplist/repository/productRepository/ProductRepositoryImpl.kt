package br.com.devjmcn.shoplist.repository.productRepository

import br.com.devjmcn.shoplist.domain.mapper.toProductEntity
import br.com.devjmcn.shoplist.domain.mapper.toProductModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.repository.room.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao
):ProductRepositoryInterface {
    override suspend fun saveProduct(productModel: ProductModel) {
        productDao.saveProduct(productModel.toProductEntity())
    }

    override suspend fun deleteProduct(productModel: ProductModel) {
        productDao.deleteProduct(productModel.toProductEntity())
    }

    override fun getAllProducts(): Flow<List<ProductModel>> {
        return productDao.getAllProducts().catch {
            emit(emptyList())
        }.map { listProductEntity ->
            listProductEntity.map { it.toProductModel() }
        }
    }

    override fun getAllProductsByName(prodName: String): Flow<List<ProductModel>> {
        return productDao.getAllProductsByName(prodName).catch {
            emit(emptyList())
        }.map { listProductEntity ->
            listProductEntity.map { it.toProductModel() }
        }
    }
}