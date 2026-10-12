package com.example.data.model

enum class Genero(val label: String) {
  MASCULINO("Masculino"),
  FEMENINO("Femenino");

  companion object {
    fun fromString(value: String): Genero =
      entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
        ?: FEMENINO
  }
}

enum class NivelActividad(val label: String, val factor: Double, val descripcion: String) {
  BAJO("Bajo", 1.2, "Sedentario / Poco movimiento"),
  MEDIO("Medio", 1.55, "Actividad ligera a moderada (1-3 días/sem)"),
  ALTO("Alto", 1.725, "Actividad moderada a fuerte (3-5 días/sem)"),
  MUY_ALTO("Muy alto", 1.9, "Ejercicio intenso / Trabajo pesado (6-7 días/sem)");

  companion object {
    fun fromString(value: String): NivelActividad =
      entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
        ?: MEDIO
  }
}

enum class Meta(val label: String, val ajusteCalorias: Int, val emoji: String, val descripcion: String) {
  PERDER("Perder peso", -400, "⚖️", "Déficit moderado (-400 kcal/día)"),
  MANTENER("Mantener peso", 0, "✅", "Mantenimiento energético equilibrado"),
  GANAR("Ganar peso", 350, "💪", "Superávit saludable (+350 kcal/día)");

  companion object {
    fun fromString(value: String): Meta =
      entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
        ?: MANTENER
  }
}

data class PerfilUsuario(
  val nombre: String = "",
  val edad: Int = 0,
  val peso: Double = 0.0,      // kg
  val altura: Double = 0.0,    // cm
  val genero: Genero = Genero.FEMENINO,
  val nivelActividad: NivelActividad = NivelActividad.MEDIO,
  val meta: Meta = Meta.MANTENER,
  val metaAguaDiaria: Int = 2000 // ml
)

data class RegistroPeso(
  val id: Long = 0,
  val fecha: String,
  val peso: Double,
  val nota: String? = null,
  val timestamp: Long = System.currentTimeMillis()
)

data class RegistroAgua(
  val fecha: String,
  val cantidadMl: Int = 0,
  val metaMl: Int = 2000
) {
  val cantidadVasos: Int get() = Math.round(cantidadMl / 250.0).toInt()
  val vasosMeta: Int get() = Math.round(metaMl / 250.0).toInt()
  val porcentaje: Int get() = if (metaMl > 0) ((cantidadMl.toDouble() / metaMl) * 100).toInt().coerceAtMost(100) else 0
  val faltanteMl: Int get() = (metaMl - cantidadMl).coerceAtLeast(0)
}

data class MetasCalculadas(
  val geb: Int = 0,
  val get: Int = 0,
  val caloriasDiarias: Int = 0,
  val proteMin: Int = 0,
  val proteMax: Int = 0,
  val carboGramos: Int = 0,
  val grasaGramos: Int = 0,
  val imc: Double = 0.0,
  val categoriaImc: String = ""
)

enum class CategoriaAlimento(val label: String, val emoji: String) {
  TODOS("Todos", "🍽️"),
  CEREALES("Cereales", "🌾"),
  LEGUMBRES("Legumbres", "🫘"),
  TUBERCULOS("Tubérculos/Víveres", "🥔"),
  PROTEINAS("Proteínas", "🍗"),
  FRUTAS("Frutas", "🥭"),
  GRASAS("Grasas saludables", "🥑"),
  PLATOS_TIPICOS("Platos típicos", "🇩🇴"),
  BEBIDAS("Bebidas", "🥤")
}

enum class OrigenAlimento(val label: String, val flag: String) {
  LOCAL("Local RD", "🇩🇴"),
  IMPORTADO("Importado", "🌎");

  companion object {
    fun fromString(value: String): OrigenAlimento =
      entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
        ?: LOCAL
  }
}

data class Alimento(
  val id: String,
  val nombre: String,
  val porcion: String,
  val calorias: Int,
  val proteinas: Double,
  val carbohidratos: Double,
  val grasas: Double,
  val categoria: String,
  val origen: OrigenAlimento = OrigenAlimento.LOCAL,
  val esPersonalizado: Boolean = false
)

enum class MomentoComida(val label: String, val emoji: String, val orden: Int) {
  DESAYUNO("Desayuno", "🍳", 1),
  ALMUERZO("Almuerzo", "🍲", 2),
  CENA("Cena", "🥗", 3),
  MERIENDA("Merienda", "🍌", 4);

  companion object {
    fun fromString(value: String): MomentoComida =
      entries.find { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
        ?: DESAYUNO
  }
}

data class ComidaRegistrada(
  val id: Long = 0,
  val alimento: Alimento,
  val cantidad: Double, // multiplicador de porciones (ej. 1.0, 1.5)
  val momento: MomentoComida,
  val fecha: String // YYYY-MM-DD
) {
  val caloriasTotales: Int get() = (alimento.calorias * cantidad).toInt()
  val proteinasTotales: Double get() = alimento.proteinas * cantidad
  val carbohidratosTotales: Double get() = alimento.carbohidratos * cantidad
  val grasasTotales: Double get() = alimento.grasas * cantidad
}
