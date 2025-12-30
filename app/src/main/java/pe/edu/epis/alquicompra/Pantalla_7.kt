package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla7Search(
    onBackClick: () -> Unit,
    onMapViewClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var isListView by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {

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
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color(0xFF9CA3AF)
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedContainerColor = Color(0xFFF3F4F6),
                        focusedContainerColor = Color.White
                    )
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
                    Card(
                        onClick = onFilterClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3F4F6)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🔧", fontSize = 16.sp)
                            Text(
                                text = "Filtros",
                                fontSize = 14.sp,
                                color = Color(0xFF111827)
                            )
                        }
                    }

                    Card(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF3F4F6)
                        )
                    ) {
                        Text(
                            text = "Ordenar",
                            fontSize = 14.sp,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
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

                    IconButton(
                        onClick = onMapViewClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFF3F4F6),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Vista mapa",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "24 resultados encontrados",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Text(
                    text = "📍 Cerca de Puno",
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchProductCard(
                    emoji = "💻",
                    title = "Laptop HP Pavilion 15\"",
                    price = "S/ 25/día • Alquiler",
                    description = "Intel i5, 8GB RAM, SSD 256GB",
                    distance = "0.5 km",
                    rating = "4.8",
                    reviews = "12",
                    badge = "Disponible",
                    badgeColor = Color(0xFF10B981),
                    isFavorite = false,
                    gradientColors = listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE))
                )

                SearchProductCard(
                    emoji = "🪑",
                    title = "Silla de oficina ergonómica",
                    price = "S/ 150 • Venta",
                    description = "Ajustable, soporte lumbar, como nueva",
                    distance = "1.2 km",
                    rating = "4.5",
                    reviews = "8",
                    badge = "Negociable",
                    badgeColor = Color(0xFF3B82F6),
                    isFavorite = false,
                    gradientColors = listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0))
                )

                SearchProductCard(
                    emoji = "📷",
                    title = "Cámara Canon EOS R6",
                    price = "S/ 40/día • Alquiler",
                    description = "Incluye lente 24-70mm y trípode",
                    distance = "2.1 km",
                    rating = "5.0",
                    reviews = "15",
                    badge = "Muy popular",
                    badgeColor = Color(0xFFF59E0B),
                    isFavorite = true,
                    gradientColors = listOf(Color(0xFFF3E8FF), Color(0xFFE9D5FF))
                )

                SearchProductCard(
                    emoji = "🎸",
                    title = "Guitarra acústica Yamaha",
                    price = "S/ 280 • Venta",
                    description = "FG800, excelente estado, con funda",
                    distance = "0.8 km",
                    rating = "4.7",
                    reviews = "5",
                    badge = "Disponible",
                    badgeColor = Color(0xFF10B981),
                    isFavorite = false,
                    gradientColors = listOf(Color(0xFFFECDD3), Color(0xFFFDA4AF))
                )

                SearchProductCard(
                    emoji = "🎮",
                    title = "PlayStation 5 con juegos",
                    price = "S/ 50/día • Alquiler",
                    description = "Incluye 2 controles y 5 juegos",
                    distance = "1.5 km",
                    rating = "4.9",
                    reviews = "20",
                    badge = "Muy popular",
                    badgeColor = Color(0xFFF59E0B),
                    isFavorite = false,
                    gradientColors = listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE))
                )

                SearchProductCard(
                    emoji = "🚲",
                    title = "Bicicleta de montaña Trek",
                    price = "S/ 450 • Venta",
                    description = "Shimano 21 velocidades, frenos de disco",
                    distance = "3.0 km",
                    rating = "4.6",
                    reviews = "9",
                    badge = "Negociable",
                    badgeColor = Color(0xFF3B82F6),
                    isFavorite = false,
                    gradientColors = listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductCard(
    emoji: String,
    title: String,
    price: String,
    description: String,
    distance: String,
    rating: String,
    reviews: String,
    badge: String,
    badgeColor: Color,
    isFavorite: Boolean,
    gradientColors: List<Color>
) {
    Card(
        onClick = {},
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
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 48.sp)
            }

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
                            text = title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                    }

                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) Color(0xFFEF4444) else Color(0xFF9CA3AF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (price.contains("Alquiler")) Color(0xFF3B82F6) else Color(0xFF10B981),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 $distance • ⭐ $rating ($reviews)",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(badgeColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 10.sp,
                            color = badgeColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}