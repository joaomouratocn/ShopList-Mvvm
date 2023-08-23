package br.com.devjmcn.shoplist.domain.mapper

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import br.com.devjmcn.shoplist.domain.model.product.ProductModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListModel
import br.com.devjmcn.shoplist.domain.model.shoplist.ShopListWithItemsModel
import br.com.devjmcn.shoplist.repository.room.entitys.ItemShopListEntity
import br.com.devjmcn.shoplist.repository.room.entitys.ProductEntity
import br.com.devjmcn.shoplist.repository.room.entitys.ShopListEntity
import br.com.devjmcn.shoplist.repository.room.nested.ShopListWihItemsNested
import br.com.devjmcn.shoplist.repository.room.views.ItemShopListView

fun ProductEntity.toProductModel(): ProductModel {
    return ProductModel(
        prodId = prodId,
        prodName = prodName,
        prodCategoryIndex = prodCategoryIndex
    )
}

fun ProductModel.toProductEntity():ProductEntity{
    return ProductEntity(
        prodId = prodId,
        prodName = prodName,
        prodCategoryIndex = prodCategoryIndex
    )
}

fun ShopListModel.toShopListEntity(): ShopListEntity {
    return ShopListEntity(
        shopId = shopId,
        shopName = shopName,
        shopDate = shopDate
    )
}

fun ItemShopListModel.toItemShopListEntity(): ItemShopListEntity {
    return ItemShopListEntity(
        prodId = prodId,
        shopId = shopId,
        amount = amount,
        price = price,
        typeIndex = typeIndex,
        description = description,
        isOk = isOk
    )
}

fun ShopListWihItemsNested.toShopListWithItemsModel(): ShopListWithItemsModel {
    return ShopListWithItemsModel(
        shopId = shopListEntity.shopId,
        shopName = shopListEntity.shopName,
        shopDate = shopListEntity.shopDate,
        listItems = listItems.map {
            it.toItemShopListModel()
        }.sortedBy { itemShopListModel -> itemShopListModel.isOk }
    )
}

fun ItemShopListView.toItemShopListModel(): ItemShopListModel {
    return ItemShopListModel(
        prodId = prodId,
        prodName = prodName,
        prodCategoryIndex = prodCategoryIndex,
        shopId = shopId,
        amount = amount,
        description = description,
        price = price,
        typeIndex = typeIndex,
        isOk = isOk
    )
}

fun ProductModel.toItemShopListModel(shopId:Long):ItemShopListModel{
    return ItemShopListModel(
        shopId = shopId,
        prodId = prodId,
        prodCategoryIndex = prodCategoryIndex,
        prodName = prodName
    )
}

fun List<ProductModel>?.setCategoryMap(): Map<Int, Int> {
    return this?.mapIndexed { index, productModel ->
        index to productModel.prodCategoryIndex
    }?.distinctBy { it.second }?.toMap() ?: emptyMap()
}