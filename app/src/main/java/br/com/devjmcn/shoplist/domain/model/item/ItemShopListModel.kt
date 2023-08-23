package br.com.devjmcn.shoplist.domain.model.item

import br.com.devjmcn.shoplist.domain.model.product.BaseProduct
import java.math.BigDecimal

data class ItemShopListModel(
    override val prodId: Long,
    override val prodName: String,
    override val prodCategoryIndex: Int,
    val shopId:Long,
    val amount:Int = 1,
    val description:String = "",
    val price:BigDecimal = BigDecimal.ZERO,
    val typeIndex:Int = 0,
    val isOk:Boolean = false
): BaseProduct(prodId, prodName, prodCategoryIndex)