package br.com.devjmcn.shoplist.domain.model.product

data class ProductModel(
    override val prodId: Long = 0L,
    override val prodName: String,
    override val prodCategoryIndex: Int,
): BaseProduct(prodId, prodName, prodCategoryIndex)
