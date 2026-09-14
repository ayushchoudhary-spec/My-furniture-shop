package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimberArtIllustration(
  imageKey: String,
  category: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFFE2E8F0),
            Color(0xFFCBD5E1)
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      if (w <= 0 || h <= 0) return@Canvas

      // Soft ambient shadow under furniture
      drawOval(
        color = Color(0x22000000),
        topLeft = Offset(w * 0.15f, h * 0.78f),
        size = Size(w * 0.7f, h * 0.12f)
      )

      when {
        // SOFA ARTWORK
        imageKey.contains("sofa") -> {
          val woodColor = Color(0xFF5D4037)
          val cushionColor = Color(0xFF3E2723)
          val accentColor = Color(0xFF8D6E63)

          // Backrest
          drawRoundRect(
            color = cushionColor,
            topLeft = Offset(w * 0.18f, h * 0.28f),
            size = Size(w * 0.64f, h * 0.32f),
            cornerRadius = CornerRadius(16f, 16f)
          )
          // Tufting lines on sofa back
          for (i in 1..3) {
            val tx = w * (0.18f + i * 0.16f)
            drawLine(
              color = Color(0x33FFFFFF),
              start = Offset(tx, h * 0.32f),
              end = Offset(tx, h * 0.56f),
              strokeWidth = 2f
            )
          }
          // Main seat cushion
          drawRoundRect(
            color = accentColor,
            topLeft = Offset(w * 0.14f, h * 0.52f),
            size = Size(w * 0.72f, h * 0.20f),
            cornerRadius = CornerRadius(12f, 12f)
          )
          // Armrests
          drawRoundRect(
            color = woodColor,
            topLeft = Offset(w * 0.10f, h * 0.42f),
            size = Size(w * 0.12f, h * 0.30f),
            cornerRadius = CornerRadius(14f, 14f)
          )
          drawRoundRect(
            color = woodColor,
            topLeft = Offset(w * 0.78f, h * 0.42f),
            size = Size(w * 0.12f, h * 0.30f),
            cornerRadius = CornerRadius(14f, 14f)
          )
          // Tapered timber legs
          drawLine(
            color = Color(0xFF2E1B10),
            start = Offset(w * 0.22f, h * 0.72f),
            end = Offset(w * 0.18f, h * 0.84f),
            strokeWidth = 7f
          )
          drawLine(
            color = Color(0xFF2E1B10),
            start = Offset(w * 0.78f, h * 0.72f),
            end = Offset(w * 0.82f, h * 0.84f),
            strokeWidth = 7f
          )
          drawLine(
            color = Color(0xFF2E1B10),
            start = Offset(w * 0.50f, h * 0.72f),
            end = Offset(w * 0.50f, h * 0.84f),
            strokeWidth = 6f
          )
        }

        // BED ARTWORK
        imageKey.contains("bed") -> {
          val teakHeadboard = Color(0xFF4E342E)
          val mattressWhite = Color(0xFFF7F7F7)
          val throwBlanket = Color(0xFFB0722C)

          // Headboard
          drawRoundRect(
            color = teakHeadboard,
            topLeft = Offset(w * 0.14f, h * 0.20f),
            size = Size(w * 0.72f, h * 0.36f),
            cornerRadius = CornerRadius(12f, 12f)
          )
          // Cane weave simulation in headboard
          for (yStep in 0..4) {
            drawLine(
              color = Color(0xFFD7CCC8),
              start = Offset(w * 0.18f, h * (0.24f + yStep * 0.05f)),
              end = Offset(w * 0.82f, h * (0.24f + yStep * 0.05f)),
              strokeWidth = 2f
            )
          }
          // Pillows
          drawRoundRect(
            color = Color(0xFFECEFF1),
            topLeft = Offset(w * 0.22f, h * 0.44f),
            size = Size(w * 0.24f, h * 0.12f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          drawRoundRect(
            color = Color(0xFFECEFF1),
            topLeft = Offset(w * 0.54f, h * 0.44f),
            size = Size(w * 0.24f, h * 0.12f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          // Mattress & duvet
          drawRoundRect(
            color = mattressWhite,
            topLeft = Offset(w * 0.16f, h * 0.54f),
            size = Size(w * 0.68f, h * 0.22f),
            cornerRadius = CornerRadius(10f, 10f)
          )
          // Throw runner
          drawRect(
            color = throwBlanket,
            topLeft = Offset(w * 0.16f, h * 0.66f),
            size = Size(w * 0.68f, h * 0.08f)
          )
          // Base legs
          drawRect(
            color = teakHeadboard,
            topLeft = Offset(w * 0.18f, h * 0.76f),
            size = Size(w * 0.06f, h * 0.07f)
          )
          drawRect(
            color = teakHeadboard,
            topLeft = Offset(w * 0.76f, h * 0.76f),
            size = Size(w * 0.06f, h * 0.07f)
          )
        }

        // DINING / TABLE ARTWORK
        imageKey.contains("dining") || imageKey.contains("table") -> {
          val timberTop = Color(0xFF6D4C41)
          val chairColor = Color(0xFF3E2723)

          // Chair backs
          drawRoundRect(
            color = chairColor,
            topLeft = Offset(w * 0.22f, h * 0.28f),
            size = Size(w * 0.14f, h * 0.24f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          drawRoundRect(
            color = chairColor,
            topLeft = Offset(w * 0.64f, h * 0.28f),
            size = Size(w * 0.14f, h * 0.24f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          // Solid Table Top
          drawRoundRect(
            color = timberTop,
            topLeft = Offset(w * 0.10f, h * 0.48f),
            size = Size(w * 0.80f, h * 0.09f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          // Bevel edge
          drawRect(
            color = Color(0xFF8D6E63),
            topLeft = Offset(w * 0.10f, h * 0.48f),
            size = Size(w * 0.80f, h * 0.02f)
          )
          // Table legs
          drawRect(
            color = Color(0xFF3E2723),
            topLeft = Offset(w * 0.18f, h * 0.57f),
            size = Size(w * 0.06f, h * 0.24f)
          )
          drawRect(
            color = Color(0xFF3E2723),
            topLeft = Offset(w * 0.76f, h * 0.57f),
            size = Size(w * 0.06f, h * 0.24f)
          )
          // Chair legs
          drawLine(
            color = chairColor,
            start = Offset(w * 0.24f, h * 0.58f),
            end = Offset(w * 0.22f, h * 0.81f),
            strokeWidth = 4f
          )
          drawLine(
            color = chairColor,
            start = Offset(w * 0.72f, h * 0.58f),
            end = Offset(w * 0.74f, h * 0.81f),
            strokeWidth = 4f
          )
        }

        // TIMBER & RAW SLABS
        imageKey.contains("timber") || imageKey.contains("plank") || imageKey.contains("slab") -> {
          // Cross-cut wood rings and timber log stack
          val barkDark = Color(0xFF2E1B10)
          val heartwood = Color(0xFFB0722C)
          val sapwood = Color(0xFFD7A765)

          // 3 stacked raw timber logs
          val logPositions = listOf(
            Offset(w * 0.32f, h * 0.58f) to (w * 0.16f),
            Offset(w * 0.68f, h * 0.58f) to (w * 0.16f),
            Offset(w * 0.50f, h * 0.38f) to (w * 0.17f)
          )

          logPositions.forEach { (center, radius) ->
            // Outer bark
            drawCircle(color = barkDark, radius = radius, center = center)
            // Sapwood
            drawCircle(color = sapwood, radius = radius * 0.90f, center = center)
            // Heartwood
            drawCircle(color = heartwood, radius = radius * 0.72f, center = center)
            // Growth rings
            drawCircle(
              color = barkDark.copy(alpha = 0.4f),
              radius = radius * 0.50f,
              center = center,
              style = Stroke(width = 2f)
            )
            drawCircle(
              color = barkDark.copy(alpha = 0.4f),
              radius = radius * 0.28f,
              center = center,
              style = Stroke(width = 2f)
            )
            // Center pith
            drawCircle(color = barkDark, radius = 5f, center = center)
          }
        }

        // WARDROBE / CABINET
        imageKey.contains("wardrobe") || imageKey.contains("cabinet") || imageKey.contains("nightstand") || imageKey.contains("shelf") -> {
          val woodFrame = Color(0xFF4E342E)
          val woodPanel = Color(0xFF6D4C41)
          val brassGold = Color(0xFFD4AF37)

          // Outer frame
          drawRoundRect(
            color = woodFrame,
            topLeft = Offset(w * 0.20f, h * 0.22f),
            size = Size(w * 0.60f, h * 0.58f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          // Left door
          drawRoundRect(
            color = woodPanel,
            topLeft = Offset(w * 0.23f, h * 0.25f),
            size = Size(w * 0.26f, h * 0.52f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          // Right door
          drawRoundRect(
            color = woodPanel,
            topLeft = Offset(w * 0.51f, h * 0.25f),
            size = Size(w * 0.26f, h * 0.52f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          // Brass handles
          drawRoundRect(
            color = brassGold,
            topLeft = Offset(w * 0.45f, h * 0.46f),
            size = Size(w * 0.02f, h * 0.08f),
            cornerRadius = CornerRadius(3f, 3f)
          )
          drawRoundRect(
            color = brassGold,
            topLeft = Offset(w * 0.53f, h * 0.46f),
            size = Size(w * 0.02f, h * 0.08f),
            cornerRadius = CornerRadius(3f, 3f)
          )
        }

        // POOJA MANDIR SHRINE
        imageKey.contains("mandir") -> {
          val teakTone = Color(0xFF5D2510)
          val goldDome = Color(0xFFC59B27)

          // Mandir Dome Shikhar
          val path = Path().apply {
            moveTo(w * 0.50f, h * 0.16f)
            lineTo(w * 0.64f, h * 0.32f)
            lineTo(w * 0.36f, h * 0.32f)
            close()
          }
          drawPath(path, goldDome)

          // Columns
          drawRect(
            color = teakTone,
            topLeft = Offset(w * 0.28f, h * 0.32f),
            size = Size(w * 0.07f, h * 0.36f)
          )
          drawRect(
            color = teakTone,
            topLeft = Offset(w * 0.65f, h * 0.32f),
            size = Size(w * 0.07f, h * 0.36f)
          )
          // Shrine base platform
          drawRoundRect(
            color = teakTone,
            topLeft = Offset(w * 0.22f, h * 0.68f),
            size = Size(w * 0.56f, h * 0.12f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          // Hanging bell
          drawCircle(color = goldDome, radius = 8f, center = Offset(w * 0.50f, h * 0.42f))
          drawLine(
            color = goldDome,
            start = Offset(w * 0.50f, h * 0.32f),
            end = Offset(w * 0.50f, h * 0.42f),
            strokeWidth = 2f
          )
        }

        // DEFAULT / OUTDOOR SWING / CHAIR
        else -> {
          val mainWood = Color(0xFF5D4037)
          val seatTone = Color(0xFFB0722C)

          // Modern minimalist chair
          drawRoundRect(
            color = mainWood,
            topLeft = Offset(w * 0.32f, h * 0.25f),
            size = Size(w * 0.36f, h * 0.30f),
            cornerRadius = CornerRadius(10f, 10f)
          )
          drawRoundRect(
            color = seatTone,
            topLeft = Offset(w * 0.26f, h * 0.52f),
            size = Size(w * 0.48f, h * 0.12f),
            cornerRadius = CornerRadius(8f, 8f)
          )
          drawLine(
            color = mainWood,
            start = Offset(w * 0.32f, h * 0.64f),
            end = Offset(w * 0.28f, h * 0.82f),
            strokeWidth = 6f
          )
          drawLine(
            color = mainWood,
            start = Offset(w * 0.68f, h * 0.64f),
            end = Offset(w * 0.72f, h * 0.82f),
            strokeWidth = 6f
          )
        }
      }
    }

    // Category watermark badge for visual elegance
    Text(
      text = category.uppercase(),
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
      ),
      color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(10.dp)
    )
  }
}
