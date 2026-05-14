package com.example.grameenlight.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.data.model.Pole
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.awaitClose
import kotlin.random.Random

class MapViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _allPoles = MutableStateFlow<List<Pole>>(emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilters = MutableStateFlow(setOf("Working", "Pending", "Assigned", "Faulty"))
    val selectedFilters: StateFlow<Set<String>> = _selectedFilters.asStateFlow()

    val filteredPoles: StateFlow<List<Pole>> = combine(_allPoles, _searchQuery, _selectedFilters) { poles, query, filters ->
        poles.filter { pole ->
            val matchesSearch = pole.poleId.contains(query, ignoreCase = true)
            val matchesFilter = filters.contains(pole.status)
            matchesSearch && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _reports = MutableStateFlow<List<ElectricityReport>>(emptyList())
    val reports: StateFlow<List<ElectricityReport>> = _reports.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFilter(filter: String) {
        val current = _selectedFilters.value.toMutableSet()
        if (current.contains(filter)) {
            current.remove(filter)
        } else {
            current.add(filter)
        }
        _selectedFilters.value = current
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Listen to Poles
            db.collection("poles")
                .addSnapshotListener { snap, error ->
                    if (error == null) {
                        val list = snap?.toObjects(Pole::class.java) ?: emptyList()
                        if (list.isEmpty()) {
                            _allPoles.value = generateDummyPoles()
                        } else {
                            _allPoles.value = list
                        }
                    } else {
                        _allPoles.value = generateDummyPoles()
                    }
                }

            // Listen to Reports
            db.collection("reports")
                .addSnapshotListener { snap, error ->
                    if (error == null) {
                        _reports.value = snap?.toObjects(ElectricityReport::class.java) ?: emptyList()
                    }
                }
            
            _isLoading.value = false
        }
    }

    private fun generateDummyPoles(): List<Pole> {
        val poles = mutableListOf<Pole>()
        val center = kotlin.Pair(13.3291, 76.8342) // Keri Ambalaga area
        val streets = listOf("Main St", "Temple Rd", "School Lane", "Agri Link", "Market Rd")
        val statuses = listOf("Working", "Pending", "Assigned", "Faulty")
        val types = listOf("Village Use", "Agriculture Use", "Street Light", "Transformer", "Water Supply")

        for (i in 1..200) {
            poles.add(
                Pole(
                    poleId = "KA-KERI-${1000 + i}",
                    latitude = center.first + Random.nextDouble(-0.005, 0.005),
                    longitude = center.second + Random.nextDouble(-0.005, 0.005),
                    status = statuses.random(),
                    type = types.random(),
                    village = "Keri Ambalaga",
                    panchayat = "Keri",
                    streetName = streets.random(),
                    installYear = Random.nextInt(2015, 2024)
                )
            )
        }
        return poles
    }
}
