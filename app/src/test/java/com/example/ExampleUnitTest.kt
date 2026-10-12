package com.example

import com.example.data.model.Genero
import com.example.data.model.Meta
import com.example.data.model.NivelActividad
import com.example.data.model.PerfilUsuario
import com.example.domain.utils.CalculosNutricion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testCalculoGEBMasculino() {
    val perfil = PerfilUsuario(
      nombre = "Juan",
      edad = 30,
      peso = 80.0,
      altura = 175.0,
      genero = Genero.MASCULINO,
      nivelActividad = NivelActividad.MEDIO,
      meta = Meta.MANTENER
    )
    val geb = CalculosNutricion.calcularGEB(perfil)
    // 88.362 + (13.397 * 80) + (4.799 * 175) - (5.677 * 30) = 88.362 + 1071.76 + 839.825 - 170.31 = 1829.637
    assertEquals(1829.6, geb, 1.0)
  }

  @Test
  fun testCalculoGEBFemenino() {
    val perfil = PerfilUsuario(
      nombre = "María",
      edad = 25,
      peso = 60.0,
      altura = 160.0,
      genero = Genero.FEMENINO,
      nivelActividad = NivelActividad.BAJO,
      meta = Meta.PERDER
    )
    val geb = CalculosNutricion.calcularGEB(perfil)
    // 447.593 + (9.247 * 60) + (3.098 * 160) - (4.330 * 25) = 447.593 + 554.82 + 495.68 - 108.25 = 1389.843
    assertEquals(1389.8, geb, 1.0)
  }

  @Test
  fun testCalculoMetaCalorias() {
    val perfil = PerfilUsuario(
      nombre = "Pedro",
      edad = 30,
      peso = 80.0,
      altura = 175.0,
      genero = Genero.MASCULINO,
      nivelActividad = NivelActividad.MEDIO, // factor 1.55
      meta = Meta.PERDER // -400
    )
    val geb = CalculosNutricion.calcularGEB(perfil)
    val get = CalculosNutricion.calcularGET(perfil)
    val calorias = CalculosNutricion.calcularMetaCalorias(perfil)

    assertTrue(get > geb)
    assertEquals(Math.round(get - 400).toInt(), calorias)
  }

  @Test
  fun testCalculoMetasCompletasRangoProteinas() {
    val perfil = PerfilUsuario(
      nombre = "Ana",
      edad = 28,
      peso = 70.0,
      altura = 165.0,
      genero = Genero.FEMENINO,
      nivelActividad = NivelActividad.ALTO,
      meta = Meta.MANTENER
    )
    val metas = CalculosNutricion.calcularMetasCompletas(perfil)
    // Proteína: 70 * 0.8 = 56, 70 * 1.6 = 112
    assertEquals(56, metas.proteMin)
    assertEquals(112, metas.proteMax)
    assertTrue(metas.caloriasDiarias > 1200)
  }
}
