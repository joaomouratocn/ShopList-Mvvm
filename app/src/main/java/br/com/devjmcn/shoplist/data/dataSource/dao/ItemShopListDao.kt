package br.com.devjmcn.shoplist.data.dataSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.devjmcn.shoplist.data.dataSource.entitys.ItemShopListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemShopListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveItemShopList(itemShopListEntity: ItemShopListEntity)

    @Query("DELETE FROM ItemShopListEntity WHERE prodId = :prodId AND shopId = :shopId")
    suspend fun deleteItemShopList(prodId: Long, shopId: Long)

    @Query("SELECT prodId FROM ItemShopListEntity WHERE shopId = :shopId")
    fun getItemsList(shopId: Long): Flow<List<Long>?>
}