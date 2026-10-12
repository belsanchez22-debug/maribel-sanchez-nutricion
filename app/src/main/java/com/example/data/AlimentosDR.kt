package com.example.data

import com.example.data.model.Alimento
import com.example.data.model.OrigenAlimento

val alimentosDRIniciales: List<Alimento> = listOf(
  Alimento(
    id = "arroz-blanco",
    nombre = "Arroz blanco cocido",
    porcion = "1 taza (200g)",
    calorias = 242,
    proteinas = 4.4,
    carbohidratos = 53.0,
    grasas = 0.4,
    categoria = "Cereales",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "habichuelas-rojas",
    nombre = "Habichuelas rojas guisadas",
    porcion = "1 taza",
    calorias = 225,
    proteinas = 15.0,
    carbohidratos = 40.0,
    grasas = 1.5,
    categoria = "Legumbres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "platano-maduro",
    nombre = "Plátano maduro frito",
    porcion = "2 rodajas medianas",
    calorias = 180,
    proteinas = 1.2,
    carbohidratos = 45.0,
    grasas = 6.0,
    categoria = "Tubérculos/Víveres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "yuca-hervida",
    nombre = "Yuca hervida",
    porcion = "1 taza (150g)",
    calorias = 216,
    proteinas = 1.6,
    carbohidratos = 51.0,
    grasas = 0.3,
    categoria = "Tubérculos/Víveres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "pescado-frito",
    nombre = "Pescado frito (sin rebozado)",
    porcion = "1 filete mediano (120g)",
    calorias = 205,
    proteinas = 32.0,
    carbohidratos = 0.0,
    grasas = 7.0,
    categoria = "Proteínas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "aguacate",
    nombre = "Aguacate",
    porcion = "½ unidad mediana",
    calorias = 160,
    proteinas = 2.0,
    carbohidratos = 8.5,
    grasas = 15.0,
    categoria = "Grasas saludables",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "guanabana",
    nombre = "Guanábana",
    porcion = "1 taza de pulpa",
    calorias = 148,
    proteinas = 2.3,
    carbohidratos = 37.0,
    grasas = 0.7,
    categoria = "Frutas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "mango",
    nombre = "Mango",
    porcion = "1 unidad mediana",
    calorias = 135,
    proteinas = 1.0,
    carbohidratos = 35.0,
    grasas = 0.6,
    categoria = "Frutas",
    origen = OrigenAlimento.LOCAL
  ),
  // Additional authentic Dominican favorites
  Alimento(
    id = "mangu-verde",
    nombre = "Mangú de plátano verde con cebollita",
    porcion = "1 taza (200g)",
    calorias = 265,
    proteinas = 2.4,
    carbohidratos = 58.0,
    grasas = 4.0,
    categoria = "Platos típicos",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "queso-frito",
    nombre = "Queso frito dominicano",
    porcion = "1 rebanada (50g)",
    calorias = 175,
    proteinas = 11.0,
    carbohidratos = 1.0,
    grasas = 14.5,
    categoria = "Proteínas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "salami-guisado",
    nombre = "Salami dominicano guisado",
    porcion = "2 rodajas (60g)",
    calorias = 190,
    proteinas = 9.5,
    carbohidratos = 2.0,
    grasas = 16.0,
    categoria = "Proteínas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "pollo-guisado",
    nombre = "Pollo guisado criollo",
    porcion = "1 presa mediana (150g)",
    calorias = 230,
    proteinas = 29.0,
    carbohidratos = 2.5,
    grasas = 11.0,
    categoria = "Proteínas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "sancocho-dr",
    nombre = "Sancocho de 7 carnes tradicional",
    porcion = "1 plato hondo (350ml)",
    calorias = 390,
    proteinas = 26.0,
    carbohidratos = 36.0,
    grasas = 16.0,
    categoria = "Platos típicos",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "moro-guandules",
    nombre = "Moro de guandules con coco",
    porcion = "1 taza (200g)",
    calorias = 295,
    proteinas = 7.5,
    carbohidratos = 50.0,
    grasas = 7.0,
    categoria = "Cereales",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "tostones",
    nombre = "Tostones de plátano verde",
    porcion = "4 unidades (80g)",
    calorias = 185,
    proteinas = 1.0,
    carbohidratos = 27.0,
    grasas = 8.5,
    categoria = "Tubérculos/Víveres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "huevo-frito",
    nombre = "Huevo frito con cebolla salteada",
    porcion = "1 unidad grande",
    calorias = 115,
    proteinas = 6.5,
    carbohidratos = 1.0,
    grasas = 9.5,
    categoria = "Proteínas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "jugo-chinola",
    nombre = "Jugo natural de chinola (maracuyá)",
    porcion = "1 vaso (250ml)",
    calorias = 110,
    proteinas = 0.8,
    carbohidratos = 26.5,
    grasas = 0.2,
    categoria = "Bebidas",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "guineitos-verdes",
    nombre = "Guineitos verdes hervidos",
    porcion = "2 unidades medianas",
    calorias = 145,
    proteinas = 1.8,
    carbohidratos = 34.0,
    grasas = 0.4,
    categoria = "Tubérculos/Víveres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "batata-asada",
    nombre = "Batata dulce asada",
    porcion = "1 unidad mediana (150g)",
    calorias = 135,
    proteinas = 2.0,
    carbohidratos = 32.0,
    grasas = 0.2,
    categoria = "Tubérculos/Víveres",
    origen = OrigenAlimento.LOCAL
  ),
  Alimento(
    id = "pechuga-plancha",
    nombre = "Pechuga de pollo a la plancha",
    porcion = "1 filete (150g)",
    calorias = 195,
    proteinas = 39.0,
    carbohidratos = 0.0,
    grasas = 4.0,
    categoria = "Proteínas",
    origen = OrigenAlimento.IMPORTADO
  ),
  Alimento(
    id = "avena-leche",
    nombre = "Avena cocida con leche y canela",
    porcion = "1 taza (240ml)",
    calorias = 210,
    proteinas = 8.5,
    carbohidratos = 33.0,
    grasas = 4.8,
    categoria = "Cereales",
    origen = OrigenAlimento.IMPORTADO
  ),
  Alimento(
    id = "ensalada-verde",
    nombre = "Ensalada verde criolla con vinagreta",
    porcion = "1 plato mediano",
    calorias = 55,
    proteinas = 1.5,
    carbohidratos = 6.0,
    grasas = 3.0,
    categoria = "Frutas",
    origen = OrigenAlimento.LOCAL
  )
)
