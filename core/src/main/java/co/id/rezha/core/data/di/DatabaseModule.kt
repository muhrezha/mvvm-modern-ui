package co.id.rezha.core.data.di

import android.content.Context
import androidx.room.Room
import co.id.rezha.core.data.db.AppDatabase
import co.id.rezha.core.data.db.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Dbku"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
}
