package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos(
    onBackClick: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    val favoritesRepository = remember { FavoritesRepository(context) }
    val scope = rememberCoroutineScope()

    var favoriteListings by remember { mutableStateOf<List<Listing>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    fun loadFavorites() {
        scope.launch {
            isRefreshing = true
            favoritesRepository.getUserFavorites().onSuccess { listings ->
                favoriteListings = listings
                isLoading = false
                isRefreshing = false
            }.onFailure {
                isLoading = false
                isRefreshing = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFEF4444)
                    )
                    Text(
                        text = "Mis Favoritos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Divider(color = Color(0xFFF3F4F6))

        // Contenido
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else if (favoriteListings.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "💔", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No tienes favoritos aún",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explora productos y guarda tus favoritos",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    if (isRefreshing) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF3B82F6)
                        )
                    }
                }

                item {
                    Text(
                        text = "${favoriteListings.size} ${if (favoriteListings.size == 1) "favorito" else "favoritos"}",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                items(favoriteListings) { listing ->
                    FavoriteProductCard(
                        listing = listing,
                        onClick = { onProductClick(listing.id) },
                        onRemoveFavorite = {
                            scope.launch {
                                favoritesRepository.removeFavorite(listing.id)
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
fun FavoriteProductCard(
    listing: Listing,
    onClick: () -> Unit,
    onRemoveFavorite: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
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
                    .size(100.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(Color(0xFFF3F4F6)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = listing.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827),
                        maxLines = 2,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemoveFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Quitar de favoritos",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = "S/ ${listing.price}${if (listing.type == "alquiler") "/día" else ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (listing.type == "alquiler") Color(0xFF3B82F6) else Color(0xFF10B981)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ${listing.location}",
                        fontSize = 12.sp,
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
            }
        }
    }
}