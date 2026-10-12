package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RecordatoriosCard
import com.example.ui.components.RespaldosCard
import com.example.data.model.Genero
import com.example.data.model.Meta
import com.example.data.model.NivelActividad
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutricionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
  viewModel: NutricionViewModel,
  modifier: Modifier = Modifier
) {
  val perfil by viewModel.perfilDraft.collectAsState()
  val metas by viewModel.metasCalculadas.collectAsState()
  val snackbarMsg by viewModel.mensajeSnackbar.collectAsState()

  val scrollState = rememberScrollState()

  var actividadExpanded by remember { mutableStateOf(false) }
  var metaExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // Header
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Text("📋", fontSize = 22.sp)
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "Tu Perfil Nutricional",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Cálculo de metabolismo y requerimientos calóricos",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Main Form Card
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Nombre
        OutlinedTextField(
          value = perfil.nombre,
          onValueChange = { nuevoNombre ->
            viewModel.updatePerfilDraft { it.copy(nombre = nuevoNombre) }
          },
          label = { Text("Nombre") },
          placeholder = { Text("Tu nombre o apodo") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("perfil_nombre_input")
        )

        // Edad y Género
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedTextField(
            value = if (perfil.edad > 0) perfil.edad.toString() else "",
            onValueChange = { valor ->
              val edadNum = valor.filter { it.isDigit() }.toIntOrNull() ?: 0
              viewModel.updatePerfilDraft { it.copy(edad = edadNum) }
            },
            label = { Text("Edad (años)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("perfil_edad_input")
          )

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Género",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              FilterChip(
                selected = perfil.genero == Genero.FEMENINO,
                onClick = { viewModel.updatePerfilDraft { it.copy(genero = Genero.FEMENINO) } },
                label = { Text("Mujer", style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                  .weight(1f)
                  .testTag("perfil_genero_femenino")
              )
              FilterChip(
                selected = perfil.genero == Genero.MASCULINO,
                onClick = { viewModel.updatePerfilDraft { it.copy(genero = Genero.MASCULINO) } },
                label = { Text("Hombre", style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                  .weight(1f)
                  .testTag("perfil_genero_masculino")
              )
            }
          }
        }

        // Peso y Altura
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedTextField(
            value = if (perfil.peso > 0.0) perfil.peso.toString() else "",
            onValueChange = { valor ->
              val pesoNum = valor.toDoubleOrNull() ?: 0.0
              viewModel.updatePerfilDraft { it.copy(peso = pesoNum) }
            },
            label = { Text("Peso (kg)") },
            placeholder = { Text("Ej. 70") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("perfil_peso_input")
          )

          OutlinedTextField(
            value = if (perfil.altura > 0.0) perfil.altura.toInt().toString() else "",
            onValueChange = { valor ->
              val altNum = valor.filter { it.isDigit() }.toDoubleOrNull() ?: 0.0
              viewModel.updatePerfilDraft { it.copy(altura = altNum) }
            },
            label = { Text("Altura (cm)") },
            placeholder = { Text("Ej. 170") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("perfil_altura_input")
          )
        }

        // Nivel de Actividad Dropdown
        ExposedDropdownMenuBox(
          expanded = actividadExpanded,
          onExpandedChange = { actividadExpanded = it }
        ) {
          OutlinedTextField(
            value = "${perfil.nivelActividad.label} (${perfil.nivelActividad.descripcion})",
            onValueChange = {},
            readOnly = true,
            label = { Text("Nivel de Actividad") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = actividadExpanded) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
              .testTag("perfil_actividad_dropdown")
          )
          ExposedDropdownMenu(
            expanded = actividadExpanded,
            onDismissRequest = { actividadExpanded = false }
          ) {
            NivelActividad.entries.forEach { nivel ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(nivel.label, fontWeight = FontWeight.Bold)
                    Text(
                      nivel.descripcion,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                },
                onClick = {
                  viewModel.updatePerfilDraft { it.copy(nivelActividad = nivel) }
                  actividadExpanded = false
                }
              )
            }
          }
        }

        // Meta Dropdown
        ExposedDropdownMenuBox(
          expanded = metaExpanded,
          onExpandedChange = { metaExpanded = it }
        ) {
          OutlinedTextField(
            value = "${perfil.meta.emoji} ${perfil.meta.label} - ${perfil.meta.descripcion}",
            onValueChange = {},
            readOnly = true,
            label = { Text("Tu Meta Nutricional") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = metaExpanded) },
            modifier = Modifier
              .menuAnchor(MenuAnchorType.PrimaryNotEditable)
              .fillMaxWidth()
              .testTag("perfil_meta_dropdown")
          )
          ExposedDropdownMenu(
            expanded = metaExpanded,
            onDismissRequest = { metaExpanded = false }
          ) {
            Meta.entries.forEach { metaItem ->
              DropdownMenuItem(
                text = {
                  Text("${metaItem.emoji} ${metaItem.label} (${metaItem.descripcion})")
                },
                onClick = {
                  viewModel.updatePerfilDraft { it.copy(meta = metaItem) }
                  metaExpanded = false
                }
              )
            }
          }
        }

        // Meta de Agua Diaria (ml)
        OutlinedTextField(
          value = if (perfil.metaAguaDiaria > 0) perfil.metaAguaDiaria.toString() else "",
          onValueChange = { valor ->
            val ml = valor.filter { it.isDigit() }.toIntOrNull() ?: 2000
            viewModel.updatePerfilDraft { it.copy(metaAguaDiaria = ml) }
          },
          label = { Text("💧 Meta de Agua Diaria (ml)") },
          placeholder = { Text("Ej. 2000 ml = 8 vasos") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("perfil_meta_agua_input")
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Results Card (Matching and expanding user's Perfil.tsx card with gradient)
    AnimatedVisibility(
      visible = perfil.peso > 0 && perfil.altura > 0 && perfil.edad > 0,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("perfil_resultados_card")
      ) {
        Box(
          modifier = Modifier
            .background(
              brush = Brush.linearGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
              ),
              shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
        ) {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "🎯 Tus Metas Diarias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.2f)
              ) {
                Text(
                  text = "IMC: ${metas.imc} • ${metas.categoriaImc}",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color.White,
                  fontWeight = FontWeight.SemiBold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Calorie Recommendation
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = "${metas.caloriasDiarias}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
              )
              Text(
                text = " kcal / día",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Gasto basal y Gasto total
            Text(
              text = "Gasto basal (GEB): ${metas.geb} kcal  |  Gasto total (GET): ${metas.get} kcal",
              style = MaterialTheme.typography.bodySmall,
              color = Color.White.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Macronutrient Targets
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.White.copy(alpha = 0.15f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "Distribución Recomendada:",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "🍗 Proteínas: ${metas.proteMin} - ${metas.proteMax} g/día",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                  )
                  Text(
                    text = "🌾 Carbos: ~${metas.carboGramos} g",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                  )
                  Text(
                    text = "🥑 Grasas: ~${metas.grasaGramos} g",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                  )
                }
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Save Button
    Button(
      onClick = { viewModel.guardarPerfil() },
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("guardar_perfil_btn")
    ) {
      Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "💾 Guardar Mi Perfil",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Recordatorios de Horarios y Hábitos
    RecordatoriosCard()

    Spacer(modifier = Modifier.height(16.dp))

    // Copia de Respaldo y Zona de Reinicio
    RespaldosCard(
      perfil = perfil,
      onReiniciarTodo = {
        viewModel.reiniciarAgua()
      }
    )

    Spacer(modifier = Modifier.height(30.dp))
  }
}
