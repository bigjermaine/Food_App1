package com.example.food_app1.data.local.Data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorites")
data class FavoriteMealEntity(
    @PrimaryKey val mealId: String,
    val name: String,
    val image: String
)

@Dao
interface FavoriteMealDao {
    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteMealEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(meal: FavoriteMealEntity)

    @Query("DELETE FROM favorites WHERE mealId = :mealId")
    suspend fun removeFavorite(mealId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mealId = :mealId)")
    fun isFavorite(mealId: String): Flow<Boolean>
}


@Database(
    entities = [FavoriteMealEntity::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteMealDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "food_db"
            ).build().also { INSTANCE = it }
        }
    }
}
