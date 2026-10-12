package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface NutricionDao {

  @Query("SELECT * FROM perfil_usuario WHERE id = 1 LIMIT 1")
  fun getPerfil(): Flow<PerfilEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPerfil(perfil: PerfilEntity)

  @Query("SELECT * FROM alimentos ORDER BY nombre ASC")
  fun getAllAlimentos(): Flow<List<AlimentoEntity>>

  @Query("SELECT COUNT(*) FROM alimentos")
  suspend fun countAlimentos(): Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertAlimentos(alimentos: List<AlimentoEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAlimento(alimento: AlimentoEntity)

  @Transaction
  @Query("SELECT * FROM comidas_registradas WHERE fecha = :fecha ORDER BY timestamp ASC")
  fun getComidasPorFecha(fecha: String): Flow<List<ComidaWithAlimentoRelation>>

  @Transaction
  @Query("SELECT * FROM comidas_registradas ORDER BY timestamp DESC")
  fun getAllComidas(): Flow<List<ComidaWithAlimentoRelation>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComidaRegistrada(comida: ComidaRegistradaEntity)

  @Query("DELETE FROM comidas_registradas WHERE id = :id")
  suspend fun deleteComidaRegistrada(id: Long)

  // Registro de Agua
  @Query("SELECT * FROM registro_agua WHERE fecha = :fecha LIMIT 1")
  fun getAguaPorFecha(fecha: String): Flow<RegistroAguaEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateAgua(registro: RegistroAguaEntity)

  // Control de Peso
  @Query("SELECT * FROM registro_peso ORDER BY timestamp DESC, id DESC")
  fun getHistorialPeso(): Flow<List<RegistroPesoEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRegistroPeso(registro: RegistroPesoEntity)

  @Query("DELETE FROM registro_peso WHERE id = :id")
  suspend fun deleteRegistroPeso(id: Long)
}
