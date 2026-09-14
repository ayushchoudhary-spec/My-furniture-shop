package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.QuoteItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EstimateQuoteDialog(
  quoteItems: List<QuoteItem>,
  onDismiss: () -> Unit,
  onUpdateQuantity: (String, Int) -> Unit,
  onRemoveItem: (String) -> Unit,
  onClearAll: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var customerName by remember { mutableStateOf("") }
  var includeTax by remember { mutableStateOf(true) }

  val subtotal = quoteItems.sumOf { it.unitPrice * it.quantity }
  val taxAmount = if (includeTax) subtotal * 0.18 else 0.0
  val grandTotal = subtotal + taxAmount

  val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
    maximumFractionDigits = 0
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = modifier
        .fillMaxWidth(0.95f)
        .heightIn(max = 700.dp)
        .testTag("estimate_quote_dialog"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
            Column {
              Text(
                text = "Arzoo Timber & Furniture",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Custom Catalogue Estimate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (quoteItems.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "No customized items yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Tap 'Customize' on any furniture piece to calculate pricing and add here.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        } else {
          // Customer Name Field
          OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name / Reference (Optional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Items List
          LazyColumn(
            modifier = Modifier
              .weight(1f, fill = false)
              .heightIn(max = 260.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(quoteItems, key = { it.id }) { item ->
              Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = item.name,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                      modifier = Modifier.weight(1f)
                    )
                    IconButton(
                      onClick = { onRemoveItem(item.id) },
                      modifier = Modifier.size(28.dp)
                    ) {
                      Icon(Icons.Default.Delete, contentDescription = "Remove", modifier = Modifier.size(16.dp))
                    }
                  }

                  Text(
                    text = "${item.customization.woodType} • ${item.customization.finish} • ${item.customization.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                  )
                  if (item.customization.fabric.isNotBlank() && item.customization.fabric != "Natural Linen") {
                    Text(
                      text = "Fabric: ${item.customization.fabric}",
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }

                  Spacer(modifier = Modifier.height(6.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = "${currencyFormat.format(item.unitPrice)} x ${item.quantity} = ${currencyFormat.format(item.unitPrice * item.quantity)}",
                      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                      color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                      FilledIconButton(
                        onClick = { if (item.quantity > 1) onUpdateQuantity(item.id, item.quantity - 1) },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
                      }
                      Text(
                        text = "${item.quantity}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 4.dp)
                      )
                      FilledIconButton(
                        onClick = { onUpdateQuantity(item.id, item.quantity + 1) },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.size(28.dp)
                      ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                      }
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Divider(color = MaterialTheme.colorScheme.outlineVariant)
          Spacer(modifier = Modifier.height(10.dp))

          // GST toggle and calculations
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "Include 18% GST (Tax)", style = MaterialTheme.typography.bodySmall)
            Switch(
              checked = includeTax,
              onCheckedChange = { includeTax = it },
              colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Subtotal:", style = MaterialTheme.typography.bodySmall)
            Text(text = currencyFormat.format(subtotal), style = MaterialTheme.typography.bodySmall)
          }

          if (includeTax) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "GST (18%):", style = MaterialTheme.typography.bodySmall)
              Text(text = currencyFormat.format(taxAmount), style = MaterialTheme.typography.bodySmall)
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Estimated Total:",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
              text = currencyFormat.format(grandTotal),
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            TextButton(
              onClick = onClearAll,
              modifier = Modifier.weight(1f)
            ) {
              Text("Clear", color = MaterialTheme.colorScheme.error)
            }

            Button(
              onClick = {
                shareQuote(
                  context = context,
                  customerName = customerName,
                  items = quoteItems,
                  subtotal = subtotal,
                  tax = taxAmount,
                  grandTotal = grandTotal,
                  currencyFormat = currencyFormat
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.weight(2f)
            ) {
              Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Share Estimate")
            }
          }
        }
      }
    }
  }
}

private fun shareQuote(
  context: Context,
  customerName: String,
  items: List<QuoteItem>,
  subtotal: Double,
  tax: Double,
  grandTotal: Double,
  currencyFormat: NumberFormat
) {
  val sb = StringBuilder()
  sb.appendLine("🛋️ *ARZOO TIMBER & FURNITURE*")
  sb.appendLine("Official Furniture Catalogue Estimate")
  if (customerName.isNotBlank()) {
    sb.appendLine("Client: $customerName")
  }
  sb.appendLine("-----------------------------------")
  items.forEachIndexed { idx, item ->
    sb.appendLine("${idx + 1}. *${item.name}* (Qty: ${item.quantity})")
    sb.appendLine("   Timber: ${item.customization.woodType}")
    sb.appendLine("   Finish: ${item.customization.finish}")
    sb.appendLine("   Size: ${item.customization.size}")
    if (item.customization.fabric.isNotBlank()) {
      sb.appendLine("   Fabric: ${item.customization.fabric}")
    }
    sb.appendLine("   Price: ${currencyFormat.format(item.unitPrice * item.quantity)}")
    sb.appendLine()
  }
  sb.appendLine("-----------------------------------")
  sb.appendLine("Subtotal: ${currencyFormat.format(subtotal)}")
  if (tax > 0) {
    sb.appendLine("GST (18%): ${currencyFormat.format(tax)}")
  }
  sb.appendLine("*Grand Total: ${currencyFormat.format(grandTotal)}*")
  sb.appendLine()
  sb.appendLine("Thank you for choosing Arzoo Timber & Furniture!")

  val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, sb.toString())
    type = "text/plain"
  }
  context.startActivity(Intent.createChooser(sendIntent, "Share Estimate via"))
}
