package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CustomizationSelection
import com.example.data.FurnitureItem
import com.example.data.FurnitureRepository
import com.example.data.LocalImageManager
import com.example.data.QuoteItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption(val displayName: String) {
  FEATURED("Featured"),
  PRICE_LOW_TO_HIGH("Price: Low to High"),
  PRICE_HIGH_TO_LOW("Price: High to Low"),
  HIGHEST_RATED("Top Rated"),
  NEWEST("Newest")
}

class FurnitureViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: FurnitureRepository

  init {
    val database = AppDatabase.getDatabase(application)
    repository = FurnitureRepository(database.furnitureDao())
    viewModelScope.launch {
      repository.seedInitialCatalogueIfEmpty()
    }
  }

  val itemCount: StateFlow<Int> = repository.itemCount
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery

  private val _sortOption = MutableStateFlow(SortOption.FEATURED)
  val sortOption: StateFlow<SortOption> = _sortOption

  // Dialog & Sheet States
  private val _activeCustomizationItem = MutableStateFlow<FurnitureItem?>(null)
  val activeCustomizationItem: StateFlow<FurnitureItem?> = _activeCustomizationItem

  private val _itemForEdit = MutableStateFlow<FurnitureItem?>(null)
  val itemForEdit: StateFlow<FurnitureItem?> = _itemForEdit

  private val _isAddEditOpen = MutableStateFlow(false)
  val isAddEditOpen: StateFlow<Boolean> = _isAddEditOpen

  private val _isQuoteDialogOpen = MutableStateFlow(false)
  val isQuoteDialogOpen: StateFlow<Boolean> = _isQuoteDialogOpen

  // Quote cart state
  private val _quoteItems = MutableStateFlow<List<QuoteItem>>(emptyList())
  val quoteItems: StateFlow<List<QuoteItem>> = _quoteItems

  // Filtered and Sorted Catalogue Flow
  val displayedItems: StateFlow<List<FurnitureItem>> = combine(
    repository.allItems,
    _selectedCategory,
    _searchQuery,
    _sortOption
  ) { items, category, query, sort ->
    var result = items

    // Filter by category and search query
    val cleanQuery = query.trim().lowercase()
    if (cleanQuery.isNotBlank()) {
      // Find items matching by name or category (or description/timber type)
      val nameOrCategoryMatches = items.filter { item ->
        item.name.lowercase().contains(cleanQuery) ||
          item.category.lowercase().contains(cleanQuery) ||
          item.description.lowercase().contains(cleanQuery) ||
          item.availableWoodTypes.any { it.lowercase().contains(cleanQuery) }
      }

      result = if (category != "All") {
        val matchesInCategory = nameOrCategoryMatches.filter {
          it.category.equals(category, ignoreCase = true)
        }
        // If matches found in current category, show them; otherwise show all global matches for seamless discovery
        if (matchesInCategory.isNotEmpty()) matchesInCategory else nameOrCategoryMatches
      } else {
        nameOrCategoryMatches
      }
    } else if (category != "All") {
      result = items.filter { it.category.equals(category, ignoreCase = true) }
    }

    // Sorting
    when (sort) {
      SortOption.PRICE_LOW_TO_HIGH -> result.sortedBy { it.basePrice }
      SortOption.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.basePrice }
      SortOption.HIGHEST_RATED -> result.sortedByDescending { it.rating }
      SortOption.NEWEST -> result.sortedByDescending { it.dateAdded }
      SortOption.FEATURED -> result
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun setCategory(category: String) {
    _selectedCategory.value = category
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSortOption(option: SortOption) {
    _sortOption.value = option
  }

  fun openCustomization(item: FurnitureItem) {
    _activeCustomizationItem.value = item
  }

  fun closeCustomization() {
    _activeCustomizationItem.value = null
  }

  fun openAddItem() {
    _itemForEdit.value = null
    _isAddEditOpen.value = true
  }

  fun openEditItem(item: FurnitureItem) {
    _itemForEdit.value = item
    _isAddEditOpen.value = true
  }

  fun closeAddEdit() {
    _itemForEdit.value = null
    _isAddEditOpen.value = false
  }

  fun openQuoteDialog() {
    _isQuoteDialogOpen.value = true
  }

  fun closeQuoteDialog() {
    _isQuoteDialogOpen.value = false
  }

  fun saveItem(item: FurnitureItem) {
    viewModelScope.launch {
      if (item.id == 0L) {
        // Enforce 200 limit check before inserting
        val count = repository.getCount()
        if (count < LocalImageManager.MAX_CATALOGUE_CAPACITY) {
          repository.insert(item)
        }
      } else {
        repository.update(item)
      }
    }
  }

  fun deleteItem(item: FurnitureItem) {
    viewModelScope.launch {
      LocalImageManager.deleteLocalImage(item.imageUri)
      repository.delete(item)
      if (_activeCustomizationItem.value?.id == item.id) {
        closeCustomization()
      }
    }
  }

  fun resetCatalogue() {
    viewModelScope.launch {
      repository.resetToDefaultCatalogue()
    }
  }

  fun addToQuote(
    item: FurnitureItem,
    selection: CustomizationSelection,
    quantity: Int,
    unitPrice: Double
  ) {
    val quoteItem = QuoteItem(
      furnitureId = item.id,
      name = item.name,
      category = item.category,
      imageUri = item.imageUri,
      customization = selection,
      unitPrice = unitPrice,
      quantity = quantity
    )
    _quoteItems.value = _quoteItems.value + quoteItem
  }

  fun updateQuoteQuantity(id: String, newQty: Int) {
    _quoteItems.value = _quoteItems.value.map {
      if (it.id == id) it.copy(quantity = newQty) else it
    }
  }

  fun removeFromQuote(id: String) {
    _quoteItems.value = _quoteItems.value.filterNot { it.id == id }
  }

  fun clearQuote() {
    _quoteItems.value = emptyList()
  }
}
