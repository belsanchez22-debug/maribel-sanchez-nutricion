package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Alimento
import com.example.data.model.ComidaRegistrada
import com.example.data.model.MomentoComida
import com.example.ui.components.CalorieGaugeCard
import com.example.ui.components.ControlAguaCard
import com.example.ui.components.EscanerFotoDialog
import com.example.ui.components.MacroBadge
import com.example.ui.components.RegistrarComidaDialog
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutricionViewModel

@Composable
fun RegistroComidaScreen(
  viewModel: NutricionViewModel,
  onNavigateToCatalog: () -> Unit,
  modifier: Modifier = Modifier
) {
  val fechaSeleccionada by viewModel.fechaSeleccionada.collectAsState()
  val comidas by viewModel.comidasDelDia.collectAsState()
  val metas by viewModel.metasCalculadas.collectAsState()
  val alimentosCatalogo by viewModel.alimentosCatalogo.collectAsState()
  val registroAgua by viewModel.registroAgua.collectAsState()

  var showRegistrarDialog by remember { mutableStateOf(false) }
  var showEscanerDialog by remember { mutableStateOf(false) }
  var momentoParaRegistrar by remember { mutableStateOf(MomentoComida.DESAYUNO) }
  var alimentoPreseleccionado by remember { mutableStateOf<Alimento?>(null) }

  // Calcular totales del día
  val totalCalorias = comidas.sumOf { it.caloriasTotales }
  val totalProteinas = comidas.sumOf { it.proteinasTotales }
  val totalCarbos = comidas.sumOf { it.carbohidratosTotales }
  val totalGrasas = comidas.sumOf { it.grasasTotales }

  val targetProte = (metas.proteMin + metas.proteMax) / 2

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header & Date Switcher
    item {
      Spacer(modifier = Modifier.height(10.dp))
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(
            onClick = { viewModel.irDiaAnterior() },
            modifier = Modifier.testTag("dia_anterior_btn")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Día anterior")
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.testTag("fecha_actual_label")
          ) {
            Icon(
              Icons.Default.CalendarToday,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = viewModel.formatFechaLegible(fechaSeleccionada),
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          IconButton(
            onClick = { viewModel.irDiaSiguiente() },
            modifier = Modifier.testTag("dia_siguiente_btn")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Día siguiente")
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Quick Actions Row: Escanear con Foto y Buscar en Catálogo
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        FilledTonalButton(
          onClick = {
            momentoParaRegistrar = MomentoComida.ALMUERZO
            showEscanerDialog = true
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("abrir_escaner_btn")
        ) {
          Text("📸 Escanear Foto")
        }

        FilledTonalButton(
          onClick = {
            momentoParaRegistrar = MomentoComida.DESAYUNO
            alimentoPreseleccionado = null
            showRegistrarDialog = true
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("buscar_alimentos_btn")
        ) {
          Text("🔍 Buscar Alimento")
        }
      }
    }

    // Calorie & Macro Gauge Card
    item {
      CalorieGaugeCard(
        consumidas = totalCalorias,
        metaCalorias = metas.caloriasDiarias,
        proteinasTotal = totalProteinas,
        carbosTotal = totalCarbos,
        grasasTotal = totalGrasas,
        metaProteMin = targetProte,
        metaCarbos = metas.carboGramos,
        metaGrasas = metas.grasaGramos
      )
    }

    // Water Hydration Card
    item {
      ControlAguaCard(
        registroAgua = registroAgua,
        onAgregarMl = { ml -> viewModel.agregarAgua(ml) },
        onReiniciar = { viewModel.reiniciarAgua() },
        onActualizarMeta = { meta -> viewModel.actualizarMetaAgua(meta) }
      )
    }

    // Empty state or intro tip
    if (comidas.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("🇩🇴", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "¡Aún no hay comidas hoy!",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Registra tu desayuno, almuerzo o merienda con platos típicos dominicanos.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            Button(
              onClick = {
                momentoParaRegistrar = MomentoComida.DESAYUNO
                alimentoPreseleccionado = null
                showRegistrarDialog = true
              },
              modifier = Modifier.testTag("registrar_primera_comida_btn")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Registrar Desayuno")
            }
          }
        }
      }
    }

    // Meal Sections: Desayuno, Almuerzo, Cena, Merienda
    items(MomentoComida.entries.sortedBy { it.orden }) { momento ->
      val comidasMomento = comidas.filter { it.momento == momento }
      val subtotalKcal = comidasMomento.sumOf { it.caloriasTotales }

      MealSectionCard(
        momento = momento,
        subtotalKcal = subtotalKcal,
        comidas = comidasMomento,
        onAddClick = {
          momentoParaRegistrar = momento
          alimentoPreseleccionado = null
          showRegistrarDialog = true
        },
        onDeleteClick = { comidaId ->
          viewModel.eliminarComida(comidaId)
        }
      )
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }

  // Dialog to register a food
  if (showRegistrarDialog) {
    RegistrarComidaDialog(
      alimentosDisponibles = alimentosCatalogo,
      momentoInicial = momentoParaRegistrar,
      alimentoPreseleccionado = alimentoPreseleccionado,
      onDismiss = { showRegistrarDialog = false },
      onConfirmar = { alimentoId, cantidad, momento ->
        viewModel.registrarComida(alimentoId, cantidad, momento)
        showRegistrarDialog = false
      }
    )
  }

  // Dialog to scan food with camera/photos
  if (showEscanerDialog) {
    EscanerFotoDialog(
      momentoInicial = momentoParaRegistrar,
      onDismiss = { showEscanerDialog = false },
      onRegistrarAlimento = { alimento, momento ->
        viewModel.agregarNuevoAlimento(alimento)
        viewModel.registrarComida(alimento.id, 1.0, momento)
        showEscanerDialog = false
      }
    )
  }
}

@Composable
fun MealSectionCard(
  momento: MomentoComida,
  subtotalKcal: Int,
  comidas: List<ComidaRegistrada>,
  onAddClick: () -> Unit,
  onDeleteClick: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header of the meal section
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = momento.emoji, fontSize = 20.sp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = momento.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (subtotalKcal > 0) {
            Text(
              text = "$subtotalKcal kcal",
              style = MaterialTheme.typography.labelLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(end = 8.dp)
            )
          }

          FilledTonalButton(
            onClick = onAddClick,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("add_meal_${momento.name.lowercase()}")
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Añadir", style = MaterialTheme.typography.labelMedium)
          }
        }
      }

      // Meal items
      if (comidas.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        comidas.forEach { item ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = item.alimento.origen.flag)
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = item.alimento.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                  )
                }
                Text(
                  text = "${item.cantidad}x (${item.alimento.porcion})",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  MacroBadge("P", "${item.proteinasTotales.toInt()}g", ProteinColor)
                  MacroBadge("C", "${item.carbohidratosTotales.toInt()}g", CarbColor)
                  MacroBadge("G", "${item.grasasTotales.toInt()}g", FatColor)
                }
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "${item.caloriasTotales} kcal",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                  onClick = { onDeleteClick(item.id) },
                  modifier = Modifier
                    .size(32.dp)
                    .testTag("eliminar_comida_${item.id}")
                ) {
                  Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
