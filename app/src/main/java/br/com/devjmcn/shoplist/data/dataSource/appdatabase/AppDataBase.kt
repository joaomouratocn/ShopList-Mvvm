package br.com.devjmcn.shoplist.data.dataSource.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.devjmcn.shoplist.data.dataSource.converters.Converters
import br.com.devjmcn.shoplist.data.dataSource.dao.ItemShopListDao
import br.com.devjmcn.shoplist.data.dataSource.dao.ProductDao
import br.com.devjmcn.shoplist.data.dataSource.dao.ShopListDao
import br.com.devjmcn.shoplist.data.dataSource.entitys.ItemShopListEntity
import br.com.devjmcn.shoplist.data.dataSource.entitys.ProductEntity
import br.com.devjmcn.shoplist.data.dataSource.entitys.ShopListEntity
import br.com.devjmcn.shoplist.data.dataSource.views.ItemShopListView

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