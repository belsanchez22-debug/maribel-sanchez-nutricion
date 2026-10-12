package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RegistroPeso

@Composable
fun RegistroPesoCard(
  historial: List<RegistroPeso>,
  onRegistrarPeso: (Double, String?) -> Unit,
  onEliminarPeso: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  var pesoTexto by remember { mutableStateOf("") }
  var notaTexto by remember { mutableStateOf("") }
  var errorTexto by remember { mutableStateOf<String?>(null) }

  val ultimo = historial.getOrNull(0)
  val anterior = historial.getOrNull(1)
  val diferencia = if (ultimo != null && anterior != null) ultimo.peso - anterior.peso else 0.0

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("registro_peso_card")
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFF805AD5).copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            Icons.Default.MonitorWeight,
            contentDescription = null,
            tint = Color(0xFF6B46C1),
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Control de Peso",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Seguimiento y evolución corporal",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Input Form
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = pesoTexto,
          onValueChange = {
            pesoTexto = it
            errorTexto = null
          },
          label = { Text("Peso (kg)") },
          placeholder = { Text("Ej. 68.5") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          singleLine = true,
          modifier = Modifier
            .weight(1f)
            .testTag("input_peso_actual")
        )

        OutlinedTextField(
          value = notaTexto,
          onValueChange = { notaTexto = it },
          label = { Text("Nota (opcional)") },
          placeholder = { Text("Ej. En ayunas") },
          singleLine = true,
          modifier = Modifier.weight(1.3f)
        )
      }

      if (errorTexto != null) {
        Text(
          text = errorTexto!!,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.error,
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Button(
        onClick = {
          val pesoNum = pesoTexto.toDoubleOrNull()
          if (pesoNum == null || pesoNum < 20.0 || pesoNum > 300.0) {
            errorTexto = "Ingresa un peso válido entre 20 y 300 kg"
            return@Button
          }
          onRegistrarPeso(pesoNum, notaTexto)
          pesoTexto = ""
          notaTexto = ""
          errorTexto = null
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B46C1)),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("btn_guardar_peso")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Registrar Peso", fontWeight = FontWeight.Bold)
      }

      // Latest Record Highlight
      if (ultimo != null) {
        Spacer(modifier = Modifier.height(14.dp))
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFFFAF5FF),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Último registro:",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6B46C1),
                fontWeight = FontWeight.SemiBold
              )
              Row(verticalAlignment = Alignment.Bottom) {
                Text(
                  text = "%.1f".format(ultimo.peso),
                  style = MaterialTheme.typography.headlineMedium,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = " kg",
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
                )
              }
              Text(
                text = "${ultimo.fecha}${if (!ultimo.nota.isNullOrBlank()) " • ${ultimo.nota}" else ""}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Difference badge
            if (anterior != null) {
              val diffAbs = Math.abs(diferencia)
              val diffColor = when {
                diferencia < 0.0 -> Color(0xFF2E7D32)
                diferencia > 0.0 -> Color(0xFFC62828)
                else -> Color(0xFF757575)
              }
              val diffText = when {
                diferencia < 0.0 -> "⬇️ -%.1f kg".format(diffAbs)
                diferencia > 0.0 -> "⬆️ +%.1f kg".format(diffAbs)
                else -> "→ Sin cambio"
              }

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = diffColor.copy(alpha = 0.15f)
              ) {
                Text(
                  text = diffText,
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = diffColor,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
              }
            }
          }
        }
      }

      // History items
      if (historial.isNotEmpty()) {
        Spacer(modifier = Modifier.height(14.dp))
        Text(
          text = "📋 Historial Reciente",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        historial.take(4).forEach { reg ->
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = "%.1f kg".format(reg.peso),
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "${reg.fecha}${if (!reg.nota.isNullOrBlank()) " · ${reg.nota}" else ""}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              IconButton(
                onClick = { onEliminarPeso(reg.id) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  Icons.Default.Delete,
                  contentDescription = "Eliminar",
                  tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }
        }
      }
    }
  }
}
