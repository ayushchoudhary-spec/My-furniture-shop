package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "furniture")
data class FurnitureItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val name: String,
  val category: String,
  val basePrice: Double,
  val description: String,
  val dimensions: String,
  val inStock: Boolean = true,
  val imageUri: String,
  val availableWoodTypes: List<String> = listOf("Burmese Teak", "Indian Sheesham", "American Walnut", "White Oak"),
  val availableFinishes: List<String> = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain", "PU Scratch-Proof"),
  val availableFabrics: List<String> = listOf("Natural Linen", "Royal Emerald Velvet", "Cognac Leatherette", "Terracotta Weave"),
  val availableSizes: List<String> = listOf("Standard", "Compact", "Deluxe", "Grand"),
  val rating: Float = 4.8f,
  val isCustomizable: Boolean = true,
  val dateAdded: Long = System.currentTimeMillis()
) : Serializable

data class CustomizationSelection(
  val woodType: String = "Burmese Teak",
  val finish: String = "Natural Matte",
  val fabric: String = "Natural Linen",
  val size: String = "Standard"
) {
  fun calculatePrice(basePrice: Double): Double {
    val woodExtra = when (woodType) {
      "Indian Sheesham" -> 1500.0
      "American Walnut" -> 3800.0
      "White Oak" -> 2800.0
      "White Ash" -> 1200.0
      "Reclaimed Sal Wood" -> 800.0
      else -> 0.0 // Burmese Teak / Standard
    }

    val finishExtra = when (finish) {
      "Teak Honey Gloss" -> 850.0
      "Dark Walnut Stain" -> 1400.0
      "PU Scratch-Proof" -> 2200.0
      "Vintage Distressed" -> 1600.0
      else -> 0.0 // Natural Matte
    }

    val fabricExtra = when (fabric) {
      "Royal Emerald Velvet" -> 1800.0
      "Cognac Leatherette" -> 3200.0
      "Midnight Blue Fabric" -> 1400.0
      "Terracotta Weave" -> 1100.0
      else -> 0.0 // Natural Linen / None
    }

    val sizeMultiplier = when (size) {
      "Compact" -> 0.85
      "Deluxe" -> 1.25
      "Grand" -> 1.40
      else -> 1.0 // Standard
    }

    return (basePrice + woodExtra + finishExtra + fabricExtra) * sizeMultiplier
  }
}

data class QuoteItem(
  val id: String = java.util.UUID.randomUUID().toString(),
  val furnitureId: Long,
  val name: String,
  val category: String,
  val imageUri: String,
  val customization: CustomizationSelection,
  val unitPrice: Double,
  val quantity: Int = 1
)
