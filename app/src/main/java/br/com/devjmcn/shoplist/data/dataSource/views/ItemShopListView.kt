package br.com.devjmcn.shoplist.data.dataSource.views

import androidx.room.DatabaseView
import java.math.BigDecimal

@DatabaseView(
    "SELECT itemTab.*,  prodTab.* FROM ItemShopListEntity as itemTab " +
            "INNER JOIN ProductEntity prodTab ON prodTab.prodId = itemTab.prodId"
)
data class ItemShopListView(
    val prodId: Long,
    val prodName: String,
    val prodCategoryIndex: Int,
    val shopId: Long,
    val amount: Int,
    val description: String,
    val price: BigDecimal,
    val typeIndex: Int,
    val isOk: Boolean
)
