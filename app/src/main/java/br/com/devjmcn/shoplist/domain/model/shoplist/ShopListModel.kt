package br.com.devjmcn.shoplist.domain.model.shoplist

import java.util.Date

data class ShopListModel(
    val shopId:Long = 0L,
    val shopName:String,
    val shopDate:Date = Date()
)