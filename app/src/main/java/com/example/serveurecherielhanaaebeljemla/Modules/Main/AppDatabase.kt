// AppDatabase.kt
package com.example.serveurecherielhanaaebeljemla.Modules.Main

import a_RoomDB.*
import android.content.Context
import androidx.room.*
import com.example.Start.P2_ClientBonsByDay.DaySoldBonsModel
import com.example.Start.P2_ClientBonsByDay.DaySoldStatistics
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.Res.DevicesTypeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Database(
    entities = [
        ArticlesBasesStatsTable::class,
        CategoriesTabelle::class,
        ColorsArticlesTabelle::class,
        SoldArticlesTabelle::class,
        ClientsModel::class,
        AppSettingsSaverModel::class,
        DevicesTypeManager::class,
        DaySoldBonsModel::class  ,
        DaySoldStatistics ::class  ,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articlesBasesStatsModelDao(): ArticlesBasesStatsModelDao
    abstract fun categoriesModelDao(): CategoriesModelDao
    abstract fun colorsArticlesDao(): ColorsArticlesDao
    abstract fun soldArticlesModelDao(): SoldArticlesTabelleDao
    abstract fun clientsModelDao(): ClientsModelDao
    abstract fun appSettingsSaverModelDao(): AppSettingsSaverModelDao
    abstract fun devicesTypeManagerDao(): DevicesTypeManagerDao
    abstract fun clientBonsByDayDao(): ClientBonsByDayDao
    abstract fun daySoldStatisticsDao(): DaySoldStatisticsDao


    companion object {
        const val DATABASE_NAME = "app_database"
    }
}

// DateConverter.kt
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideClientBonsByDayDao(db: AppDatabase) = db.clientBonsByDayDao()

    @Provides
    @Singleton
    fun provideArticlesBasesStatsModelDao(db: AppDatabase) = db.articlesBasesStatsModelDao()

    @Provides
    @Singleton
    fun provideCategoriesModelDao(db: AppDatabase) = db.categoriesModelDao()

    @Provides
    @Singleton
    fun provideColorsArticlesDao(db: AppDatabase) = db.colorsArticlesDao()

    @Provides
    @Singleton
    fun provideSoldArticlesTabelleDao(db: AppDatabase) = db.soldArticlesModelDao()

    @Provides
    @Singleton
    fun provideClientsModelDao(db: AppDatabase) = db.clientsModelDao()

    @Provides
    @Singleton
    fun provideAppSettingsSaverModelDao(db: AppDatabase) = db.appSettingsSaverModelDao()

    @Provides
    @Singleton
    fun provideDevicesTypeManagerDao(db: AppDatabase) = db.devicesTypeManagerDao()

    @Provides
    @Singleton
    fun provideDaySoldStatisticsDao(db: AppDatabase) = db.daySoldStatisticsDao()
}


@Singleton
class DatabaseRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val clientBonsByDayDao: ClientBonsByDayDao,
    private val articlesBasesStatsModelDao: ArticlesBasesStatsModelDao,
    private val categoriesModelDao: CategoriesModelDao,
    private val colorsArticlesDao: ColorsArticlesDao,
    private val soldArticlesTabelleDao: SoldArticlesTabelleDao,
    private val clientsModelDao: ClientsModelDao,
    private val appSettingsSaverModelDao: AppSettingsSaverModelDao,
    private val devicesTypeManagerDao: DevicesTypeManagerDao ,
    private val daySoldStatisticsDao: DaySoldStatisticsDao

) {
    fun getDaos() = listOf(
        clientBonsByDayDao,
        articlesBasesStatsModelDao,
        categoriesModelDao,
        colorsArticlesDao,
        soldArticlesTabelleDao,
        clientsModelDao,
        appSettingsSaverModelDao,
        devicesTypeManagerDao,
        daySoldStatisticsDao
    )

    // You can add useful database operations here
    fun getDatabase() = appDatabase
}
