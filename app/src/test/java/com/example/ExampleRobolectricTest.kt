package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.FurnitureRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Arzoo Timber & Furniture", appName)
  }

  @Test
  fun `search furniture items by name matches correctly`() {
    val items = FurnitureRepository.initialCatalogueItems
    val query = "chesterfield"
    val results = items.filter { item ->
      item.name.lowercase().contains(query) ||
        item.category.lowercase().contains(query) ||
        item.description.lowercase().contains(query) ||
        item.availableWoodTypes.any { it.lowercase().contains(query) }
    }
    assertTrue("Chesterfield search should return at least 1 item", results.isNotEmpty())
    assertTrue(results.all { it.name.contains("Chesterfield", ignoreCase = true) })
  }

  @Test
  fun `search furniture items by category matches correctly`() {
    val items = FurnitureRepository.initialCatalogueItems
    val query = "dining"
    val results = items.filter { item ->
      item.name.lowercase().contains(query) ||
        item.category.lowercase().contains(query) ||
        item.description.lowercase().contains(query) ||
        item.availableWoodTypes.any { it.lowercase().contains(query) }
    }
    assertTrue("Dining category search should return matching items", results.isNotEmpty())
    assertTrue(results.any { it.category.contains("Dining", ignoreCase = true) })
  }
}
