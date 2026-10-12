package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Alimento
import com.example.data.model.MomentoComida
import com.example.data.model.OrigenAlimento
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class PlatoDetectado(
  val nombre: String,
  val ingredientes: List<String>,
  val calorias: Int,
  val proteinas: Double,
  val carbohidratos: Double,
  val grasas: Double,
  val confianza: Int,
  val emoji: String,
  val porcion: String
)

val platosTipicosDetectables = listOf(
  PlatoDetectado(
    nombre = "Mangú con Los Tres Golpes",
    ingredientes = listOf("Mangú de plátano verde", "Salami frito", "Queso frito", "Huevo con cebollita"),
    calorias = 680,
    proteinas = 28.5,
    carbohidratos = 65.0,
    grasas = 35.0,
    confianza = 96,
    emoji = "🍳",
    porcion = "1 plato completo"
  ),
  PlatoDetectado(
    nombre = "La Bandera Dominicana",
    ingredientes = listOf("Arroz blanco", "Habichuelas rojas guisadas", "Pollo al caldero", "Aguacate"),
    calorias = 720,
    proteinas = 44.0,
    carbohidratos = 85.0,
    grasas = 22.0,
    confianza = 98,
    emoji = "🍲",
    porcion = "1 servicio típico"
  ),
  PlatoDetectado(
    nombre = "Sancocho Criollo Tradicional",
    ingredientes = listOf("Plátano verde", "Yuca", "Carne de res y cerdo", "Mazorca de maíz"),
    calorias = 480,
    proteinas = 32.0,
    carbohidratos = 46.0,
    grasas = 18.0,
    confianza = 94,
    emoji = "🥣",
    porcion = "1 tazón grande"
  ),
  PlatoDetectado(
    nombre = "Pescado Frito con Tostones",
    ingredientes = listOf("Filete de pescado", "Tostones de plátano verde", "Limón criollo"),
    calorias = 450,
    proteinas = 34.0,
    carbohidratos = 32.0,
    grasas = 17.5,
    confianza = 92,
    emoji = "🐟",
    porcion = "1 filete + 4 tostones"
  )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EscanerFotoDialog(
  momentoInicial: MomentoComida,
  onDismiss: () -> Unit,
  onRegistrarAlimento: (Alimento, MomentoComida) -> Unit
) {
  var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
  var isAnalyzing by remember { mutableStateOf(false) }
  var platoIdentificado by remember { mutableStateOf<PlatoDetectado?>(null) }
  var momentoSeleccionado by remember { mutableStateOf(momentoInicial) }

  val coroutineScope = rememberCoroutineScope()

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      selectedImageUri = uri
      isAnalyzing = true
      coroutineScope.launch {
        delay(1200) // Simular análisis visual inteligente
        platoIdentificado = platosTipicosDetectables.random()
        isAnalyzing = false
      }
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 6.dp,
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
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
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                Icons.Default.CameraAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Escáner de Comida RD",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Reconocimiento de platos criollos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meal moment chips
        Text(
          text = "Registrar en:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          MomentoComida.entries.forEach { m ->
            FilterChip(
              selected = momentoSeleccionado == m,
              onClick = { momentoSeleccionado = m },
              label = { Text("${m.emoji} ${m.label}", style = MaterialTheme.typography.labelSmall) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Camera / Gallery Action Box
        if (platoIdentificado == null && !isAnalyzing) {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("escaner_caja_captura")
          ) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text("📸", fontSize = 42.sp)
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Toma o sube una foto de tu comida",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Identifica automáticamente platos típicos y estima sus calorías.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
              )

              Button(
                onClick = {
                  photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                  )
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("seleccionar_foto_btn")
              ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escanear Foto del Plato")
              }

              Spacer(modifier = Modifier.height(10.dp))

              // Demo quick test button
              OutlinedButton(
                onClick = {
                  isAnalyzing = true
                  coroutineScope.launch {
                    delay(800)
                    platoIdentificado = platosTipicosDetectables.first()
                    isAnalyzing = false
                  }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("demo_escaner_btn")
              ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Probar con Mangú Típico 🇩🇴", style = MaterialTheme.typography.labelMedium)
              }
            }
          }
        }

        // Analyzing State
        if (isAnalyzing) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Analizando plato dominicano...",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Reconociendo víveres, carnes y macros criollos",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Result state
        platoIdentificado?.let { plato ->
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("plato_detectado_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = plato.emoji, fontSize = 28.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = plato.nombre,
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "${plato.porcion} • Confianza: ${plato.confianza}%",
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Ingredientes identificados:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = plato.ingredientes.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 2.dp)
              )

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Estimado:",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${plato.calorias} kcal",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.primary
                )
              }

              Spacer(modifier = Modifier.height(6.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                MacroBadge("Proteína", "${plato.proteinas}g", ProteinColor, Modifier.weight(1f))
                MacroBadge("Carbos", "${plato.carbohidratos}g", CarbColor, Modifier.weight(1f))
                MacroBadge("Grasas", "${plato.grasas}g", FatColor, Modifier.weight(1f))
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            OutlinedButton(onClick = { platoIdentificado = null }) {
              Text("Escanear otro")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
              onClick = {
                val alimento = Alimento(
                  id = "scan-${System.currentTimeMillis()}",
                  nombre = plato.nombre,
                  porcion = plato.porcion,
                  calorias = plato.calorias,
                  proteinas = plato.proteinas,
                  carbohidratos = plato.carbohidratos,
                  grasas = plato.grasas,
                  categoria = "Platos típicos",
                  origen = OrigenAlimento.LOCAL,
                  esPersonalizado = true
                )
                onRegistrarAlimento(alimento, momentoSeleccionado)
                onDismiss()
              },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("confirmar_escaneo_btn")
            ) {
              Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Registrar Plato")
            }
          }
        }
      }
    }
  }
}
