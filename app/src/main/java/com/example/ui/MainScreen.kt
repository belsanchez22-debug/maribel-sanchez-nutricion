package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.DinnerDining
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.screens.ListaAlimentosScreen
import com.example.ui.screens.PerfilScreen
import com.example.ui.screens.ProgresoScreen
import com.example.ui.screens.RecetasScreen
import com.example.ui.screens.RegistroComidaScreen
import com.example.ui.viewmodel.NutricionViewModel

enum class NavigationTab(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  DIARIO("Diario", Icons.Filled.Restaurant, Icons.Outlined.Restaurant, "tab_diario"),
  ALIMENTOS("Alimentos", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_alimentos"),
  RECETAS("Recetas RD", Icons.Filled.DinnerDining, Icons.Outlined.DinnerDining, "tab_recetas"),
  PROGRESO("Progreso", Icons.Filled.BarChart, Icons.Outlined.BarChart, "tab_progreso"),
  PERFIL("Perfil", Icons.Filled.Person, Icons.Outlined.Person, "tab_perfil")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  viewModel: NutricionViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  val snackbarHostState = remember { SnackbarHostState() }
  val snackbarMsg by viewModel.mensajeSnackbar.collectAsState()

  // Handle back button: if on sub-tab, return to home Diario tab
  BackHandler(enabled = selectedTab != 0) {
    selectedTab = 0
  }

  LaunchedEffect(snackbarMsg) {
    snackbarMsg?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.limpiarMensajeSnackbar()
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "🥗 Nutrición RD",
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleLarge
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
          titleContentColor = MaterialTheme.colorScheme.primary
        )
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
      ) {
        NavigationTab.entries.forEachIndexed { index, tab ->
          val isSelected = selectedTab == index
          NavigationBarItem(
            selected = isSelected,
            onClick = { selectedTab = index },
            icon = {
              Icon(
                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                contentDescription = tab.title
              )
            },
            label = {
              Text(
                text = tab.title,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
              selectedTextColor = MaterialTheme.colorScheme.primary,
              indicatorColor = MaterialTheme.colorScheme.primaryContainer,
              unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
              unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.testTag(tab.testTag)
          )
        }
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) },
    modifier = modifier.fillMaxSize()
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      Crossfade(targetState = selectedTab, label = "tab_fade") { tabIndex ->
        when (tabIndex) {
          0 -> RegistroComidaScreen(
            viewModel = viewModel,
            onNavigateToCatalog = { selectedTab = 1 }
          )
          1 -> ListaAlimentosScreen(viewModel = viewModel)
          2 -> RecetasScreen(viewModel = viewModel)
          3 -> ProgresoScreen(viewModel = viewModel)
          4 -> PerfilScreen(viewModel = viewModel)
        }
      }
    }
  }
}
