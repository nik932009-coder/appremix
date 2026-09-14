package com.example.remix.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class,
        CustomerEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        PurchaseEntity::class,
        ShrinkageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RemixDatabase : RoomDatabase() {
    abstract fun remixDao(): RemixDao

    companion object {
        @Volatile
        private var Instance: RemixDatabase? = null

        fun getDatabase(context: Context): RemixDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RemixDatabase::class.java, "remix_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
