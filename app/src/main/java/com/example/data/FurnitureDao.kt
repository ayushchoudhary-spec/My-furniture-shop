package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FurnitureDao {
  @Query("SELECT * FROM furniture ORDER BY dateAdded DESC")
  fun getAllItems(): Flow<List<FurnitureItem>>

  @Query("SELECT COUNT(*) FROM furniture")
  fun getItemsCount(): Flow<Int>

  @Query("SELECT COUNT(*) FROM furniture")
  suspend fun getCount(): Int

  @Query("SELECT * FROM furniture WHERE category = :category ORDER BY dateAdded DESC")
  fun getItemsByCategory(category: String): Flow<List<FurnitureItem>>

  @Query("SELECT * FROM furniture WHERE id = :id LIMIT 1")
  suspend fun getItemById(id: Long): FurnitureItem?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItem(item: FurnitureItem): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<FurnitureItem>)

  @Update
  suspend fun updateItem(item: FurnitureItem)

  @Delete
  suspend fun deleteItem(item: FurnitureItem)

  @Query("DELETE FROM furniture WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("DELETE FROM furniture")
  suspend fun clearAll()
}
