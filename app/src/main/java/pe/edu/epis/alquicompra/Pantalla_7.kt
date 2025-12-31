package pe.edu.epis.alquicompra

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla7Search(
    onBackClick: () -> Unit,
    onMapViewClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    categoryFilter: String? = null
) {
    val context = LocalContext.current
    val listingRepository = remember { ListingRepository(context) }
    val favoritesRepository = remember { FavoritesRepository(context) }
    val scope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var isListView by remember { mutableStateOf(true) }
    var listings by remember { mutableStateOf<List<Listing>>(emptyList()) }
    var favoriteIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }

    //CARGAR FAVORITOS
    fun loadFavorites() {
        scope.launch {
            favoritesRepository.getFavoriteIds().onSuccess { ids ->
                favoriteIds = ids
            }
        }
    }

    // CARGAR TODOS LOS PRODUCTOS (cuando no hay categoría)
    LaunchedEffect(categoryFilter) {
        if (categoryFilter != null) {
            scope.launch {
                isLoading = true
                hasSearched = true

                listingRepository.getListingsByCategory(categoryFilter).onSuccess { loadedListings ->
                    listings = loadedListings
                    isLoading = false
                }.onFailure {
                    isLoading = false
                }
            }
        } else {
            // SI NO HAY CATEGORÍA, CARGAR TODOS
            scope.launch {
                isLoading = true
                hasSearched = true

                listingRepository.getAllListings().onSuccess { allListings ->
                    listings = allListings
                    isLoading = false
                }.onFailure {
                    isLoading = false
                }
            }
        }

        loadFavorites()
    }

    // Función de búsqueda
    fun performSearch(query: String) {
        if (query.isNotBlank()) {
            scope.launch {
                isLoading = true
                hasSearched = true

                listingRepository.getAllListings().onSuccess { allListings ->
                    listings = allListings.filter { listing ->
                        listing.title.contains(query, ignoreCase = true) ||
                                listing.description.contains(query, ignoreCase = true) ||
                                listing.category.contains(query, ignoreCase = true)
                    }
                    isLoading = false
                }.onFailure {
                    isLoading = false
                }
            }
        } else {
            // Si borra la búsqueda, volver a mostrar todos
            scope.launch {
                isLoading = true
                listingRepository.getAllListings().onSuccess { allListings ->
                    listings = if (categoryFilter != null) {
                        allListings.filter { it.category == categoryFilter }
                    } else {
                        allListings
                    }
                    isLoading = false
                }.onFailure {
                    isLoading = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color(0xFF4B5563)
                    )
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.isEmpty()) {
                            performSearch("")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color(0xFF9CA3AF)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            Row {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    performSearch("")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar",
                                        tint = Color(0xFF9CA3AF)
                                    )
                                }
                                IconButton(onClick = { performSearch(searchQuery) }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Buscar",
                                        tint = Color(0xFF3B82F6)
                                    )
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedContainerColor = Color(0xFFF3F4F6),
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (categoryFilter != null) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFDBEAFE)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = categoryFilter,
                                    fontSize = 12.sp,
                                    color = Color(0xFF1E40AF),
                                    fontWeight = FontWeight.Medium
                                )
                                IconButton(
                                    onClick = onBackClick,
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Quitar filtro",
                                        tint = Color(0xFF1E40AF),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { isListView = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = if (isListView) Color(0xFFDBEAFE) else Color(0xFFF3F4F6),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.List,
                            contentDescription = "Vista lista",
                            tint = if (isListView) Color(0xFF3B82F6) else Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        // Contenido
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else if (listings.isEmpty() && hasSearched) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(text = "😔", fontSize = 64.sp)
                    Text(
                        text = "No encontramos resultados",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "Intenta con otros términos de búsqueda",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${listings.size} ${if (listings.size == 1) "resultado" else "resultados"}",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280)
                        )
                        Text(
                            text = "📍 Cerca de Puno",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                items(listings) { listing ->
                    SearchProductCard(
                        listing = listing,
                        isFavorite = favoriteIds.contains(listing.id),
                        onClick = { onProductClick(listing.id) },
                        onToggleFavorite = {
                            scope.launch {
                                if (favoriteIds.contains(listing.id)) {
                                    favoritesRepository.removeFavorite(listing.id)
                                } else {
                                    favoritesRepository.addFavorite(listing.id)
                                }
                                loadFavorites()
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductCard(
    listing: Listing,
    isFavorite: Boolean = false,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = listing.imageUrl,
                contentDescription = listing.title,
                modifier = Modifier
                    .size(96.dp)
                    .background(Color(0xFFF3F4F6)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = listing.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827),
                            maxLines = 2
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "S/ ${listing.price}${if (listing.type == "alquiler") "/día" else ""}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (listing.type == "alquiler") Color(0xFF3B82F6) else Color(0xFF10B981),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = listing.description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📍 ${listing.location}",
                            fontSize = 11.sp,
                            color = Color(0xFF6B7280)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (listing.type == "alquiler")
                                        Color(0xFFDBEAFE)
                                    else
                                        Color(0xFFD1FAE5)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = listing.type.uppercase(),
                                fontSize = 9.sp,
                                color = if (listing.type == "alquiler")
                                    Color(0xFF1E40AF)
                                else
                                    Color(0xFF065F46),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val message = "Hola! Me interesa tu producto: ${listing.title}"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/${listing.userPhone}?text=${Uri.encode(message)}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(text = "💬", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}