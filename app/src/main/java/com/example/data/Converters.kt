package com.example.data

import androidx.room.TypeConverter

class Converters {
  @TypeConverter
  fun fromStringList(value: List<String>?): String {
    if (value == null) return ""
    return value.joinToString("|||")
  }

  @TypeConverter
  fun toStringList(value: String?): List<String> {
    if (value.isNullOrBlank()) return emptyList()
    return value.split("|||").filter { it.isNotBlank() }
  }
}
