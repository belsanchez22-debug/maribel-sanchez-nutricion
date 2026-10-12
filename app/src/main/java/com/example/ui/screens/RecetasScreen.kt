package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.Receta
import com.example.data.model.Alimento
import com.example.data.model.MomentoComida
import com.example.data.model.OrigenAlimento
import com.example.data.recetasDR
import com.example.ui.components.MacroBadge
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutricionViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecetasScreen(
  viewModel: NutricionViewModel,
  modifier: Modifier = Modifier
) {
  var categoriaFiltro by remember { mutableStateOf("todas") }
  var recetaSeleccionada by remember { mutableStateOf<Receta?>(null) }

  val categorias = listOf(
    "todas" to "Todas",
    "desayuno" to "🌅 Desayuno",
    "almuerzo" to "☀️ Almuerzo",
    "cena" to "🌙 Cena",
    "merienda" to "🍎 Merienda"
  )

  val recetasFiltradas = if (categoriaFiltro == "todas") {
    recetasDR
  } else {
    recetasDR.filter { it.categoria == categoriaFiltro }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(10.dp))

    // Header
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Text("🇩🇴", fontSize = 22.sp)
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Recetas RD Saludables",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Platos típicos adaptados con balance nutricional",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (recetaSeleccionada == null) {
      // Category filter chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        categorias.forEach { (id, label) ->
          FilterChip(
            selected = categoriaFiltro == id,
            onClick = { categoriaFiltro = id },
            label = { Text(label) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.testTag("filtro_receta_$id")
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(recetasFiltradas, key = { it.id }) { receta ->
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            onClick = { recetaSeleccionada = receta },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("receta_card_${receta.id}")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = receta.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = receta.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = MaterialTheme.colorScheme.primaryContainer,
                  modifier = Modifier.padding(start = 8.dp)
                ) {
                  Text(
                    text = "${receta.caloriasPorPorcion} kcal",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  Text(
                    text = "⏱️ ${receta.tiempo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = "👥 ${receta.porciones} porciones",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  MacroBadge("P", "${receta.proteinas.toInt()}g", ProteinColor)
                  MacroBadge("C", "${receta.carbohidratos.toInt()}g", CarbColor)
                  MacroBadge("G", "${receta.grasas.toInt()}g", FatColor)
                }
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    } else {
      // Detailed View of Recipe
      val r = recetaSeleccionada!!
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(8.dp)
            ) {
              IconButton(onClick = { recetaSeleccionada = null }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
              }
              Text(
                text = "Volver a Recetas",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Text(
                text = r.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = r.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "⏱️ Tiempo: ${r.tiempo}",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.SemiBold
                )
                Text(
                  text = "👥 Porciones: ${r.porciones}",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text(
                    text = "🔥 ${r.caloriasPorPorcion} kcal por porción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroBadge("Proteína", "${r.proteinas}g", ProteinColor)
                    MacroBadge("Carbohidratos", "${r.carbohidratos}g", CarbColor)
                    MacroBadge("Grasas", "${r.grasas}g", FatColor)
                  }
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Text(
                text = "🥬 Ingredientes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(6.dp))
              r.ingredientes.forEach { ing ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                ) {
                  Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                  Text(ing, style = MaterialTheme.typography.bodyMedium)
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              Text(
                text = "📝 Preparación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(6.dp))
              r.pasos.forEachIndexed { idx, paso ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                  verticalAlignment = Alignment.Top
                ) {
                  Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                  ) {
                    Box(contentAlignment = Alignment.Center) {
                      Text(
                        text = "${idx + 1}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                      )
                    }
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(paso, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                }
              }

              Spacer(modifier = Modifier.height(16.dp))

              // Badges
              FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                r.etiquetas.forEach { tag ->
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                  ) {
                    Text(
                      text = "#$tag",
                      style = MaterialTheme.typography.labelSmall,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(20.dp))

              Button(
                onClick = {
                  val momento = when (r.categoria) {
                    "desayuno" -> MomentoComida.DESAYUNO
                    "almuerzo" -> MomentoComida.ALMUERZO
                    "cena" -> MomentoComida.CENA
                    else -> MomentoComida.MERIENDA
                  }
                  val alimento = Alimento(
                    id = "receta-${r.id}",
                    nombre = r.nombre,
                    porcion = "1 porción (${r.caloriasPorPorcion} kcal)",
                    calorias = r.caloriasPorPorcion,
                    proteinas = r.proteinas,
                    carbohidratos = r.carbohidratos,
                    grasas = r.grasas,
                    categoria = "Recetas RD",
                    origen = OrigenAlimento.LOCAL,
                    esPersonalizado = true
                  )
                  viewModel.agregarNuevoAlimento(alimento)
                  viewModel.registrarComida(alimento.id, 1.0, momento)
                  recetaSeleccionada = null
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("registrar_receta_btn")
              ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Registrar en mi Diario de Hoy")
              }
            }
          }
        }

        item {
          Spacer(modifier = Modifier.height(20.dp))
        }
      }
    }
  }
}
