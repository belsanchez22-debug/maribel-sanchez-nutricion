package com.example.data.repository

import com.example.data.alimentosDRIniciales
import com.example.data.local.AlimentoEntity
import com.example.data.local.ComidaRegistradaEntity
import com.example.data.local.NutricionDao
import com.example.data.local.PerfilEntity
import com.example.data.local.RegistroAguaEntity
import com.example.data.local.RegistroPesoEntity
import com.example.data.model.Alimento
import com.example.data.model.ComidaRegistrada
import com.example.data.model.Genero
import com.example.data.model.Meta
import com.example.data.model.MomentoComida
import com.example.data.model.NivelActividad
import com.example.data.model.OrigenAlimento
import com.example.data.model.PerfilUsuario
import com.example.data.model.RegistroAgua
import com.example.data.model.RegistroPeso
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NutricionRepository(private val dao: NutricionDao) {

  val perfilFlow: Flow<PerfilUsuario?> = dao.getPerfil().map { entity ->
    entity?.let {
      PerfilUsuario(
        nombre = it.nombre,
        edad = it.edad,
        peso = it.peso,
        altura = it.altura,
        genero = Genero.fromString(it.genero),
        nivelActividad = NivelActividad.fromString(it.nivelActividad),
        meta = Meta.fromString(it.meta),
        metaAguaDiaria = it.metaAguaDiaria
      )
    }
  }

  val alimentosFlow: Flow<List<Alimento>> = dao.getAllAlimentos().map { entities ->
    entities.map { it.toDomain() }
  }

  val historialPesoFlow: Flow<List<RegistroPeso>> = dao.getHistorialPeso().map { entities ->
    entities.map {
      RegistroPeso(
        id = it.id,
        fecha = it.fecha,
        peso = it.peso,
        nota = it.nota,
        timestamp = it.timestamp
      )
    }
  }

  suspend fun preseedAlimentosSiEsNecesario() {
    val count = dao.countAlimentos()
    if (count == 0) {
      val entities = alimentosDRIniciales.map { alimento ->
        AlimentoEntity(
          id = alimento.id,
          nombre = alimento.nombre,
          porcion = alimento.porcion,
          calorias = alimento.calorias,
          proteinas = alimento.proteinas,
          carbohidratos = alimento.carbohidratos,
          grasas = alimento.grasas,
          categoria = alimento.categoria,
          origen = alimento.origen.name,
          esPersonalizado = alimento.esPersonalizado
        )
      }
      dao.insertAlimentos(entities)
    }
  }

  suspend fun guardarPerfil(perfil: PerfilUsuario) {
    val entity = PerfilEntity(
      id = 1,
      nombre = perfil.nombre,
      edad = perfil.edad,
      peso = perfil.peso,
      altura = perfil.altura,
      genero = perfil.genero.name,
      nivelActividad = perfil.nivelActividad.name,
      meta = perfil.meta.name,
      metaAguaDiaria = perfil.metaAguaDiaria
    )
    dao.insertPerfil(entity)
  }

  suspend fun agregarAlimento(alimento: Alimento) {
    val entity = AlimentoEntity(
      id = alimento.id,
      nombre = alimento.nombre,
      porcion = alimento.porcion,
      calorias = alimento.calorias,
      proteinas = alimento.proteinas,
      carbohidratos = alimento.carbohidratos,
      grasas = alimento.grasas,
      categoria = alimento.categoria,
      origen = alimento.origen.name,
      esPersonalizado = true
    )
    dao.insertAlimento(entity)
  }

  fun getComidasPorFecha(fecha: String): Flow<List<ComidaRegistrada>> {
    return dao.getComidasPorFecha(fecha).map { list ->
      list.mapNotNull { item ->
        val alimento = item.alimento?.toDomain() ?: return@mapNotNull null
        ComidaRegistrada(
          id = item.comida.id,
          alimento = alimento,
          cantidad = item.comida.cantidad,
          momento = MomentoComida.fromString(item.comida.momento),
          fecha = item.comida.fecha
        )
      }
    }
  }

  fun getAllComidas(): Flow<List<ComidaRegistrada>> {
    return dao.getAllComidas().map { list ->
      list.mapNotNull { item ->
        val alimento = item.alimento?.toDomain() ?: return@mapNotNull null
        ComidaRegistrada(
          id = item.comida.id,
          alimento = alimento,
          cantidad = item.comida.cantidad,
          momento = MomentoComida.fromString(item.comida.momento),
          fecha = item.comida.fecha
        )
      }
    }
  }

  suspend fun registrarComida(alimentoId: String, cantidad: Double, momento: MomentoComida, fecha: String) {
    val entity = ComidaRegistradaEntity(
      alimentoId = alimentoId,
      cantidad = cantidad,
      momento = momento.name,
      fecha = fecha
    )
    dao.insertComidaRegistrada(entity)
  }

  suspend fun eliminarComida(id: Long) {
    dao.deleteComidaRegistrada(id)
  }

  // Agua
  fun getAguaPorFecha(fecha: String, metaDefault: Int = 2000): Flow<RegistroAgua> {
    return dao.getAguaPorFecha(fecha).map { entity ->
      if (entity != null) {
        RegistroAgua(
          fecha = entity.fecha,
          cantidadMl = entity.cantidadMl,
          metaMl = entity.metaMl
        )
      } else {
        RegistroAgua(
          fecha = fecha,
          cantidadMl = 0,
          metaMl = metaDefault
        )
      }
    }
  }

  suspend fun guardarAgua(fecha: String, cantidadMl: Int, metaMl: Int) {
    dao.insertOrUpdateAgua(
      RegistroAguaEntity(
        fecha = fecha,
        cantidadMl = cantidadMl.coerceAtLeast(0),
        metaMl = metaMl.coerceAtLeast(500)
      )
    )
  }

  // Peso
  suspend fun registrarPeso(fecha: String, peso: Double, nota: String?) {
    dao.insertRegistroPeso(
      RegistroPesoEntity(
        fecha = fecha,
        peso = peso,
        nota = nota?.trim()?.ifBlank { null },
        timestamp = System.currentTimeMillis()
      )
    )
  }

  suspend fun eliminarPeso(id: Long) {
    dao.deleteRegistroPeso(id)
  }

  private fun AlimentoEntity.toDomain(): Alimento {
    return Alimento(
      id = this.id,
      nombre = this.nombre,
      porcion = this.porcion,
      calorias = this.calorias,
      proteinas = this.proteinas,
      carbohidratos = this.carbohidratos,
      grasas = this.grasas,
      categoria = this.categoria,
      origen = OrigenAlimento.fromString(this.origen),
      esPersonalizado = this.esPersonalizado
    )
  }
}
