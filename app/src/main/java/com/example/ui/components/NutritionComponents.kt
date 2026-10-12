package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor

@Composable
fun MacroProgressBar(
  label: String,
  consumido: Double,
  meta: Int,
  color: Color,
  unidad: String = "g",
  modifier: Modifier = Modifier
) {
  val ratio = if (meta > 0) (consumido / meta.toDouble()).toFloat().coerceIn(0f, 1.5f) else 0f
  val animatedRatio by animateFloatAsState(targetValue = ratio.coerceAtMost(1f), animationSpec = tween(500), label = "ratio")

  Column(modifier = modifier) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = label,
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Text(
        text = "${consumido.toInt()}/${meta}$unidad",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    LinearProgressIndicator(
      progress = { animatedRatio },
      modifier = Modifier
        .fillMaxWidth()
        .height(6.dp)
        .clip(RoundedCornerShape(3.dp)),
      color = color,
      trackColor = color.copy(alpha = 0.2f),
      strokeCap = StrokeCap.Round
    )
  }
}

@Composable
fun CalorieGaugeCard(
  consumidas: Int,
  metaCalorias: Int,
  proteinasTotal: Double,
  carbosTotal: Double,
  grasasTotal: Double,
  metaProteMin: Int,
  metaCarbos: Int,
  metaGrasas: Int,
  modifier: Modifier = Modifier
) {
  val restantes = (metaCalorias - consumidas).coerceAtLeast(0)
  val ratio = if (metaCalorias > 0) (consumidas.toFloat() / metaCalorias.toFloat()).coerceIn(0f, 1f) else 0f
  val animatedProgress by animateFloatAsState(targetValue = ratio, animationSpec = tween(600), label = "calProgress")

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 2.dp
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Left: Texts
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Calorías Diarias",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$consumidas",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = " / $metaCalorias kcal",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (consumidas > metaCalorias) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
          ) {
            Text(
              text = if (consumidas > metaCalorias) {
                "⚠️ Superaste por ${consumidas - metaCalorias} kcal"
              } else {
                "⚡ Te restan $restantes kcal para tu meta"
              },
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = if (consumidas > metaCalorias) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right: Circular Gauge
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier
            .size(92.dp)
            .testTag("calorie_gauge")
        ) {
          CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(92.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 9.dp
          )
          CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(92.dp),
            color = if (consumidas > metaCalorias) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            strokeWidth = 9.dp,
            strokeCap = StrokeCap.Round
          )
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "${(ratio * 100).toInt()}%",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Meta",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // 3 Macro Bars
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        MacroProgressBar(
          label = "Proteínas",
          consumido = proteinasTotal,
          meta = metaProteMin,
          color = ProteinColor,
          modifier = Modifier.weight(1f)
        )
        MacroProgressBar(
          label = "Carbos",
          consumido = carbosTotal,
          meta = metaCarbos,
          color = CarbColor,
          modifier = Modifier.weight(1f)
        )
        MacroProgressBar(
          label = "Grasas",
          consumido = grasasTotal,
          meta = metaGrasas,
          color = FatColor,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
fun MacroBadge(
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = color.copy(alpha = 0.12f),
    modifier = modifier
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(color)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = "$label: $value",
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
        fontWeight = FontWeight.Medium,
        color = color
      )
    }
  }
}
