package br.com.devjmcn.shoplist.repository.room.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.devjmcn.shoplist.repository.room.converters.Converters
import br.com.devjmcn.shoplist.repository.room.dao.ItemShopListDao
import br.com.devjmcn.shoplist.repository.room.dao.ProductDao
import br.com.devjmcn.shoplist.repository.room.dao.ShopListDao
import br.com.devjmcn.shoplist.repository.room.entitys.ItemShopListEntity
import br.com.devjmcn.shoplist.repository.room.entitys.ProductEntity
import br.com.devjmcn.shoplist.repository.room.entitys.ShopListEntity
import br.com.devjmcn.shoplist.repository.room.views.ItemShopListView

@Database(
    entities = [ProductEntity::class, ShopListEntity::class, ItemShopListEntity::class],
    views = [ItemShopListView::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shopListDao(): ShopListDao
    abstract fun itemShopListDao(): ItemShopListDao
}