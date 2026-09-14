package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.FurnitureItem
import java.io.File
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FurnitureCard(
  item: FurnitureItem,
  onClick: () -> Unit,
  onCustomizeClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
    maximumFractionDigits = 0
  }
  val priceString = try {
    currencyFormat.format(item.basePrice)
  } catch (e: Exception) {
    "₹${item.basePrice.toInt()}"
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("furniture_card_${item.id}")
      .clickable { onClick() },
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      // Image Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
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

        // Top badges: Category and Rating
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
          ) {
            Text(
              text = item.category,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFA000),
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "${item.rating}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }

        // In-stock badge
        if (item.inStock) {
          Surface(
            shape = RoundedCornerShape(topStart = 8.dp),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.92f),
            modifier = Modifier.align(Alignment.BottomEnd)
          ) {
            Text(
              text = "IN STOCK",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
              color = Color.White,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }
      }

      // Details
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = item.name,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = item.dimensions,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Available woods tag list
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          item.availableWoodTypes.take(2).forEach { wood ->
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = wood,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          if (item.availableWoodTypes.size > 2) {
            Text(
              text = "+${item.availableWoodTypes.size - 2} more",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = MaterialTheme.colorScheme.secondary
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Price and Customize Action
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Starting from",
              style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = priceString,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            )
          }

          FilledTonalButton(
            onClick = onCustomizeClick,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("customize_btn_${item.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Palette,
              contentDescription = "Customize",
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Customize",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }
    }
  }
}
