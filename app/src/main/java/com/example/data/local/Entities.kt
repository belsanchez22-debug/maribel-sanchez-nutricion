package com.example.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "perfil_usuario")
data class PerfilEntity(
  @PrimaryKey val id: Int = 1,
  val nombre: String,
  val edad: Int,
  val peso: Double,
  val altura: Double,
  val genero: String,
  val nivelActividad: String,
  val meta: String,
  val metaAguaDiaria: Int = 2000
)

@Entity(tableName = "alimentos")
data class AlimentoEntity(
  @PrimaryKey val id: String,
  val nombre: String,
  val porcion: String,
  val calorias: Int,
  val proteinas: Double,
  val carbohidratos: Double,
  val grasas: Double,
  val categoria: String,
  val origen: String,
  val esPersonalizado: Boolean = false
)

@Entity(tableName = "comidas_registradas")
data class ComidaRegistradaEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val alimentoId: String,
  val cantidad: Double,
  val momento: String,
  val fecha: String, // formato YYYY-MM-DD
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "registro_peso")
data class RegistroPesoEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val fecha: String,
  val peso: Double,
  val nota: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "registro_agua")
data class RegistroAguaEntity(
  @PrimaryKey val fecha: String, // Una entrada por fecha
  val cantidadMl: Int,
  val metaMl: Int = 2000
)

data class ComidaWithAlimentoRelation(
  @Embedded val comida: ComidaRegistradaEntity,
  @Relation(
    parentColumn = "alimentoId",
    entityColumn = "id"
  )
  val alimento: AlimentoEntity?
)
