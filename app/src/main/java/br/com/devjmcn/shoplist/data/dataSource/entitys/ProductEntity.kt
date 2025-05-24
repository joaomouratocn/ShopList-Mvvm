package br.com.devjmcn.shoplist.data.dataSource.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val prodId:Long,
    val prodName:String,
    val prodCategoryIndex:Int,
)