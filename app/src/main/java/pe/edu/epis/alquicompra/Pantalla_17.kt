package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log

@Composable
fun Pantalla17Perfil(
    onBackClick: () -> Unit,
    onEditProfile: () -> Unit = {},
    onPaymentMethods: () -> Unit = {},
    onSettings: () -> Unit = {},
    onHostDashboard: () -> Unit,
    onLogout: () -> Unit,
    onProductClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val listingRepository = remember { ListingRepository(context) }
    val scope = rememberCoroutineScope()

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var userListings by remember { mutableStateOf<List<Listing>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }

    // 🔥 FUNCIÓN PARA CARGAR DATOS
    fun loadUserData() {
        scope.launch {
            isRefreshing = true
            Log.d("PERFIL_SCREEN", "🔄 Cargando datos del usuario...")

            // Cargar perfil
            authRepository.getUserProfile().onSuccess { profile ->
                userProfile = profile
                Log.d("PERFIL_SCREEN", "✅ Perfil cargado: ${profile.firstName}")

                // Cargar publicaciones
                listingRepository.getUserListings(profile.uid).onSuccess { listings ->
                    userListings = listings
                    Log.d("PERFIL_SCREEN", "✅ ${listings.size} publicaciones cargadas")

                    listings.forEach { listing ->
                        Log.d("PERFIL_SCREEN", "- ${listing.title} (${listing.category})")
                    }
                }.onFailure { error ->
                    Log.e("PERFIL_SCREEN", "❌ Error al cargar listings", error)
                }
            }.onFailure { error ->
                Log.e("PERFIL_SCREEN", "❌ Error al cargar perfil", error)
            }

            isRefreshing = false
            isLoading = false
        }
    }

    // 🔥 CARGAR AL INICIO
    LaunchedEffect(Unit) {
        loadUserData()
    }

    // 🔥 RECARGAR CADA 3 SEGUNDOS (temporal para debug)
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            Log.d("PERFIL_SCREEN", "🔄 Auto-recargando...")
            loadUserData()
        }
    }

    val displayName = userProfile?.firstName ?: "Usuario"
    val activeListingsCount = userListings.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E3A8A))
                .padding(vertical = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }

                    // 🔥 BOTÓN DE REFRESCAR
                    IconButton(
                        onClick = { loadUserData() },
                        enabled = !isRefreshing
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar",
                            tint = Color.White,
                            modifier = if (isRefreshing) {
                                Modifier.size(24.dp)
                            } else {
                                Modifier.size(24.dp)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Foto de perfil
                if (userProfile?.photoUrl?.isNotEmpty() == true) {
                    AsyncImage(
                        model = userProfile?.photoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF93C5FD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.firstOrNull()?.toString()?.uppercase() ?: "U",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = displayName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "📍", fontSize = 14.sp)
                    Text(
                        text = "Puno, Perú",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        if (isLoading && userListings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Indicador de recarga
                if (isRefreshing) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFDBEAFE)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF3B82F6)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Actualizando...",
                                fontSize = 14.sp,
                                color = Color(0xFF1E40AF)
                            )
                        }
                    }
                }

                // Sección de publicaciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Artículos publicados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Text(
                        text = "$activeListingsCount",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEFF6FF)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (activeListingsCount == 0) "Sin publicaciones" else "$activeListingsCount activos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3B82F6)
                            )
                            Text(
                                text = if (activeListingsCount == 1) "artículo" else "artículos",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }

                        Button(
                            onClick = onHostDashboard,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            )
                        ) {
                            Text("+ Publicar")
                        }
                    }
                }

                if (userListings.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF9FAFB)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(text = "📦", fontSize = 48.sp)
                            Text(
                                text = "Aún no tienes publicaciones",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF6B7280),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Publica tus productos y empieza a vender o alquilar",
                                fontSize = 14.sp,
                                color = Color(0xFF9CA3AF),
                                textAlign = TextAlign.Center
                            )

                            Button(
                                onClick = { loadUserData() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3B82F6)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Recargar")
                            }
                        }
                    }
                } else {
                    userListings.forEach { listing ->
                        ListingCard(
                            listing = listing,
                            onDeleteClick = {
                                scope.launch {
                                    listingRepository.deleteListing(listing.id).onSuccess {
                                        // Recargar después de eliminar
                                        loadUserData()
                                    }
                                }
                            },
                            onClick = { onProductClick(listing.id) }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E7EB))
                )

                // Opciones del perfil
                Text(
                    text = "Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )

                ProfileOption(
                    emoji = "🏠",
                    text = "Panel de anfitrión",
                    onClick = onHostDashboard
                )

                ProfileOption(
                    emoji = "⚙️",
                    text = "Configuración",
                    onClick = onSettings
                )

                ProfileOption(
                    emoji = "🚪",
                    text = "Cerrar sesión",
                    onClick = {
                        authRepository.logout()
                        onLogout()
                    },
                    textColor = Color(0xFFDC2626)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ListingCard(
    listing: Listing,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit
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
            // Imagen
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
                Text(
                    text = listing.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    maxLines = 2
                )

                Text(
                    text = "S/ ${listing.price} • ${listing.type}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (listing.type == "venta") Color(0xFF10B981) else Color(0xFF3B82F6)
                )

                Text(
                    text = listing.category,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(text = "🗑️", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOption(
    emoji: String,
    text: String,
    onClick: () -> Unit,
    textColor: Color = Color(0xFF111827)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "›",
            fontSize = 20.sp,
            color = Color(0xFF9CA3AF)
        )
    }
}