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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComidaRegistrada
import com.example.data.model.OrigenAlimento
import com.example.data.model.RegistroAgua
import com.example.data.model.RegistroPeso

data class LogroItem(
  val id: String,
  val titulo: String,
  val descripcion: String,
  val icono: String,
  val desbloqueado: Boolean
)

@Composable
fun LogrosCard(
  todasLasComidas: List<ComidaRegistrada>,
  historialPeso: List<RegistroPeso>,
  registroAgua: RegistroAgua,
  modifier: Modifier = Modifier
) {
  val tieneComida = todasLasComidas.isNotEmpty()
  val tieneComidaLocal = todasLasComidas.any { it.alimento.origen == OrigenAlimento.LOCAL }
  val tieneSuficienteComida = todasLasComidas.size >= 5
  val tienePeso = historialPeso.isNotEmpty()
  val metaAguaCumplida = registroAgua.cantidadMl >= registroAgua.metaMl

  val listaLogros = listOf(
    LogroItem(
      id = "primer_paso",
      titulo = "Primer Paso",
      descripcion = "Registraste tu primera comida criolla",
      icono = "🌟",
      desbloqueado = tieneComida
    ),
    LogroItem(
      id = "sabor_nuestro",
      titulo = "Sabor Nuestro",
      descripcion = "Disfrutando platos típicos dominicanos",
      icono = "🇩🇴",
      desbloqueado = tieneComidaLocal
    ),
    LogroItem(
      id = "bien_hidratado",
      titulo = "Bien Hidratado",
      descripcion = "Cumpliste tu meta diaria de agua",
      icono = "💧",
      desbloqueado = metaAguaCumplida
    ),
    LogroItem(
      id = "seguimiento_peso",
      titulo = "Seguimiento",
      descripcion = "Registraste tu peso corporal",
      icono = "⚖️",
      desbloqueado = tienePeso
    ),
    LogroItem(
      id = "constancia",
      titulo = "Constancia",
      descripcion = "5 o más comidas registradas en tu diario",
      icono = "🔥",
      desbloqueado = tieneSuficienteComida
    ),
    LogroItem(
      id = "disciplina",
      titulo = "Disciplina de Acero",
      descripcion = "Uso activo de nutrición y balance saludable",
      icono = "💎",
      desbloqueado = tieneComida && tienePeso && metaAguaCumplida
    )
  )

  val desbloqueados = listaLogros.count { it.desbloqueado }
  val ratio = (desbloqueados.toFloat() / listaLogros.size.toFloat()).coerceIn(0f, 1f)
  val animatedRatio by animateFloatAsState(targetValue = ratio, animationSpec = tween(600), label = "logrosRatio")

  Card(
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = modifier
      .fillMaxWidth()
      .testTag("logros_card")
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
            Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Mis Logros & Hábitos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color(0xFFFEF3C7)
        ) {
          Text(
            text = "$desbloqueados / ${listaLogros.size}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB45309),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      LinearProgressIndicator(
        progress = { animatedRatio },
        modifier = Modifier
          .fillMaxWidth()
          .height(8.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = Color(0xFFD97706),
        trackColor = Color(0xFFFEF3C7),
        strokeCap = StrokeCap.Round
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Badges Grid / Column
      listaLogros.forEach { logro ->
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (logro.desbloqueado) Color(0xFFFEF9C3).copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (logro.desbloqueado) logro.icono else "🔒",
              fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = logro.titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (logro.desbloqueado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = logro.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            if (logro.desbloqueado) {
              Text("✅", fontSize = 16.sp)
            }
          }
        }
      }
    }
  }
}
