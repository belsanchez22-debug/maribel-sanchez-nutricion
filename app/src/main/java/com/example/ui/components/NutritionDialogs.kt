package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Alimento
import com.example.data.model.MomentoComida
import com.example.data.model.OrigenAlimento
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistrarComidaDialog(
  alimentosDisponibles: List<Alimento>,
  momentoInicial: MomentoComida,
  alimentoPreseleccionado: Alimento? = null,
  onDismiss: () -> Unit,
  onConfirmar: (alimentoId: String, cantidad: Double, momento: MomentoComida) -> Unit
) {
  var selectedAlimento by remember { mutableStateOf(alimentoPreseleccionado) }
  var selectedMomento by remember { mutableStateOf(momentoInicial) }
  var cantidad by remember { mutableDoubleStateOf(1.0) }
  var searchQuery by remember { mutableStateOf("") }

  val filteredAlimentos = remember(alimentosDisponibles, searchQuery) {
    if (searchQuery.isBlank()) alimentosDisponibles
    else alimentosDisponibles.filter {
      it.nombre.contains(searchQuery, ignoreCase = true) ||
          it.categoria.contains(searchQuery, ignoreCase = true)
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Registrar Comida",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Meal moment picker chips
        Text(
          text = "Momento del día",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          MomentoComida.entries.forEach { momento ->
            FilterChip(
              selected = selectedMomento == momento,
              onClick = { selectedMomento = momento },
              label = { Text("${momento.emoji} ${momento.label}") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
              ),
              modifier = Modifier.testTag("momento_chip_${momento.name.lowercase()}")
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (selectedAlimento == null) {
          // Food selector
          Text(
            text = "Selecciona un alimento dominicano",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(6.dp))

          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar arroz, plátano, habichuelas...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("buscar_alimento_input")
          )

          Spacer(modifier = Modifier.height(8.dp))

          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 220.dp)
          ) {
            items(filteredAlimentos, key = { it.id }) { alimento ->
              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clickable { selectedAlimento = alimento },
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(text = alimento.origen.flag)
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text = alimento.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                      )
                    }
                    Text(
                      text = "${alimento.porcion} • ${alimento.categoria}",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                  Text(
                    text = "${alimento.calorias} kcal",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                }
              }
            }
          }
        } else {
          // Food chosen preview & quantity adjustment
          val alimento = selectedAlimento!!
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = alimento.origen.flag)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = alimento.nombre,
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold
                    )
                  }
                  Text(
                    text = "Porción base: ${alimento.porcion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                TextButton(onClick = { selectedAlimento = null }) {
                  Text("Cambiar")
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Stepper for quantity
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Cantidad de porciones:",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(
                    onClick = { if (cantidad > 0.5) cantidad -= 0.5 },
                    modifier = Modifier.size(36.dp)
                  ) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos")
                  }
                  Text(
                    text = "%.1fx".format(cantidad),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                  )
                  IconButton(
                    onClick = { cantidad += 0.5 },
                    modifier = Modifier.size(36.dp)
                  ) {
                    Icon(Icons.Default.Add, contentDescription = "Más")
                  }
                }
              }

              // Quick multiplier pills
              Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                listOf(0.5, 1.0, 1.5, 2.0).forEach { mul ->
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (cantidad == mul) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                      .clickable { cantidad = mul }
                      .weight(1f)
                  ) {
                    Box(
                      contentAlignment = Alignment.Center,
                      modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                      Text(
                        text = "${mul}x",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (cantidad == mul) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Computed totals preview
              val totalKcal = (alimento.calorias * cantidad).toInt()
              val totalP = "%.1f".format(alimento.proteinas * cantidad)
              val totalC = "%.1f".format(alimento.carbohidratos * cantidad)
              val totalG = "%.1f".format(alimento.grasas * cantidad)

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Total:",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "$totalKcal kcal",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                MacroBadge("Proteína", "${totalP}g", ProteinColor, Modifier.weight(1f))
                MacroBadge("Carbos", "${totalC}g", CarbColor, Modifier.weight(1f))
                MacroBadge("Grasas", "${totalG}g", FatColor, Modifier.weight(1f))
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(onClick = onDismiss) {
            Text("Cancelar")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              selectedAlimento?.let { alimento ->
                onConfirmar(alimento.id, cantidad, selectedMomento)
              }
            },
            enabled = selectedAlimento != null,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.testTag("confirmar_registro_comida")
          ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Registrar")
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NuevoAlimentoDialog(
  onDismiss: () -> Unit,
  onGuardar: (Alimento) -> Unit
) {
  var nombre by remember { mutableStateOf("") }
  var porcion by remember { mutableStateOf("1 porción (150g)") }
  var caloriasStr by remember { mutableStateOf("") }
  var proteinasStr by remember { mutableStateOf("") }
  var carbohidratosStr by remember { mutableStateOf("") }
  var grasasStr by remember { mutableStateOf("") }
  var categoria by remember { mutableStateOf("Platos típicos") }
  var origen by remember { mutableStateOf(OrigenAlimento.LOCAL) }
  var errorMsg by remember { mutableStateOf<String?>(null) }

  val categorias = listOf(
    "Platos típicos", "Cereales", "Legumbres", "Tubérculos/Víveres",
    "Proteínas", "Frutas", "Grasas saludables", "Bebidas"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "🇩🇴 Nuevo Alimento Criollo",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = nombre,
          onValueChange = { nombre = it; errorMsg = null },
          label = { Text("Nombre del alimento") },
          placeholder = { Text("Ej. Chenchén sureño, Yaniqueque") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("nuevo_alimento_nombre")
        )

        OutlinedTextField(
          value = porcion,
          onValueChange = { porcion = it },
          label = { Text("Tamaño de porción") },
          placeholder = { Text("Ej. 1 taza, 2 unidades") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = caloriasStr,
            onValueChange = { caloriasStr = it },
            label = { Text("Calorías (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("nuevo_alimento_calorias")
          )
          OutlinedTextField(
            value = proteinasStr,
            onValueChange = { proteinasStr = it },
            label = { Text("Proteínas (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = carbohidratosStr,
            onValueChange = { carbohidratosStr = it },
            label = { Text("Carbos (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = grasasStr,
            onValueChange = { grasasStr = it },
            label = { Text("Grasas (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.weight(1f)
          )
        }

        // Categoría selector
        Text(
          text = "Categoría:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          categorias.take(4).forEach { cat ->
            FilterChip(
              selected = categoria == cat,
              onClick = { categoria = cat },
              label = { Text(cat, style = MaterialTheme.typography.labelSmall) }
            )
          }
        }

        // Origen selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = origen == OrigenAlimento.LOCAL,
            onClick = { origen = OrigenAlimento.LOCAL },
            label = { Text("🇩🇴 Local RD") },
            modifier = Modifier.weight(1f)
          )
          FilterChip(
            selected = origen == OrigenAlimento.IMPORTADO,
            onClick = { origen = OrigenAlimento.IMPORTADO },
            label = { Text("🌎 Importado") },
            modifier = Modifier.weight(1f)
          )
        }

        if (errorMsg != null) {
          Text(
            text = errorMsg!!,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (nombre.isBlank()) {
            errorMsg = "Por favor ingresa un nombre"
            return@Button
          }
          val cal = caloriasStr.toIntOrNull()
          if (cal == null || cal < 0) {
            errorMsg = "Ingresa calorías válidas"
            return@Button
          }
          val prot = proteinasStr.toDoubleOrNull() ?: 0.0
          val carbs = carbohidratosStr.toDoubleOrNull() ?: 0.0
          val fats = grasasStr.toDoubleOrNull() ?: 0.0

          val id = "custom-${System.currentTimeMillis()}"
          val nuevo = Alimento(
            id = id,
            nombre = nombre.trim(),
            porcion = porcion.ifBlank { "1 porción" },
            calorias = cal,
            proteinas = prot,
            carbohidratos = carbs,
            grasas = fats,
            categoria = categoria,
            origen = origen,
            esPersonalizado = true
          )
          onGuardar(nuevo)
        },
        modifier = Modifier.testTag("guardar_nuevo_alimento_btn")
      ) {
        Text("Guardar Alimento")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar")
      }
    }
  )
}
