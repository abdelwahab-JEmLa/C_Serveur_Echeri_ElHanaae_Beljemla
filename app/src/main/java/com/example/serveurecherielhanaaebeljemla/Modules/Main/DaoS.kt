package com.example.serveurecherielhanaaebeljemla.Modules.Main

import a_RoomDB.ArticlesBasesStatsTable
import a_RoomDB.CategoriesTabelle
import a_RoomDB.ClientsModel
import a_RoomDB.ColorsArticlesTabelle
import a_RoomDB.SoldArticlesTabelle
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.serveurecherielhanaaebeljemla.Models.BuyBonModel
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.Res.DevicesTypeManager

import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsSaverModelDao {
    @Query("SELECT * FROM AppSettingsSaverModel")
    fun getAllFlow(): Flow<List<AppSettingsSaverModel>>

    @Query("SELECT * FROM AppSettingsSaverModel")
    suspend fun getAll(): List<AppSettingsSaverModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: AppSettingsSaverModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<AppSettingsSaverModel>)

    @Delete
    suspend fun delete(item: AppSettingsSaverModel)

    @Query("DELETE FROM AppSettingsSaverModel")
    suspend fun deleteAll()

}

@Dao
interface BuyBonModelDao {
    @Query("SELECT * FROM BuyBonModel ORDER BY date DESC")
    fun getAllFlow(): Flow<List<BuyBonModel>>

    @Query("SELECT * FROM BuyBonModel ORDER BY date DESC")
    suspend fun getAll(): List<BuyBonModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(value: BuyBonModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<BuyBonModel>)

    @Delete
    suspend fun delete(bon: BuyBonModel)

    @Query("DELETE FROM BuyBonModel")
    suspend fun deleteAll()

    @Query("SELECT * FROM BuyBonModel WHERE date = :date LIMIT 1")
    suspend fun getStatisticsByDate(date: String): BuyBonModel?
}


@Dao
interface ClientBonsByDayDao {
    @Query("SELECT * FROM DaySoldBonsModel ORDER BY date DESC")
    fun getAllBonsFlow(): Flow<List<DaySoldBonsModel>>

    @Query("SELECT * FROM DaySoldBonsModel ORDER BY date DESC")
    suspend fun getAllBons(): List<DaySoldBonsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBon(bon: DaySoldBonsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBons(bons: List<DaySoldBonsModel>)

    @Delete
    suspend fun deleteBon(bon: DaySoldBonsModel)

    @Query("DELETE FROM DaySoldBonsModel")
    suspend fun deleteAllBons()
}


@Dao
interface ArticlesBasesStatsModelDao {
    @Query("SELECT * FROM ArticlesBasesStatsTable ORDER BY idCategorie")
    suspend fun getAll(): MutableList<ArticlesBasesStatsTable>

    @Transaction
    suspend fun transaction(block: suspend ArticlesBasesStatsModelDao.() -> Unit) {
        block()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ArticlesBasesStatsTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articlesBasesStatTabelles: List<ArticlesBasesStatsTable>)

    @Query("DELETE FROM ArticlesBasesStatsTable")
    suspend fun deleteAll()

    @Update
    suspend fun updateAll(articlesBasesStatTabelles: List<ArticlesBasesStatsTable>)
}
@Dao
interface ColorsArticlesDao {
    @Query("SELECT * FROM ColorsArticlesTabelle ORDER BY classementColore")
    suspend fun getAllOrdred(): MutableList<ColorsArticlesTabelle>

    @Transaction
    suspend fun transaction(block: suspend ColorsArticlesDao.() -> Unit) {
        block()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: ColorsArticlesTabelle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(colorsArticleTabelles: List<ColorsArticlesTabelle>)

    @Query("DELETE FROM ColorsArticlesTabelle")
    suspend fun deleteAll()

    @Update
    suspend fun updateAll(colorsArticleTabelles: List<ColorsArticlesTabelle>)
}
@Dao
interface CategoriesModelDao {
    @Query("SELECT * FROM CategoriesTabelle ORDER BY idClassementCategorieInCategoriesTabele")
    suspend fun getAll(): MutableList<CategoriesTabelle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: CategoriesTabelle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoriesTabelle>)

    @Query("DELETE FROM CategoriesTabelle")
    suspend fun deleteAll()

    @Update
    suspend fun updateAll(categories: List<CategoriesTabelle>)

    @Transaction
    suspend fun transaction(block: suspend CategoriesModelDao.() -> Unit) {
        block()
    }
}

@Dao
interface SoldArticlesTabelleDao{
    @Query("SELECT * FROM SoldArticlesTabelle ORDER BY vid")
    suspend fun getAll(): MutableList<SoldArticlesTabelle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(soldArticlesTabelle: SoldArticlesTabelle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(soldArticles: List<SoldArticlesTabelle>)

    @Delete
    suspend fun delete(item: SoldArticlesTabelle)

    @Query("DELETE FROM SoldArticlesTabelle")
    suspend fun deleteAll()

    @Update
    suspend fun updateAll(soldArticles: List<SoldArticlesTabelle>)

    @Transaction
    suspend fun transaction(block: suspend SoldArticlesTabelleDao.() -> Unit) {
        block()
    }
}

@Dao
interface ClientsModelDao{
    @Query("SELECT * FROM ClientsModel ORDER BY vidSu")
    suspend fun getAll(): MutableList<ClientsModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clientsModel: ClientsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clientsModel: List<ClientsModel>)
}



@Dao
interface DevicesTypeManagerDao{
    @Query("SELECT * FROM DevicesTypeManager ORDER BY id")
    suspend fun getAll(): MutableList<DevicesTypeManager>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DevicesTypeManager)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<DevicesTypeManager>)
    @Query("SELECT MAX(id) FROM DevicesTypeManager")
    suspend fun getMaxId(): Long?

    @Query("DELETE FROM DevicesTypeManager")
    suspend fun deleteAll()

}


