package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    PerfilEntity::class,
    AlimentoEntity::class,
    ComidaRegistradaEntity::class,
    RegistroPesoEntity::class,
    RegistroAguaEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class NutricionDatabase : RoomDatabase() {
  abstract fun nutricionDao(): NutricionDao

  companion object {
    @Volatile
    private var INSTANCE: NutricionDatabase? = null

    fun getDatabase(context: Context): NutricionDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          NutricionDatabase::class.java,
          "nutricion_dr.db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
