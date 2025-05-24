package br.com.devjmcn.shoplist.domain.model.product

abstract class BaseProduct(
    open val prodId:Long = 0L,
    open val prodName:String,
    open val prodCategoryIndex:Int
)