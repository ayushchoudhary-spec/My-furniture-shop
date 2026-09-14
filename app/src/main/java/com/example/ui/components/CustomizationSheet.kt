package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.CustomizationSelection
import com.example.data.FurnitureItem
import com.example.ui.theme.FabricCharcoal
import com.example.ui.theme.FabricLeatherCognac
import com.example.ui.theme.FabricLinenIvory
import com.example.ui.theme.FabricMidnightNavy
import com.example.ui.theme.FabricTerracotta
import com.example.ui.theme.FabricVelvetEmerald
import com.example.ui.theme.WoodAshColor
import com.example.ui.theme.WoodOakColor
import com.example.ui.theme.WoodSheeshamColor
import com.example.ui.theme.WoodTeakColor
import com.example.ui.theme.WoodWalnutColor
import java.io.File
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomizationSheet(
  item: FurnitureItem,
  sheetState: SheetState,
  onDismiss: () -> Unit,
  onAddToQuote: (CustomizationSelection, Int, Double) -> Unit,
  onDeleteItem: (FurnitureItem) -> Unit,
  onEditItem: (FurnitureItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedWood by remember(item) {
    mutableStateOf(item.availableWoodTypes.firstOrNull() ?: "Burmese Teak")
  }
  var selectedFinish by remember(item) {
    mutableStateOf(item.availableFinishes.firstOrNull() ?: "Natural Matte")
  }
  var selectedFabric by remember(item) {
    mutableStateOf(item.availableFabrics.firstOrNull() ?: "Natural Linen")
  }
  var selectedSize by remember(item) {
    mutableStateOf(item.availableSizes.firstOrNull() ?: "Standard")
  }
  var quantity by remember { mutableIntStateOf(1) }

  val selection = CustomizationSelection(
    woodType = selectedWood,
    finish = selectedFinish,
    fabric = selectedFabric,
    size = selectedSize
  )

  val calculatedUnitCost = selection.calculatePrice(item.basePrice)
  val totalCost = calculatedUnitCost * quantity

  val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
    maximumFractionDigits = 0
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("customization_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 36.dp)
    ) {
      // Header with close and actions
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "CUSTOMIZE & QUOTE",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.secondary
          )
          Text(
            text = item.name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { onEditItem(item) }) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "Edit Furniture Item",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          IconButton(onClick = { onDeleteItem(item) }) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Delete Furniture Item",
              tint = Color(0xFFBA1A1A)
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Hero Image Preview with active Wood & Fabric hint
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .padding(horizontal = 20.dp)
          .clip(RoundedCornerShape(16.dp))
      ) {
        val isLocalFile = item.imageUri.startsWith("/") || item.imageUri.startsWith("file://")
        if (isLocalFile && File(item.imageUri.removePrefix("file://")).exists()) {
          AsyncImage(
            model = File(item.imageUri.removePrefix("file://")),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          TimberArtIllustration(
            imageKey = item.imageUri,
            category = item.category,
            modifier = Modifier.fillMaxSize()
          )
        }

        // Live Selected Configuration Overlay
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(getWoodSwatchColor(selectedWood))
            )
            Text(
              text = "$selectedWood • $selectedFinish",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Specs & Description
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
          text = item.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Text(
            text = "Dimensions: ${item.dimensions}",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Category: ${item.category}",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.secondary
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
      Divider(modifier = Modifier.padding(horizontal = 20.dp), color = MaterialTheme.colorScheme.outlineVariant)
      Spacer(modifier = Modifier.height(14.dp))

      // 1. TIMBER / WOOD SELECTION
      if (item.availableWoodTypes.isNotEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "1. Select Timber / Wood Species",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = selectedWood,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item.availableWoodTypes.forEach { wood ->
              val isSelected = wood == selectedWood
              val extraText = when (wood) {
                "Indian Sheesham" -> "+₹1,500"
                "American Walnut" -> "+₹3,800"
                "White Oak" -> "+₹2,800"
                "White Ash" -> "+₹1,200"
                "Reclaimed Sal Wood" -> "+₹800"
                else -> "Standard"
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .testTag("wood_option_$wood")
                  .clickable { selectedWood = wood }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(14.dp)
                      .clip(CircleShape)
                      .background(getWoodSwatchColor(wood))
                      .border(1.dp, Color.White, CircleShape)
                  )
                  Text(
                    text = wood,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "($extraText)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 2. FINISH / POLISH SELECTION
      if (item.availableFinishes.isNotEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "2. Select Polish / Surface Finish",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = selectedFinish,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item.availableFinishes.forEach { finish ->
              val isSelected = finish == selectedFinish
              val extraText = when (finish) {
                "Teak Honey Gloss" -> "+₹850"
                "Dark Walnut Stain" -> "+₹1,400"
                "PU Scratch-Proof" -> "+₹2,200"
                "Vintage Distressed" -> "+₹1,600"
                else -> "Standard"
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .testTag("finish_option_$finish")
                  .clickable { selectedFinish = finish }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  if (isSelected) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                  Text(
                    text = finish,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "($extraText)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 3. FABRIC / UPHOLSTERY SELECTION
      if (item.availableFabrics.isNotEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "3. Select Fabric & Upholstery",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = selectedFabric,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item.availableFabrics.forEach { fabric ->
              val isSelected = fabric == selectedFabric
              val swatchColor = getFabricSwatchColor(fabric)
              val extraText = when (fabric) {
                "Royal Emerald Velvet" -> "+₹1,800"
                "Cognac Leatherette" -> "+₹3,200"
                "Midnight Blue Fabric" -> "+₹1,400"
                "Terracotta Weave" -> "+₹1,100"
                else -> "Standard"
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .testTag("fabric_option_$fabric")
                  .clickable { selectedFabric = fabric }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(16.dp)
                      .clip(CircleShape)
                      .background(swatchColor)
                      .border(1.dp, Color.White, CircleShape)
                  )
                  Text(
                    text = fabric,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "($extraText)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 4. SIZE SELECTION
      if (item.availableSizes.isNotEmpty()) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "4. Select Size / Configuration",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = selectedSize,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            item.availableSizes.forEach { size ->
              val isSelected = size == selectedSize
              val sizeModifier = when {
                size.contains("Compact") -> "-15%"
                size.contains("Deluxe") -> "+25%"
                size.contains("Grand") -> "+40%"
                else -> "Standard"
              }

              Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .testTag("size_option_$size")
                  .clickable { selectedSize = size }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = size,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "($sizeModifier)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // PRICING SUMMARY CARD
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "PRICE BREAKDOWN (REAL-TIME)",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.secondary
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Base Item Price:", style = MaterialTheme.typography.bodySmall)
            Text(text = currencyFormat.format(item.basePrice), style = MaterialTheme.typography.bodySmall)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Custom Timber ($selectedWood):", style = MaterialTheme.typography.bodySmall)
            val woodExtra = when (selectedWood) {
              "Indian Sheesham" -> 1500.0
              "American Walnut" -> 3800.0
              "White Oak" -> 2800.0
              "White Ash" -> 1200.0
              "Reclaimed Sal Wood" -> 800.0
              else -> 0.0
            }
            Text(text = if (woodExtra > 0) "+${currencyFormat.format(woodExtra)}" else "Included", style = MaterialTheme.typography.bodySmall)
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Finish Polish ($selectedFinish):", style = MaterialTheme.typography.bodySmall)
            val finishExtra = when (selectedFinish) {
              "Teak Honey Gloss" -> 850.0
              "Dark Walnut Stain" -> 1400.0
              "PU Scratch-Proof" -> 2200.0
              "Vintage Distressed" -> 1600.0
              else -> 0.0
            }
            Text(text = if (finishExtra > 0) "+${currencyFormat.format(finishExtra)}" else "Included", style = MaterialTheme.typography.bodySmall)
          }

          if (item.availableFabrics.isNotEmpty()) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "Upholstery ($selectedFabric):", style = MaterialTheme.typography.bodySmall)
              val fabricExtra = when (selectedFabric) {
                "Royal Emerald Velvet" -> 1800.0
                "Cognac Leatherette" -> 3200.0
                "Midnight Blue Fabric" -> 1400.0
                "Terracotta Weave" -> 1100.0
                else -> 0.0
              }
              Text(text = if (fabricExtra > 0) "+${currencyFormat.format(fabricExtra)}" else "Included", style = MaterialTheme.typography.bodySmall)
            }
          }

          Divider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outlineVariant)

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Calculated Unit Price:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
              )
              Text(
                text = currencyFormat.format(calculatedUnitCost),
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.primary
                )
              )
            }

            // Quantity stepper
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              FilledIconButton(
                onClick = { if (quantity > 1) quantity-- },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity", modifier = Modifier.size(16.dp))
              }

              Text(
                text = "$quantity",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 4.dp)
              )

              FilledIconButton(
                onClick = { quantity++ },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.size(36.dp)
              ) {
                Icon(Icons.Default.Add, contentDescription = "Increase Quantity", modifier = Modifier.size(16.dp))
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Final Action: Add to Estimate / Quote
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Button(
          onClick = {
            onAddToQuote(selection, quantity, calculatedUnitCost)
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .testTag("add_to_quote_button")
        ) {
          Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Add to Estimate (${currencyFormat.format(totalCost)})",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}

fun getWoodSwatchColor(wood: String): Color = when (wood) {
  "Burmese Teak" -> WoodTeakColor
  "Indian Sheesham" -> WoodSheeshamColor
  "American Walnut" -> WoodWalnutColor
  "White Oak" -> WoodOakColor
  "White Ash" -> WoodAshColor
  else -> Color(0xFF795548)
}

fun getFabricSwatchColor(fabric: String): Color = when (fabric) {
  "Royal Emerald Velvet" -> FabricVelvetEmerald
  "Cognac Leatherette" -> FabricLeatherCognac
  "Midnight Blue Fabric" -> FabricMidnightNavy
  "Terracotta Weave" -> FabricTerracotta
  "Natural Linen" -> FabricLinenIvory
  else -> FabricCharcoal
}
