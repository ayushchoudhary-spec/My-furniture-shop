package com.example.data

import kotlinx.coroutines.flow.Flow

class FurnitureRepository(private val furnitureDao: FurnitureDao) {
  val allItems: Flow<List<FurnitureItem>> = furnitureDao.getAllItems()
  val itemCount: Flow<Int> = furnitureDao.getItemsCount()

  suspend fun getItemById(id: Long): FurnitureItem? = furnitureDao.getItemById(id)

  suspend fun insert(item: FurnitureItem): Long = furnitureDao.insertItem(item)

  suspend fun update(item: FurnitureItem) = furnitureDao.updateItem(item)

  suspend fun delete(item: FurnitureItem) = furnitureDao.deleteItem(item)

  suspend fun deleteById(id: Long) = furnitureDao.deleteById(id)

  suspend fun getCount(): Int = furnitureDao.getCount()

  suspend fun seedInitialCatalogueIfEmpty() {
    if (furnitureDao.getCount() == 0) {
      furnitureDao.insertAll(initialCatalogueItems)
    }
  }

  suspend fun resetToDefaultCatalogue() {
    furnitureDao.clearAll()
    furnitureDao.insertAll(initialCatalogueItems)
  }

  companion object {
    val initialCatalogueItems = listOf(
      FurnitureItem(
        id = 1,
        name = "Royal Teakwood Chesterfield Sofa",
        category = "Living Room",
        basePrice = 48500.0,
        description = "Handcrafted from 100% seasoned Burmese Teak with deep button tufting, scroll armrests, and high-density orthopaedic foam cushioning.",
        dimensions = "84\"W x 36\"D x 34\"H",
        inStock = true,
        imageUri = "preset_living_sofa",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain", "PU Scratch-Proof"),
        availableFabrics = listOf("Cognac Leatherette", "Royal Emerald Velvet", "Natural Linen", "Terracotta Weave"),
        availableSizes = listOf("Standard (3-Seater)", "Compact (2-Seater)", "Deluxe (4-Seater)"),
        rating = 4.9f
      ),
      FurnitureItem(
        id = 2,
        name = "Sheesham Live-Edge Coffee Table",
        category = "Living Room",
        basePrice = 14200.0,
        description = "Organic natural edge slab showcasing rich dark wood grain swirls. Treated with anti-termite resin finish and mounted on sturdy matte black hairpin legs.",
        dimensions = "48\"W x 26\"D x 18\"H",
        inStock = true,
        imageUri = "preset_living_table",
        availableWoodTypes = listOf("Indian Sheesham", "Burmese Teak", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Compact", "Grand"),
        rating = 4.8f
      ),
      FurnitureItem(
        id = 3,
        name = "Mid-Century Teak Accent Armchair",
        category = "Living Room",
        basePrice = 18900.0,
        description = "Elegant curved armrest geometry inspired by Danish mid-century modern architecture. Solid teak frame with breathable weave cushion.",
        dimensions = "30\"W x 32\"D x 32\"H",
        inStock = true,
        imageUri = "preset_living_chair",
        availableWoodTypes = listOf("Burmese Teak", "White Oak", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Dark Walnut Stain"),
        availableFabrics = listOf("Royal Emerald Velvet", "Natural Linen", "Midnight Blue Fabric"),
        availableSizes = listOf("Standard"),
        rating = 4.7f
      ),
      FurnitureItem(
        id = 4,
        name = "King Teak Platform Bed with Cane Headboard",
        category = "Bedroom",
        basePrice = 56000.0,
        description = "Solid teakwood construction with hand-woven natural rattan cane lattice headboard. Noise-free mortise & tenon joinery with solid wooden support slats.",
        dimensions = "76\"W x 82\"L x 46\"H",
        inStock = true,
        imageUri = "preset_bed_king",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard (King)", "Compact (Queen)", "Grand (Super King)"),
        rating = 5.0f
      ),
      FurnitureItem(
        id = 5,
        name = "Heritage 4-Door Wardrobe with Drawers",
        category = "Bedroom",
        basePrice = 64500.0,
        description = "Spacious bedroom almirah featuring deep shelving, heavy brass locks, twin interior drawers, and full hanging rod.",
        dimensions = "72\"W x 24\"D x 80\"H",
        inStock = true,
        imageUri = "preset_bed_wardrobe",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham"),
        availableFinishes = listOf("Natural Matte", "Dark Walnut Stain", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Compact (3-Door)", "Grand (6-Door)"),
        rating = 4.9f
      ),
      FurnitureItem(
        id = 6,
        name = "Artisan Bedside Nightstand",
        category = "Bedroom",
        basePrice = 7800.0,
        description = "Dual drawer nightstand with smooth soft-close wooden runners and brushed brass knurled handles.",
        dimensions = "20\"W x 18\"D x 22\"H",
        inStock = true,
        imageUri = "preset_bed_nightstand",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Deluxe"),
        rating = 4.8f
      ),
      FurnitureItem(
        id = 7,
        name = "8-Seater Grand Dining Table Set",
        category = "Dining",
        basePrice = 72000.0,
        description = "Monumental solid timber dining table with 8 ergonomic cushioned ladder-back chairs. Scratch-resistant food-safe oil coating.",
        dimensions = "96\"W x 42\"D x 30\"H",
        inStock = true,
        imageUri = "preset_dining_table",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "PU Scratch-Proof", "Dark Walnut Stain"),
        availableFabrics = listOf("Natural Linen", "Cognac Leatherette", "Terracotta Weave"),
        availableSizes = listOf("Standard (8-Seater)", "Compact (6-Seater)", "Grand (10-Seater)"),
        rating = 4.9f
      ),
      FurnitureItem(
        id = 8,
        name = "Solid Timber Kitchen Island & Bar Stool",
        category = "Dining",
        basePrice = 9500.0,
        description = "Counter height bar stool with contoured wooden saddle seat and brass footrest bar. Built to withstand decades of daily use.",
        dimensions = "18\"W x 16\"D x 30\"H",
        inStock = true,
        imageUri = "preset_dining_stool",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "White Ash"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Deluxe"),
        rating = 4.6f
      ),
      FurnitureItem(
        id = 9,
        name = "Burmese Seasoned Teakwood Planks (Bundle)",
        category = "Timber & Slabs",
        basePrice = 28000.0,
        description = "100% kiln-dried prime grade Burma Teak wood planks. Uniform golden-amber grain, pest resistant, ideal for custom interior carpentry and yacht finishing.",
        dimensions = "8'L x 6\"W x 1.5\"Thick",
        inStock = true,
        imageUri = "preset_timber_plank",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "Reclaimed Sal Wood"),
        availableFinishes = listOf("Natural Matte", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard (50 sq.ft)", "Compact (25 sq.ft)", "Grand (100 sq.ft)"),
        rating = 5.0f
      ),
      FurnitureItem(
        id = 10,
        name = "Exotic Sheesham Natural Edge Cross-Section Slab",
        category = "Timber & Slabs",
        basePrice = 34000.0,
        description = "One-of-a-kind monolithic tree slab with natural live bark periphery. Ready for custom epoxy resin river dining table crafting.",
        dimensions = "72\"L x 36\"-42\"W x 3\"Thick",
        inStock = true,
        imageUri = "preset_timber_slab",
        availableWoodTypes = listOf("Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Grand"),
        rating = 4.9f
      ),
      FurnitureItem(
        id = 11,
        name = "Executive Walnut L-Shape Writing Desk",
        category = "Office & Study",
        basePrice = 42500.0,
        description = "Spacious workstation desk with integrated cable channel, lockable document drawers, and modesty timber privacy panel.",
        dimensions = "66\"W x 32\"D x 30\"H",
        inStock = true,
        imageUri = "preset_office_desk",
        availableWoodTypes = listOf("American Walnut", "Burmese Teak", "Indian Sheesham"),
        availableFinishes = listOf("Natural Matte", "Dark Walnut Stain", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Compact", "Grand"),
        rating = 4.8f
      ),
      FurnitureItem(
        id = 12,
        name = "Teak Modular Grid Bookshelf (6-Tier)",
        category = "Office & Study",
        basePrice = 24000.0,
        description = "Heavy load-bearing timber library bookshelf with open architectural cube slots for books, sculptures, and trophies.",
        dimensions = "48\"W x 14\"D x 78\"H",
        inStock = true,
        imageUri = "preset_office_shelf",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham", "American Walnut"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Dark Walnut Stain"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Compact (4-Tier)", "Grand (Wide 8-Tier)"),
        rating = 4.7f
      ),
      FurnitureItem(
        id = 13,
        name = "Traditional Hand-Carved Teakwood Swing (Jhoola)",
        category = "Outdoor & Balcony",
        basePrice = 38000.0,
        description = "Authentic Indian porch swing crafted from seasoned teak with heavy brass chains, peacock link hooks, and thick velvet seating mattress.",
        dimensions = "60\"W x 26\"D x 24\"H (Chain 72\")",
        inStock = true,
        imageUri = "preset_outdoor_swing",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham"),
        availableFinishes = listOf("Natural Matte", "Teak Honey Gloss", "Vintage Distressed"),
        availableFabrics = listOf("Royal Emerald Velvet", "Terracotta Weave", "Natural Linen"),
        availableSizes = listOf("Standard", "Grand"),
        rating = 5.0f
      ),
      FurnitureItem(
        id = 14,
        name = "All-Weather Teak Garden Settee Bench",
        category = "Outdoor & Balcony",
        basePrice = 16500.0,
        description = "High natural oil content timber bench that withstands monsoon rains and harsh sun without rotting or cracking.",
        dimensions = "54\"W x 24\"D x 36\"H",
        inStock = true,
        imageUri = "preset_outdoor_bench",
        availableWoodTypes = listOf("Burmese Teak", "Reclaimed Sal Wood"),
        availableFinishes = listOf("Natural Matte", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Grand (6ft)"),
        rating = 4.8f
      ),
      FurnitureItem(
        id = 15,
        name = "Sagwan Teak Pooja Mandir with Dome & Bells",
        category = "Pooja & Mandir",
        basePrice = 32000.0,
        description = "Intricately carved home temple featuring traditional Shikhar dome, brass bell hangings, diya tray pull-out drawer, and incense storage.",
        dimensions = "36\"W x 20\"D x 48\"H",
        inStock = true,
        imageUri = "preset_mandir_shrine",
        availableWoodTypes = listOf("Burmese Teak", "Indian Sheesham"),
        availableFinishes = listOf("Teak Honey Gloss", "Natural Matte", "Dark Walnut Stain"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard", "Compact (Wall-Mount)", "Grand (Double Door)"),
        rating = 4.9f
      ),
      FurnitureItem(
        id = 16,
        name = "Walnut & Teak Fluted TV Entertainment Console",
        category = "Living Room",
        basePrice = 29500.0,
        description = "Modern low-profile media unit with tambour fluted sliding wood doors and concealed wire management holes.",
        dimensions = "70\"W x 18\"D x 20\"H",
        inStock = true,
        imageUri = "preset_living_console",
        availableWoodTypes = listOf("Burmese Teak", "American Walnut", "Indian Sheesham"),
        availableFinishes = listOf("Natural Matte", "Dark Walnut Stain", "PU Scratch-Proof"),
        availableFabrics = emptyList(),
        availableSizes = listOf("Standard (70\")", "Compact (55\")", "Grand (85\")"),
        rating = 4.8f
      )
    )
  }
}
