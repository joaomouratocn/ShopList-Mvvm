package br.com.devjmcn.shoplist.data.dataSource.entitys

import androidx.room.Entity
import androidx.room.ForeignKey
import java.math.BigDecimal

@Entity(
    primaryKeys = ["prodId", "shopId"],
    foreignKeys = [
        ForeignKey(
            childColumns = ["prodId"],
            parentColumns = ["prodId"],
            entity = ProductEntity::class,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            childColumns = ["shopId"],
            parentColumns = ["shopId"],
            entity = ShopListEntity::class,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemShopListEntity(
    val prodId: Long,
    val shopId: Long,
    val amount: Int,
    val price: BigDecimal,
    val typeIndex: Int,
    val description: String,
    val isOk: Boolean
)
