package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.LocalImageManager
import com.example.ui.FurnitureViewModel
import com.example.ui.SortOption
import com.example.ui.components.AddEditItemDialog
import com.example.ui.components.CustomizationSheet
import com.example.ui.components.EstimateQuoteDialog
import com.example.ui.components.FurnitureCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogueScreen(
  viewModel: FurnitureViewModel,
  modifier: Modifier = Modifier
) {
  val items by viewModel.displayedItems.collectAsStateWithLifecycle()
  val totalCount by viewModel.itemCount.collectAsStateWithLifecycle()
  val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
  val currentSort by viewModel.sortOption.collectAsStateWithLifecycle()
  val activeItem by viewModel.activeCustomizationItem.collectAsStateWithLifecycle()
  val itemForEdit by viewModel.itemForEdit.collectAsStateWithLifecycle()
  val isAddEditOpen by viewModel.isAddEditOpen.collectAsStateWithLifecycle()
  val isQuoteOpen by viewModel.isQuoteDialogOpen.collectAsStateWithLifecycle()
  val quoteItems by viewModel.quoteItems.collectAsStateWithLifecycle()

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  var sortMenuExpanded by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current

  val categories = listOf(
    "All",
    "Living Room",
    "Bedroom",
    "Dining",
    "Timber & Slabs",
    "Office & Study",
    "Outdoor & Balcony",
    "Pooja & Mandir"
  )

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .windowInsetsPadding(WindowInsets.statusBars),
    topBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface)
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        // App Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "ARZOO TIMBER & FURNITURE",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              ),
              color = MaterialTheme.colorScheme.primary
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(12.dp)
              )
              Text(
                text = "100% Offline Catalogue • Max 200 Items",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.secondary
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            // Reset Catalogue Menu Icon
            IconButton(onClick = { viewModel.resetCatalogue() }) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Restore Default Catalogue",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            // Quote Cart Icon with Badge
            IconButton(
              onClick = { viewModel.openQuoteDialog() },
              modifier = Modifier.testTag("quote_button")
            ) {
              BadgedBox(
                badge = {
                  if (quoteItems.isNotEmpty()) {
                    Badge(
                      containerColor = MaterialTheme.colorScheme.secondary,
                      contentColor = Color.White
                    ) {
                      Text("${quoteItems.size}")
                    }
                  }
                }
              ) {
                Icon(
                  imageVector = Icons.Default.ReceiptLong,
                  contentDescription = "Estimate Quotes",
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar & Sort Menu
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = {
              Text(
                text = "Search by name or category...",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search catalogue",
                tint = MaterialTheme.colorScheme.primary
              )
            },
            trailingIcon = {
              if (searchQuery.isNotBlank()) {
                IconButton(
                  onClick = {
                    viewModel.setSearchQuery("")
                    focusManager.clearFocus()
                  },
                  modifier = Modifier.testTag("clear_search_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = MaterialTheme.colorScheme.primary,
              unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("search_input")
          )

          // Sort Button with dropdown
          Box {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .clickable { sortMenuExpanded = true }
                .testTag("sort_filter_button")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  Icons.Default.FilterList,
                  contentDescription = "Sort",
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(18.dp)
                )
                Text(
                  text = "Sort",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            DropdownMenu(
              expanded = sortMenuExpanded,
              onDismissRequest = { sortMenuExpanded = false }
            ) {
              SortOption.values().forEach { option ->
                DropdownMenuItem(
                  text = {
                    Text(
                      text = option.displayName,
                      fontWeight = if (currentSort == option) FontWeight.Bold else FontWeight.Normal,
                      color = if (currentSort == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                  },
                  onClick = {
                    viewModel.setSortOption(option)
                    sortMenuExpanded = false
                  }
                )
              }
            }
          }
        }

        // Active Search Filter Indicator
        AnimatedVisibility(
          visible = searchQuery.isNotBlank(),
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
              .testTag("search_status_banner")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "Showing ${items.size} item${if (items.size != 1) "s" else ""} for \"$searchQuery\"",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
              Text(
                text = "Clear",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .clickable {
                    viewModel.setSearchQuery("")
                    focusManager.clearFocus()
                  }
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Category Pills
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .testTag("category_pill_$category")
                .clickable { viewModel.setCategory(category) }
            ) {
              Text(
                text = category,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Storage Capacity Banner: X / 200 images
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "Catalogue Capacity: $totalCount / ${LocalImageManager.MAX_CATALOGUE_CAPACITY} items loaded",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }

            Text(
              text = "${LocalImageManager.MAX_CATALOGUE_CAPACITY - totalCount} slots available",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = MaterialTheme.colorScheme.secondary
            )
          }
        }
      }
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { viewModel.openAddItem() },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(16.dp),
        icon = { Icon(Icons.Default.Add, contentDescription = null) },
        text = {
          Text(
            text = "Add Item ($totalCount/200)",
            fontWeight = FontWeight.Bold
          )
        },
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("add_furniture_fab")
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.background)
    ) {
      if (items.isEmpty()) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(32.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = if (searchQuery.isNotBlank()) "No Matching Furniture Found" else "No Furniture Items Found",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = if (searchQuery.isNotBlank()) {
              "We couldn't find any items matching \"$searchQuery\". Try searching by another furniture name or category (e.g. Sofa, Dining, Bed, Office)."
            } else {
              "This category has no items yet."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
          Spacer(modifier = Modifier.height(20.dp))
          if (searchQuery.isNotBlank()) {
            Button(
              onClick = {
                viewModel.setSearchQuery("")
                focusManager.clearFocus()
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              modifier = Modifier.testTag("empty_clear_search_button")
            ) {
              Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Clear Search")
            }
          } else {
            TextButton(
              onClick = {
                viewModel.setSearchQuery("")
                viewModel.setCategory("All")
              }
            ) {
              Text("Reset Filters")
            }
          }
        }
      } else {
        LazyVerticalGrid(
          columns = GridCells.Adaptive(minSize = 300.dp),
          contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 88.dp),
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(items, key = { it.id }) { furniture ->
            FurnitureCard(
              item = furniture,
              onClick = { viewModel.openCustomization(furniture) },
              onCustomizeClick = { viewModel.openCustomization(furniture) }
            )
          }
        }
      }

      // Bottom Sheet: Customization & Realtime Pricing Breakdown
      if (activeItem != null) {
        CustomizationSheet(
          item = activeItem!!,
          sheetState = sheetState,
          onDismiss = { viewModel.closeCustomization() },
          onAddToQuote = { selection, qty, unitPrice ->
            viewModel.addToQuote(activeItem!!, selection, qty, unitPrice)
          },
          onDeleteItem = { item ->
            viewModel.deleteItem(item)
          },
          onEditItem = { item ->
            viewModel.closeCustomization()
            viewModel.openEditItem(item)
          }
        )
      }

      // Dialog: Add or Edit Furniture Item
      if (isAddEditOpen) {
        AddEditItemDialog(
          existingItem = itemForEdit,
          currentCount = totalCount,
          onDismiss = { viewModel.closeAddEdit() },
          onSave = { savedItem ->
            viewModel.saveItem(savedItem)
          }
        )
      }

      // Dialog: Estimate / Bill Quote Summary
      if (isQuoteOpen) {
        EstimateQuoteDialog(
          quoteItems = quoteItems,
          onDismiss = { viewModel.closeQuoteDialog() },
          onUpdateQuantity = { id, qty -> viewModel.updateQuoteQuantity(id, qty) },
          onRemoveItem = { id -> viewModel.removeFromQuote(id) },
          onClearAll = { viewModel.clearQuote() }
        )
      }
    }
  }
}
