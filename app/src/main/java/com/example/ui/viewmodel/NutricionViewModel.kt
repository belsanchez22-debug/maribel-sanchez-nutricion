package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.NutricionDatabase
import com.example.data.model.Alimento
import com.example.data.model.ComidaRegistrada
import com.example.data.model.Genero
import com.example.data.model.Meta
import com.example.data.model.MetasCalculadas
import com.example.data.model.MomentoComida
import com.example.data.model.NivelActividad
import com.example.data.model.OrigenAlimento
import com.example.data.model.PerfilUsuario
import com.example.data.model.RegistroAgua
import com.example.data.model.RegistroPeso
import com.example.data.repository.NutricionRepository
import com.example.domain.utils.CalculosNutricion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NutricionViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: NutricionRepository

  private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  private val displayDateFormat = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "DO"))

  private val _fechaSeleccionada = MutableStateFlow(obtenerFechaHoy())
  val fechaSeleccionada: StateFlow<String> = _fechaSeleccionada.asStateFlow()

  private val _perfilDraft = MutableStateFlow(
    PerfilUsuario(
      nombre = "María Sánchez",
      edad = 28,
      peso = 68.0,
      altura = 165.0,
      genero = Genero.FEMENINO,
      nivelActividad = NivelActividad.MEDIO,
      meta = Meta.MANTENER,
      metaAguaDiaria = 2000
    )
  )
  val perfilDraft: StateFlow<PerfilUsuario> = _perfilDraft.asStateFlow()

  private val _mensajeSnackbar = MutableStateFlow<String?>(null)
  val mensajeSnackbar: StateFlow<String?> = _mensajeSnackbar.asStateFlow()

  private val _queryBusqueda = MutableStateFlow("")
  val queryBusqueda: StateFlow<String> = _queryBusqueda.asStateFlow()

  private val _filtroCategoria = MutableStateFlow("Todos")
  val filtroCategoria: StateFlow<String> = _filtroCategoria.asStateFlow()

  private val _filtroOrigen = MutableStateFlow<OrigenAlimento?>(null)
  val filtroOrigen: StateFlow<OrigenAlimento?> = _filtroOrigen.asStateFlow()

  val alimentosCatalogo: StateFlow<List<Alimento>>

  val alimentosFiltrados: StateFlow<List<Alimento>>

  val perfilGuardado: StateFlow<PerfilUsuario?>

  val metasCalculadas: StateFlow<MetasCalculadas>

  val comidasDelDia: StateFlow<List<ComidaRegistrada>>

  val todasLasComidas: StateFlow<List<ComidaRegistrada>>

  // Control de Agua
  private val _registroAgua = MutableStateFlow(RegistroAgua(obtenerFechaHoy()))
  val registroAgua: StateFlow<RegistroAgua> = _registroAgua.asStateFlow()

  // Control de Peso
  val historialPeso: StateFlow<List<RegistroPeso>>

  init {
    val db = NutricionDatabase.getDatabase(application)
    repository = NutricionRepository(db.nutricionDao())

    viewModelScope.launch {
      repository.preseedAlimentosSiEsNecesario()
    }

    perfilGuardado = repository.perfilFlow.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )

    historialPeso = repository.historialPesoFlow.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    alimentosCatalogo = repository.alimentosFlow.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    alimentosFiltrados = combine(
      alimentosCatalogo,
      _queryBusqueda,
      _filtroCategoria,
      _filtroOrigen
    ) { lista, query, cat, origen ->
      lista.filter { alimento ->
        val coincideQuery = query.isBlank() ||
            alimento.nombre.contains(query, ignoreCase = true) ||
            alimento.categoria.contains(query, ignoreCase = true)

        val coincideCat = cat == "Todos" || alimento.categoria.equals(cat, ignoreCase = true)

        val coincideOrigen = origen == null || alimento.origen == origen

        coincideQuery && coincideCat && coincideOrigen
      }
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    // Sincronizar perfil con draft inicial si existe en base de datos
    viewModelScope.launch {
      repository.perfilFlow.collect { guardado ->
        if (guardado != null) {
          _perfilDraft.value = guardado
        }
      }
    }

    metasCalculadas = combine(_perfilDraft) { (perfil) ->
      CalculosNutricion.calcularMetasCompletas(perfil)
    }.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      CalculosNutricion.calcularMetasCompletas(_perfilDraft.value)
    )

    // Comidas para la fecha seleccionada
    val _comidasFlow = MutableStateFlow<List<ComidaRegistrada>>(emptyList())
    comidasDelDia = _comidasFlow.asStateFlow()

    viewModelScope.launch {
      _fechaSeleccionada.collect { fecha ->
        repository.getComidasPorFecha(fecha).collect { list ->
          _comidasFlow.value = list
        }
      }
    }

    // Monitorear consumo de agua para la fecha seleccionada
    viewModelScope.launch {
      _fechaSeleccionada.collect { fecha ->
        val metaAgua = _perfilDraft.value.metaAguaDiaria
        repository.getAguaPorFecha(fecha, metaAgua).collect { reg ->
          _registroAgua.value = reg
        }
      }
    }

    todasLasComidas = repository.getAllComidas().stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )
  }

  fun updatePerfilDraft(transform: (PerfilUsuario) -> PerfilUsuario) {
    _perfilDraft.value = transform(_perfilDraft.value)
  }

  fun guardarPerfil() {
    val perfil = _perfilDraft.value
    if (perfil.nombre.isBlank()) {
      _mensajeSnackbar.value = "Por favor ingresa tu nombre"
      return
    }
    if (perfil.edad <= 0 || perfil.peso <= 0 || perfil.altura <= 0) {
      _mensajeSnackbar.value = "Por favor ingresa valores válidos de edad, peso y altura"
      return
    }

    viewModelScope.launch {
      repository.guardarPerfil(perfil)
      // Actualizar también la meta en el registro de agua actual
      repository.guardarAgua(_fechaSeleccionada.value, _registroAgua.value.cantidadMl, perfil.metaAguaDiaria)
      _mensajeSnackbar.value = "✅ ¡Perfil nutricional guardado exitosamente!"
    }
  }

  // Métodos de Control de Agua
  fun agregarAgua(ml: Int) {
    viewModelScope.launch {
      val actual = _registroAgua.value
      val nuevaCantidad = (actual.cantidadMl + ml).coerceAtLeast(0)
      repository.guardarAgua(_fechaSeleccionada.value, nuevaCantidad, actual.metaMl)
      _mensajeSnackbar.value = "💧 +${ml}ml de agua registrados"
    }
  }

  fun reiniciarAgua() {
    viewModelScope.launch {
      val actual = _registroAgua.value
      repository.guardarAgua(_fechaSeleccionada.value, 0, actual.metaMl)
      _mensajeSnackbar.value = "Contador de agua reiniciado"
    }
  }

  fun actualizarMetaAgua(nuevaMetaMl: Int) {
    viewModelScope.launch {
      val actual = _registroAgua.value
      repository.guardarAgua(_fechaSeleccionada.value, actual.cantidadMl, nuevaMetaMl)
      updatePerfilDraft { it.copy(metaAguaDiaria = nuevaMetaMl) }
      repository.guardarPerfil(_perfilDraft.value)
      _mensajeSnackbar.value = "Meta de agua actualizada a ${nuevaMetaMl}ml"
    }
  }

  // Métodos de Control de Peso
  fun registrarPeso(peso: Double, nota: String?) {
    if (peso < 20.0 || peso > 300.0) {
      _mensajeSnackbar.value = "Ingresa un peso válido entre 20 y 300 kg"
      return
    }
    viewModelScope.launch {
      repository.registrarPeso(_fechaSeleccionada.value, peso, nota)
      // Actualizar también el peso en el perfil actual para recalcular GEB y metas
      updatePerfilDraft { it.copy(peso = peso) }
      repository.guardarPerfil(_perfilDraft.value)
      _mensajeSnackbar.value = "⚖️ ¡Peso de ${peso} kg registrado!"
    }
  }

  fun eliminarPeso(id: Long) {
    viewModelScope.launch {
      repository.eliminarPeso(id)
      _mensajeSnackbar.value = "Registro de peso eliminado"
    }
  }

  fun setQueryBusqueda(query: String) {
    _queryBusqueda.value = query
  }

  fun setFiltroCategoria(categoria: String) {
    _filtroCategoria.value = categoria
  }

  fun setFiltroOrigen(origen: OrigenAlimento?) {
    _filtroOrigen.value = origen
  }

  fun cambiarFecha(nuevaFecha: String) {
    _fechaSeleccionada.value = nuevaFecha
  }

  fun irDiaAnterior() {
    desplazarDias(-1)
  }

  fun irDiaSiguiente() {
    desplazarDias(1)
  }

  fun irAHoy() {
    _fechaSeleccionada.value = obtenerFechaHoy()
  }

  private fun desplazarDias(dias: Int) {
    try {
      val parsed = dateFormat.parse(_fechaSeleccionada.value) ?: Date()
      val cal = Calendar.getInstance()
      cal.time = parsed
      cal.add(Calendar.DAY_OF_YEAR, dias)
      _fechaSeleccionada.value = dateFormat.format(cal.time)
    } catch (_: Exception) {
      _fechaSeleccionada.value = obtenerFechaHoy()
    }
  }

  fun formatFechaLegible(fechaStr: String): String {
    return try {
      val parsed = dateFormat.parse(fechaStr) ?: Date()
      val hoyStr = obtenerFechaHoy()
      val calHoy = Calendar.getInstance()
      val calAyer = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
      val ayerStr = dateFormat.format(calAyer.time)

      when (fechaStr) {
        hoyStr -> "Hoy, ${displayDateFormat.format(parsed).replaceFirstChar { it.uppercase() }}"
        ayerStr -> "Ayer, ${displayDateFormat.format(parsed).replaceFirstChar { it.uppercase() }}"
        else -> displayDateFormat.format(parsed).replaceFirstChar { it.uppercase() }
      }
    } catch (_: Exception) {
      fechaStr
    }
  }

  fun registrarComida(alimentoId: String, cantidad: Double, momento: MomentoComida) {
    viewModelScope.launch {
      repository.registrarComida(
        alimentoId = alimentoId,
        cantidad = cantidad,
        momento = momento,
        fecha = _fechaSeleccionada.value
      )
      _mensajeSnackbar.value = "Alimento añadido al ${momento.label.lowercase()}"
    }
  }

  fun eliminarComida(id: Long) {
    viewModelScope.launch {
      repository.eliminarComida(id)
      _mensajeSnackbar.value = "Alimento eliminado del registro"
    }
  }

  fun agregarNuevoAlimento(alimento: Alimento) {
    viewModelScope.launch {
      repository.agregarAlimento(alimento)
      _mensajeSnackbar.value = "¡Alimento \"${alimento.nombre}\" agregado al catálogo!"
    }
  }

  fun limpiarMensajeSnackbar() {
    _mensajeSnackbar.value = null
  }

  private fun obtenerFechaHoy(): String {
    return dateFormat.format(Date())
  }
}
