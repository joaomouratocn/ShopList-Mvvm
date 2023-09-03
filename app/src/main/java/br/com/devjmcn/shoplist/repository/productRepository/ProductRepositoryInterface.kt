package br.com.devjmcn.shoplist.repository.productRepository

import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepositoryInterface {
    suspend fun saveProduct(productModel: ProductModel)

    suspend fun deleteProduct(productModel: ProductModel)

    fun getAllProducts(): Flow<List<ProductModel>>

    fun getAllProductsByName(prodName:String): Flow<List<ProductModel>>
}