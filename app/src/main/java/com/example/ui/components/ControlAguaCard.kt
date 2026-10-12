package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RegistroAgua

@Composable
fun ControlAguaCard(
  registroAgua: RegistroAgua,
  onAgregarMl: (Int) -> Unit,
  onReiniciar: () -> Unit,
  onActualizarMeta: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var showEditarMetaDialog by remember { mutableStateOf(false) }
  var nuevaMetaTexto by remember { mutableStateOf(registroAgua.metaMl.toString()) }

  val ratio = if (registroAgua.metaMl > 0) {
    (registroAgua.cantidadMl.toFloat() / registroAgua.metaMl.toFloat()).coerceIn(0f, 1f)
  } else 0f
  val animatedRatio by animateFloatAsState(targetValue = ratio, animationSpec = tween(500), label = "aguaProgress")

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("control_agua_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            Icons.Default.Opacity,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Hidratación Diaria",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        TextButton(
          onClick = {
            nuevaMetaTexto = registroAgua.metaMl.toString()
            showEditarMetaDialog = true
          },
          modifier = Modifier.testTag("cambiar_meta_agua_btn")
        ) {
          Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Meta", style = MaterialTheme.typography.labelSmall)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Status Numbers
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        Column {
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "${registroAgua.cantidadMl}",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.tertiary
            )
            Text(
              text = " / ${registroAgua.metaMl} ml",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )
          }
          Text(
            text = "🥤 ${registroAgua.cantidadVasos} de ${registroAgua.vasosMeta} vasos completados",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (registroAgua.cantidadMl >= registroAgua.metaMl) Color(0xFF2E7D32) else MaterialTheme.colorScheme.tertiary,
          modifier = Modifier.padding(bottom = 4.dp)
        ) {
          Text(
            text = if (registroAgua.cantidadMl >= registroAgua.metaMl) "✅ 100%" else "${registroAgua.porcentaje}%",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress Bar
      LinearProgressIndicator(
        progress = { animatedRatio },
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp)),
        color = if (registroAgua.cantidadMl >= registroAgua.metaMl) Color(0xFF2E7D32) else MaterialTheme.colorScheme.tertiary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round
      )

      if (registroAgua.faltanteMl > 0) {
        Text(
          text = "Te faltan ${registroAgua.faltanteMl} ml para tu meta",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 4.dp)
        )
      } else {
        Text(
          text = "🎉 ¡Meta de hidratación cumplida hoy!",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF2E7D32),
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Quick Add Buttons Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { onAgregarMl(250) },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
          modifier = Modifier
            .weight(1f)
            .testTag("agregar_agua_250")
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("+1 vaso", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            Text("250 ml", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
          }
        }

        Button(
          onClick = { onAgregarMl(500) },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f)),
          modifier = Modifier
            .weight(1f)
            .testTag("agregar_agua_500")
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("+2 vasos", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            Text("500 ml", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
          }
        }

        FilledTonalButton(
          onClick = { onAgregarMl(150) },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("agregar_agua_150")
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("+1 taza", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            Text("150 ml", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp))
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        TextButton(
          onClick = onReiniciar,
          modifier = Modifier.testTag("reiniciar_agua_btn")
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Reiniciar hoy", style = MaterialTheme.typography.labelSmall)
        }
      }
    }
  }

  // Dialog to edit daily water goal
  if (showEditarMetaDialog) {
    AlertDialog(
      onDismissRequest = { showEditarMetaDialog = false },
      title = { Text("Meta Diaria de Agua") },
      text = {
        Column {
          Text(
            text = "Ingresa la cantidad objetivo de agua en mililitros (ml):",
            style = MaterialTheme.typography.bodyMedium
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = nuevaMetaTexto,
            onValueChange = { nuevaMetaTexto = it.filter { ch -> ch.isDigit() } },
            label = { Text("Meta (ml)") },
            placeholder = { Text("Ej. 2000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_meta_agua")
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Recomendación general: 2000 ml = 8 vasos de 250 ml",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val metaNum = nuevaMetaTexto.toIntOrNull() ?: 2000
            onActualizarMeta(metaNum.coerceAtLeast(500))
            showEditarMetaDialog = false
          }
        ) {
          Text("Guardar Meta")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditarMetaDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}
