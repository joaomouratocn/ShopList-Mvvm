package br.com.devjmcn.shoplist.data.dataSource.nested

import androidx.room.Embedded
import androidx.room.Relation
import br.com.devjmcn.shoplist.data.dataSource.entitys.ShopListEntity
import br.com.devjmcn.shoplist.data.dataSource.views.ItemShopListView

data class ShopListWihItemsNested(
    @Embedded val shopListEntity: ShopListEntity,
    @Relation(
        parentColumn = "shopId",
        entityColumn = "shopId"
    ) val listItems: List<ItemShopListView>
)