package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.FurnitureItem
import com.example.data.LocalImageManager
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditItemDialog(
  existingItem: FurnitureItem?,
  currentCount: Int,
  onDismiss: () -> Unit,
  onSave: (FurnitureItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  val isEditing = existingItem != null
  val isAtCapacity = !isEditing && currentCount >= LocalImageManager.MAX_CATALOGUE_CAPACITY

  var name by remember { mutableStateOf(existingItem?.name ?: "") }
  var category by remember { mutableStateOf(existingItem?.category ?: "Living Room") }
  var basePriceText by remember { mutableStateOf(existingItem?.basePrice?.toInt()?.toString() ?: "15000") }
  var description by remember { mutableStateOf(existingItem?.description ?: "") }
  var dimensions by remember { mutableStateOf(existingItem?.dimensions ?: "48\"W x 24\"D x 32\"H") }
  var imageUri by remember { mutableStateOf(existingItem?.imageUri ?: "preset_living_sofa") }
  var inStock by remember { mutableStateOf(existingItem?.inStock ?: true) }
  var isSavingImage by remember { mutableStateOf(false) }

  val defaultWoods = listOf("Burmese Teak", "Indian Sheesham", "American Walnut", "White Oak", "White Ash", "Reclaimed Sal Wood")
  var selectedWoods by remember {
    mutableStateOf(existingItem?.availableWoodTypes?.toSet() ?: setOf("Burmese Teak", "Indian Sheesham", "American Walnut"))
  }

  val defaultFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain", "PU Scratch-Proof")
  var selectedFinishes by remember {
    mutableStateOf(existingItem?.availableFinishes?.toSet() ?: setOf("Natural Matte", "Teak Honey Gloss"))
  }

  val allCategories = listOf(
    "Living Room",
    "Bedroom",
    "Dining",
    "Timber & Slabs",
    "Office & Study",
    "Outdoor & Balcony",
    "Pooja & Mandir"
  )

  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    if (uri != null) {
      coroutineScope.launch {
        isSavingImage = true
        val savedPath = LocalImageManager.saveImageFromUri(context, uri)
        if (savedPath != null) {
          imageUri = savedPath
        }
        isSavingImage = false
      }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .heightIn(max = 750.dp)
        .testTag("add_edit_item_dialog"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (isEditing) "EDIT FURNITURE ITEM" else "ADD NEW FURNITURE ITEM",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Offline Storage Capacity: $currentCount / ${LocalImageManager.MAX_CATALOGUE_CAPACITY} items",
              style = MaterialTheme.typography.labelSmall,
              color = if (isAtCapacity) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        // Capacity Progress Bar
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { currentCount.toFloat() / LocalImageManager.MAX_CATALOGUE_CAPACITY.toFloat() },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = if (isAtCapacity) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary,
          trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        if (isAtCapacity) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Maximum catalogue capacity of 200 items reached. Delete or edit existing items to insert more.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onErrorContainer,
              modifier = Modifier.padding(10.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Image Selection Area
        Text(
          text = "Furniture Photo (Offline Stored)",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
          contentAlignment = Alignment.Center
        ) {
          val isLocalFile = imageUri.startsWith("/") || imageUri.startsWith("file://")
          if (isLocalFile && File(imageUri.removePrefix("file://")).exists()) {
            AsyncImage(
              model = File(imageUri.removePrefix("file://")),
              contentDescription = "Selected Photo",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )
          } else {
            TimberArtIllustration(
              imageKey = imageUri,
              category = category,
              modifier = Modifier.fillMaxSize()
            )
          }

          if (isSavingImage) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "Saving photo offline...",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
              )
            }
          }

          // Pick photo overlay button
          Button(
            onClick = {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.88f)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(10.dp)
              .testTag("pick_photo_button")
          ) {
            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Pick Gallery Photo", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Preset illustrations quick selector if user doesn't have a photo immediately
        Text(
          text = "Or choose standard timber preset artwork:",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val presets = listOf(
            "sofa" to "preset_living_sofa",
            "bed" to "preset_bed_king",
            "dining" to "preset_dining_table",
            "timber" to "preset_timber_slab",
            "temple" to "preset_mandir_shrine"
          )
          presets.forEach { (label, key) ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (imageUri == key) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable { imageUri = key }
            ) {
              Text(
                text = label.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = if (imageUri == key) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Item Name
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Furniture Item Name *") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("item_name_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Selector Chips
        Text(
          text = "Category *",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          allCategories.forEach { cat ->
            val isSelected = cat == category
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable { category = cat }
            ) {
              Text(
                text = cat,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Price & Dimensions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedTextField(
            value = basePriceText,
            onValueChange = { basePriceText = it.filter { char -> char.isDigit() } },
            label = { Text("Base Price (₹) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("item_price_input"),
            shape = RoundedCornerShape(12.dp)
          )

          OutlinedTextField(
            value = dimensions,
            onValueChange = { dimensions = it },
            label = { Text("Dimensions") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Crafting Description & Timber Details") },
          maxLines = 3,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Customizable Wood Types
        Text(
          text = "Available Timber Species for Customization",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        FlowRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          defaultWoods.forEach { wood ->
            val isChecked = selectedWoods.contains(wood)
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable {
                selectedWoods = if (isChecked) selectedWoods - wood else selectedWoods + wood
              }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                if (isChecked) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
                Text(
                  text = wood,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // In-Stock Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Item In Stock",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
          )
          Switch(
            checked = inStock,
            onCheckedChange = { inStock = it },
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save & Cancel Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          TextButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              if (name.isNotBlank() && !isAtCapacity) {
                val parsedPrice = basePriceText.toDoubleOrNull() ?: 10000.0
                val newItem = (existingItem ?: FurnitureItem(
                  name = name.trim(),
                  category = category,
                  basePrice = parsedPrice,
                  description = description.ifBlank { "Handcrafted solid wood furniture from Arzoo Timber & Furniture." },
                  dimensions = dimensions.ifBlank { "Standard Custom" },
                  imageUri = imageUri,
                  inStock = inStock,
                  availableWoodTypes = selectedWoods.toList().ifEmpty { listOf("Burmese Teak") },
                  availableFinishes = selectedFinishes.toList()
                )).copy(
                  name = name.trim(),
                  category = category,
                  basePrice = parsedPrice,
                  description = description.ifBlank { "Handcrafted solid wood furniture from Arzoo Timber & Furniture." },
                  dimensions = dimensions.ifBlank { "Standard Custom" },
                  imageUri = imageUri,
                  inStock = inStock,
                  availableWoodTypes = selectedWoods.toList().ifEmpty { listOf("Burmese Teak") },
                  availableFinishes = selectedFinishes.toList()
                )
                onSave(newItem)
                onDismiss()
              }
            },
            enabled = name.isNotBlank() && !isAtCapacity,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .weight(1.5f)
              .testTag("save_item_button")
          ) {
            Text(if (isEditing) "Save Changes" else "Insert Item")
          }
        }
      }
    }
  }
}
