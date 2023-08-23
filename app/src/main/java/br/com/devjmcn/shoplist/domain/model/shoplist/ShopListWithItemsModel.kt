package br.com.devjmcn.shoplist.domain.model.shoplist

import br.com.devjmcn.shoplist.domain.model.item.ItemShopListModel
import java.util.Date

data class ShopListWithItemsModel(
    val shopId:Long,
    val shopName:String,
    val shopDate:Date,
    val listItems:List<ItemShopListModel>
)
