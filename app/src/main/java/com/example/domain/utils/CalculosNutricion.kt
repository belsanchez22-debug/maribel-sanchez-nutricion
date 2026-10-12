package com.example.domain.utils

import com.example.data.model.Genero
import com.example.data.model.MetasCalculadas
import com.example.data.model.NivelActividad
import com.example.data.model.PerfilUsuario
import kotlin.math.roundToInt

object CalculosNutricion {

  // Calcular Gasto Energético Basal (Fórmula Harris-Benedict)
  fun calcularGEB(perfil: PerfilUsuario): Double {
    val peso = perfil.peso
    val altura = perfil.altura
    val edad = perfil.edad

    if (peso <= 0.0 || altura <= 0.0 || edad <= 0) return 0.0

    return if (perfil.genero == Genero.MASCULINO) {
      88.362 + (13.397 * peso) + (4.799 * altura) - (5.677 * edad)
    } else {
      447.593 + (9.247 * peso) + (3.098 * altura) - (4.330 * edad)
    }
  }

  // Calcular Gasto Energético Total según actividad física
  fun calcularGET(perfil: PerfilUsuario): Double {
    val geb = calcularGEB(perfil)
    if (geb <= 0.0) return 0.0
    return geb * perfil.nivelActividad.factor
  }

  // Calcular Meta Calórica diaria según el objetivo
  fun calcularMetaCalorias(perfil: PerfilUsuario): Int {
    val get = calcularGET(perfil)
    if (get <= 0.0) return 2000 // default saludable si no hay datos completos
    val calorias = get + perfil.meta.ajusteCalorias
    return calorias.roundToInt().coerceAtLeast(1200)
  }

  // Calcular Índice de Masa Corporal (IMC) y su categoría
  fun calcularIMC(peso: Double, alturaCm: Double): Pair<Double, String> {
    if (peso <= 0.0 || alturaCm <= 0.0) return 0.0 to "Sin datos"
    val alturaM = alturaCm / 100.0
    val imc = peso / (alturaM * alturaM)
    val categoria = when {
      imc < 18.5 -> "Bajo peso"
      imc < 24.9 -> "Peso normal"
      imc < 29.9 -> "Sobrepeso"
      imc < 34.9 -> "Obesidad grado I"
      else -> "Obesidad grado II+"
    }
    return (Math.round(imc * 10.0) / 10.0) to categoria
  }

  // Genera el desglose completo de metas y macronutrientes
  fun calcularMetasCompletas(perfil: PerfilUsuario): MetasCalculadas {
    val geb = calcularGEB(perfil).roundToInt()
    val get = calcularGET(perfil).roundToInt()
    val calorias = calcularMetaCalorias(perfil)

    // Proteína: 0.8g a 1.6g por kg de peso corporal (conforme a los lineamientos del usuario)
    val proteMin = if (perfil.peso > 0) (perfil.peso * 0.8).roundToInt() else 60
    val proteMax = if (perfil.peso > 0) (perfil.peso * 1.6).roundToInt() else 120

    // Macronutrientes sugeridos: Proteínas 20-25%, Carbohidratos 50%, Grasas 25-30%
    val proteGramosMedia = ((proteMin + proteMax) / 2).coerceAtLeast(50)
    val calProte = proteGramosMedia * 4
    val calRestantes = (calorias - calProte).coerceAtLeast(600)
    val carboGramos = ((calRestantes * 0.65) / 4).roundToInt()
    val grasaGramos = ((calRestantes * 0.35) / 9).roundToInt()

    val (imc, catImc) = calcularIMC(perfil.peso, perfil.altura)

    return MetasCalculadas(
      geb = geb,
      get = get,
      caloriasDiarias = calorias,
      proteMin = proteMin,
      proteMax = proteMax,
      carboGramos = carboGramos,
      grasaGramos = grasaGramos,
      imc = imc,
      categoriaImc = catImc
    )
  }
}
