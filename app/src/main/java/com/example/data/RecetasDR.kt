package com.example.data

data class Receta(
  val id: String,
  val nombre: String,
  val descripcion: String,
  val porciones: Int,
  val tiempo: String,
  val caloriasPorPorcion: Int,
  val ingredientes: List<String>,
  val pasos: List<String>,
  val proteinas: Double,
  val carbohidratos: Double,
  val grasas: Double,
  val categoria: String, // desayuno, almuerzo, cena, merienda
  val etiquetas: List<String>
)

val recetasDR: List<Receta> = listOf(
  Receta(
    id = "mangu-saludable",
    nombre = "Mangú con Huevo y Ensalada",
    descripcion = "Versión más ligera del plato tradicional dominicano sin freír",
    porciones = 2,
    tiempo = "25 min",
    caloriasPorPorcion = 320,
    ingredientes = listOf(
      "2 plátanos verdes medianos",
      "1 cucharadita de aceite de oliva virgen extra",
      "2 huevos criollos",
      "½ cebolla morada en rodajas finas",
      "1 cucharada de vinagre blanco",
      "Sal marina al gusto",
      "Rodajas de tomate y pepino fresco"
    ),
    pasos = listOf(
      "Pelar y cortar los plátanos en trozos. Hervir en agua con sal hasta que estén tiernos.",
      "Machacar agregando el aceite de oliva y un poco de agua caliente de cocción hasta lograr un puré suave.",
      "Saltear o marinar la cebolla con vinagre y sal por 10 minutos.",
      "Preparar los huevos a la plancha o sartén antiadherente sin exceso de grasa.",
      "Servir el mangú con los huevos, la cebolla salteada y la ensalada fresca."
    ),
    proteinas = 14.0,
    carbohidratos = 45.0,
    grasas = 10.0,
    categoria = "desayuno",
    etiquetas = listOf("Tradicional", "Alto en fibra", "Sin fritura", "Bajo en grasa")
  ),
  Receta(
    id = "locrio-pollo-ligero",
    nombre = "Locrio de Pollo Criollo Ligero",
    descripcion = "Arroz con pollo sazonado con verduras naturales y menos aceite",
    porciones = 4,
    tiempo = "40 min",
    caloriasPorPorcion = 410,
    ingredientes = listOf(
      "2 tazas de arroz blanco o integral",
      "500g de pechuga de pollo desgrasada en trozos",
      "1 cebolla picada y 1 pimiento morrón rojo",
      "1 taza de habichuelas tiernas o guandules",
      "2 cucharadas de pasta de tomate natural",
      "1 cucharadita de aceite de coco u oliva",
      "Ajo machacado, orégano dominicano y cilantro ancho"
    ),
    pasos = listOf(
      "Dorar el pollo con un toque de aceite a fuego medio hasta sellar.",
      "Sofreír la cebolla, el pimiento, el ajo y el orégano en el mismo caldero.",
      "Agregar el arroz, agua en proporción adecuada, salsa de tomate y el pollo.",
      "Tapar y cocinar a fuego bajo hasta que el arroz esté bien graneado y suave.",
      "Acompañar con ensalada verde mixta criolla."
    ),
    proteinas = 35.0,
    carbohidratos = 52.0,
    grasas = 6.0,
    categoria = "almuerzo",
    etiquetas = listOf("Plato fuerte", "Alto en proteína", "Nutritivo")
  ),
  Receta(
    id = "pescado-ensalada",
    nombre = "Pescado al Horno con Yuca y Ensalada",
    descripcion = "Comida costera dominicana horneada: ligera y saludable",
    porciones = 2,
    tiempo = "30 min",
    caloriasPorPorcion = 350,
    ingredientes = listOf(
      "2 filetes de pescado blanco fresco (chillo, mero o mero de roca)",
      "300g de yuca pelada y cortada",
      "Jugo de 1 limón criollo",
      "Ajo machacado, orégano seco y pimienta negra",
      "1 tomate de ensalada, ½ pepino, lechuga repollada",
      "1 cucharadita de aceite de oliva"
    ),
    pasos = listOf(
      "Marinar el pescado con limón, ajo, orégano, sal y pimienta durante 15 min.",
      "Hervir la yuca hasta que abra y esté bien suave. Escurrir.",
      "Hornear los filetes a 180°C por 12–15 minutos hasta que estén dorados.",
      "Preparar la ensalada fresca aderezada con limón y unas gotas de oliva.",
      "Servir el pescado horneado con la yuca al vapor."
    ),
    proteinas = 38.0,
    carbohidratos = 32.0,
    grasas = 8.0,
    categoria = "cena",
    etiquetas = listOf("Bajo en grasa", "Sin fritura", "Rico en Omega-3")
  ),
  Receta(
    id = "batida-avena-guanabana",
    nombre = "Batida de Avena con Guanábana",
    descripcion = "Bebida tropical refrescante y cremosa sin azúcar refinada",
    porciones = 2,
    tiempo = "10 min",
    caloriasPorPorcion = 210,
    ingredientes = listOf(
      "1 taza de pulpa pura de guanábana fresca sin semillas",
      "½ taza de avena en hojuelas",
      "1 taza de leche descremada o bebida de almendras",
      "1 cucharadita de miel pura de abeja o estevia",
      "Hielo picado al gusto y una pizca de canela"
    ),
    pasos = listOf(
      "Hidratar o licuar la avena con la leche hasta disolver por completo.",
      "Añadir la pulpa de guanábana y el endulzante natural.",
      "Licuar a alta velocidad hasta lograr una textura espumosa y cremosa.",
      "Servir bien fría con canela espolvoreada encima."
    ),
    proteinas = 5.0,
    carbohidratos = 42.0,
    grasas = 3.0,
    categoria = "merienda",
    etiquetas = listOf("Bebida fresca", "Rica en fibra", "Antioxidante")
  ),
  Receta(
    id = "habichuelas-arroz-ligero",
    nombre = "Habichuelas Rojas Guisadas con Arroz",
    descripcion = "El clásico de la gastronomía dominicana con sofrito natural bajo en sodio",
    porciones = 4,
    tiempo = "35 min",
    caloriasPorPorcion = 380,
    ingredientes = listOf(
      "1 taza de habichuelas rojas tiernas guisadas",
      "1½ tazas de arroz blanco cocido",
      "1 cebolla morada, 1 ají cubanela, 2 dientes de ajo",
      "1 trozo de auyama (calabaza) para espesar el caldo naturalmente",
      "1 cucharada de pasta de tomate natural",
      "Cilantrico fresco y orégano al gusto"
    ),
    pasos = listOf(
      "Sofreír el ají, cebolla, ajo y la auyama rallada en una pizca de aceite.",
      "Añadir las habichuelas con su caldo y cocinar a fuego medio hasta espesar con la auyama.",
      "Servir con una porción moderada de arroz.",
      "Acompañar con ensalada de repollo y aguacate fresco."
    ),
    proteinas = 16.0,
    carbohidratos = 65.0,
    grasas = 5.0,
    categoria = "almuerzo",
    etiquetas = listOf("Plato nacional", "Económico", "Proteína vegetal")
  )
)
