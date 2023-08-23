package br.com.devjmcn.shoplist.repository.room.nested

import androidx.room.Embedded
import androidx.room.Relation
import br.com.devjmcn.shoplist.repository.room.entitys.ShopListEntity
import br.com.devjmcn.shoplist.repository.room.views.ItemShopListView

data class ShopListWihItemsNested(
    @Embedded val shopListEntity: ShopListEntity,
    @Relation(
        parentColumn = "shopId",
        entityColumn = "shopId"
    ) val listItems: List<ItemShopListView>
)