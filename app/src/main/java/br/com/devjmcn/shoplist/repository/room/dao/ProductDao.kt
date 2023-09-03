package br.com.devjmcn.shoplist.repository.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.devjmcn.shoplist.repository.room.entitys.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProduct(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity ORDER BY prodCategoryIndex ASC, prodName ASC")
    fun getAllProducts():Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE prodName LIKE :prodName " +
            "ORDER BY prodCategoryIndex ASC, prodName ASC")
    fun getAllProductsByName(prodName:String):Flow<List<ProductEntity>>
}