package br.com.devjmcn.shoplist.data.dataSource.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
@Entity
data class ShopListEntity(
    @PrimaryKey(autoGenerate = true)
    val shopId:Long,
    val shopName:String,
    val shopDate: Date
)