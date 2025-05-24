package br.com.devjmcn.shoplist.data.dataSource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.devjmcn.shoplist.data.dataSource.entitys.ShopListEntity
import br.com.devjmcn.shoplist.data.dataSource.nested.ShopListWihItemsNested
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopListDao {
    @Insert
    suspend fun saveShopList(shopListEntity: ShopListEntity):Long

    @Update
    suspend fun updateShopList(shopListEntity: ShopListEntity)

    @Delete
    suspend fun deleteShopList(shopListEntity: ShopListEntity)

    @Transaction
    @Query("SELECT * FROM ShopListEntity ORDER BY shopId DESC")
    fun getAllShopList():Flow<List<ShopListWihItemsNested>?>

    @Transaction
    @Query("SELECT * FROM ShopListEntity WHERE shopId = :shopId")
    fun getShopListById(shopId:Long):Flow<ShopListWihItemsNested?>
}