package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MomentoComida
import com.example.ui.components.LogrosCard
import com.example.ui.components.RegistroPesoCard
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutricionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProgresoScreen(
  viewModel: NutricionViewModel,
  modifier: Modifier = Modifier
) {
  val todasLasComidas by viewModel.todasLasComidas.collectAsState()
  val metas by viewModel.metasCalculadas.collectAsState()
  val comidasHoy by viewModel.comidasDelDia.collectAsState()
  val historialPeso by viewModel.historialPeso.collectAsState()
  val registroAgua by viewModel.registroAgua.collectAsState()

  val calHoy = comidasHoy.sumOf { it.caloriasTotales }
  val protHoy = comidasHoy.sumOf { it.proteinasTotales }
  val carbHoy = comidasHoy.sumOf { it.carbohidratosTotales }
  val fatHoy = comidasHoy.sumOf { it.grasasTotales }

  // Calcular últimos 7 días
  val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  val dayLabelFormat = SimpleDateFormat("EEE", Locale("es", "DO"))

  val ultimos7Dias = (6 downTo 0).map { diasAtras ->
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -diasAtras)
    val fechaStr = sdf.format(cal.time)
    val diaNombre = dayLabelFormat.format(cal.time).replace(".", "").uppercase()
    val caloriasDia = todasLasComidas.filter { it.fecha == fechaStr }.sumOf { it.caloriasTotales }
    Triple(diaNombre, caloriasDia, fechaStr)
  }

  val maxCaloriasSemana = (ultimos7Dias.maxOfOrNull { it.second } ?: metas.caloriasDiarias)
    .coerceAtLeast(metas.caloriasDiarias)
    .coerceAtLeast(2000)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(10.dp))
      // Header
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 6.dp)
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Text("📊", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Progreso & Nutrición",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Adherencia a tus metas calóricas y de macronutrientes",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Metric Summary Cards Row
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        SummaryMiniCard(
          titulo = "Hoy",
          valor = "$calHoy",
          subtexto = "de ${metas.caloriasDiarias} kcal",
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.weight(1f)
        )
        SummaryMiniCard(
          titulo = "Meta Calórica",
          valor = "${metas.caloriasDiarias}",
          subtexto = "Meta diaria",
          color = MaterialTheme.colorScheme.secondary,
          modifier = Modifier.weight(1f)
        )
        SummaryMiniCard(
          titulo = "Registros",
          valor = "${todasLasComidas.size}",
          subtexto = "Comidas totales",
          color = MaterialTheme.colorScheme.tertiary,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Weekly Bar Chart
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("grafico_semanal_card")
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Consumo últimos 7 días",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Meta: ${metas.caloriasDiarias} kcal",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Chart Bars
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            ultimos7Dias.forEach { (dia, cal, _) ->
              val barRatio = if (maxCaloriasSemana > 0) (cal.toFloat() / maxCaloriasSemana.toFloat()).coerceIn(0.05f, 1f) else 0.05f
              val isOverGoal = cal > metas.caloriasDiarias

              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
              ) {
                if (cal > 0) {
                  Text(
                    text = "${cal}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                }

                Box(
                  modifier = Modifier
                    .width(22.dp)
                    .fillMaxHeight(barRatio)
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                    .background(
                      if (cal == 0) MaterialTheme.colorScheme.surfaceVariant
                      else if (isOverGoal) MaterialTheme.colorScheme.error
                      else MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = dia,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Macro Balance Today
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Balance de Macronutrientes (Hoy)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(14.dp))

          val totalGramosMacros = (protHoy + carbHoy + fatHoy).coerceAtLeast(1.0)
          val pPct = ((protHoy / totalGramosMacros) * 100).toInt()
          val cPct = ((carbHoy / totalGramosMacros) * 100).toInt()
          val fPct = ((fatHoy / totalGramosMacros) * 100).toInt()

          // Segmented Progress Bar
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(12.dp)
              .clip(RoundedCornerShape(6.dp))
          ) {
            if (protHoy > 0) {
              Box(
                modifier = Modifier
                  .weight((protHoy / totalGramosMacros).toFloat())
                  .fillMaxHeight()
                  .background(ProteinColor)
              )
            }
            if (carbHoy > 0) {
              Box(
                modifier = Modifier
                  .weight((carbHoy / totalGramosMacros).toFloat())
                  .fillMaxHeight()
                  .background(CarbColor)
              )
            }
            if (fatHoy > 0) {
              Box(
                modifier = Modifier
                  .weight((fatHoy / totalGramosMacros).toFloat())
                  .fillMaxHeight()
                  .background(FatColor)
              )
            }
            if (protHoy == 0.0 && carbHoy == 0.0 && fatHoy == 0.0) {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(MaterialTheme.colorScheme.surfaceVariant)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            MacroStatItem("Proteínas", "${protHoy.toInt()}g", "$pPct%", ProteinColor)
            MacroStatItem("Carbohidratos", "${carbHoy.toInt()}g", "$cPct%", CarbColor)
            MacroStatItem("Grasas", "${fatHoy.toInt()}g", "$fPct%", FatColor)
          }
        }
      }
    }

    // Weight Tracking Card & History
    item {
      RegistroPesoCard(
        historial = historialPeso,
        onRegistrarPeso = { peso, nota -> viewModel.registrarPeso(peso, nota) },
        onEliminarPeso = { id -> viewModel.eliminarPeso(id) }
      )
    }

    // Achievements & Habits Card
    item {
      LogrosCard(
        todasLasComidas = todasLasComidas,
        historialPeso = historialPeso,
        registroAgua = registroAgua
      )
    }

    // Dominican Nutrition Tips Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Default.Lightbulb,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Consejos Nutricionales Dominicanos",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          TipItem(
            emoji = "🥑",
            titulo = "Aguacate criollo: oro verde",
            descripcion = "Rico en ácidos grasos monoinsaturados. Media unidad mediana contiene 15g de grasas cardiosaludables."
          )

          TipItem(
            emoji = "🫘",
            titulo = "La Bandera Dominicana: Proteína Completa",
            descripcion = "Al combinar arroz con habichuelas guisadas obtienes un perfil completo de aminoácidos esenciales vegetales."
          )

          TipItem(
            emoji = "🍌",
            titulo = "Víveres: Plátano, Yuca y Batata",
            descripcion = "Hervirlos en lugar de freírlos reduce drásticamente las grasas añadidas conservando su fibra y almidón resistente."
          )
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun SummaryMiniCard(
  titulo: String,
  valor: String,
  subtexto: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 1.dp,
    modifier = modifier
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(
        text = titulo,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = valor,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = color
      )
      Text(
        text = subtexto,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun MacroStatItem(
  label: String,
  gramos: String,
  porcentaje: String,
  color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(8.dp)
          .clip(CircleShape)
          .background(color)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold
      )
    }
    Text(
      text = gramos,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Text(
      text = porcentaje,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun TipItem(
  emoji: String,
  titulo: String,
  descripcion: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Text(text = emoji, fontSize = 20.sp)
    Spacer(modifier = Modifier.width(10.dp))
    Column {
      Text(
        text = titulo,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )
      Text(
        text = descripcion,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
      )
    }
  }
}
