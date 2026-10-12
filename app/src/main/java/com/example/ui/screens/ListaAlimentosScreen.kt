package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.example.data.model.CategoriaAlimento
import com.example.data.model.MomentoComida
import com.example.data.model.OrigenAlimento
import com.example.ui.components.MacroBadge
import com.example.ui.components.NuevoAlimentoDialog
import com.example.ui.components.RegistrarComidaDialog
import com.example.ui.theme.CarbColor
import com.example.ui.theme.FatColor
import com.example.ui.theme.ProteinColor
import com.example.ui.viewmodel.NutricionViewModel

@Composable
fun ListaAlimentosScreen(
  viewModel: NutricionViewModel,
  modifier: Modifier = Modifier
) {
  val alimentos by viewModel.alimentosFiltrados.collectAsState()
  val query by viewModel.queryBusqueda.collectAsState()
  val categoriaActual by viewModel.filtroCategoria.collectAsState()
  val origenActual by viewModel.filtroOrigen.collectAsState()
  val todosLosAlimentos by viewModel.alimentosCatalogo.collectAsState()

  var showNuevoAlimentoDialog by remember { mutableStateOf(false) }
  var alimentoSeleccionadoParaRegistrar by remember { mutableStateOf<Alimento?>(null) }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showNuevoAlimentoDialog = true },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag("nuevo_alimento_fab")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Agregar Alimento")
      }
    },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      // Title & Subtitle
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
          Text("🥗", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Alimentos Dominicanos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Base de datos nutricional de comida criolla y local",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Search Bar
      OutlinedTextField(
        value = query,
        onValueChange = { viewModel.setQueryBusqueda(it) },
        placeholder = { Text("Buscar mangú, arroz, habichuelas, aguacate...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
          if (query.isNotEmpty()) {
            IconButton(onClick = { viewModel.setQueryBusqueda("") }) {
              Icon(Icons.Default.Close, contentDescription = "Limpiar")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("catalogo_busqueda_input")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Category chips horizontal scroll
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        CategoriaAlimento.entries.forEach { cat ->
          val isSelected = (cat == CategoriaAlimento.TODOS && categoriaActual == "Todos") ||
              categoriaActual.equals(cat.label, ignoreCase = true)

          FilterChip(
            selected = isSelected,
            onClick = {
              viewModel.setFiltroCategoria(if (cat == CategoriaAlimento.TODOS) "Todos" else cat.label)
            },
            label = { Text("${cat.emoji} ${cat.label}") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier.testTag("categoria_chip_${cat.name.lowercase()}")
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Origin filter row (Local RD vs Importado)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = origenActual == null,
          onClick = { viewModel.setFiltroOrigen(null) },
          label = { Text("Todos los orígenes") },
          modifier = Modifier.weight(1f)
        )
        FilterChip(
          selected = origenActual == OrigenAlimento.LOCAL,
          onClick = { viewModel.setFiltroOrigen(if (origenActual == OrigenAlimento.LOCAL) null else OrigenAlimento.LOCAL) },
          label = { Text("🇩🇴 Local RD") },
          modifier = Modifier.weight(1f)
        )
        FilterChip(
          selected = origenActual == OrigenAlimento.IMPORTADO,
          onClick = { viewModel.setFiltroOrigen(if (origenActual == OrigenAlimento.IMPORTADO) null else OrigenAlimento.IMPORTADO) },
          label = { Text("🌎 Importado") },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Count badge
      Text(
        text = "Mostrando ${alimentos.size} alimentos",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp)
      )

      // Food items list
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(alimentos, key = { it.id }) { alimento ->
          AlimentoItemCard(
            alimento = alimento,
            onRegistrarClick = { alimentoSeleccionadoParaRegistrar = alimento }
          )
        }

        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }

  // Dialog to register the selected food
  if (alimentoSeleccionadoParaRegistrar != null) {
    RegistrarComidaDialog(
      alimentosDisponibles = todosLosAlimentos,
      momentoInicial = MomentoComida.ALMUERZO,
      alimentoPreseleccionado = alimentoSeleccionadoParaRegistrar,
      onDismiss = { alimentoSeleccionadoParaRegistrar = null },
      onConfirmar = { alimentoId, cantidad, momento ->
        viewModel.registrarComida(alimentoId, cantidad, momento)
        alimentoSeleccionadoParaRegistrar = null
      }
    )
  }

  // Dialog to add custom food
  if (showNuevoAlimentoDialog) {
    NuevoAlimentoDialog(
      onDismiss = { showNuevoAlimentoDialog = false },
      onGuardar = { nuevoAlimento ->
        viewModel.agregarNuevoAlimento(nuevoAlimento)
        showNuevoAlimentoDialog = false
      }
    )
  }
}

@Composable
fun AlimentoItemCard(
  alimento: Alimento,
  onRegistrarClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = alimento.origen.flag, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = alimento.nombre,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = "${alimento.porcion} • ${alimento.categoria}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.primaryContainer,
          modifier = Modifier.padding(start = 8.dp)
        ) {
          Text(
            text = "${alimento.calorias} kcal",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Macros and action button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          MacroBadge("P", "${alimento.proteinas}g", ProteinColor)
          MacroBadge("C", "${alimento.carbohidratos}g", CarbColor)
          MacroBadge("G", "${alimento.grasas}g", FatColor)
        }

        FilledTonalButton(
          onClick = onRegistrarClick,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("registrar_alimento_${alimento.id}")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("+ Registrar", style = MaterialTheme.typography.labelMedium)
        }
      }
    }
  }
}
