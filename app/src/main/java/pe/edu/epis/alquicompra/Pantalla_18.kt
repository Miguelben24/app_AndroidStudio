package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun Pantalla18Dashboard(
    onBackClick: () -> Unit,
    onCreateListing: () -> Unit,
    onManageListings: () -> Unit,
    onProfile: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val listingRepository = remember { ListingRepository(context) }
    val scope = rememberCoroutineScope()

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var totalListings by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        scope.launch {
            authRepository.getUserProfile().onSuccess { profile ->
                userProfile = profile

                listingRepository.getUserListings(profile.uid).onSuccess { listings ->
                    totalListings = listings.size
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AlquiCompra",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Dashboard Anfitrión",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEF4444), CircleShape)
                        .clickable { /* Notificaciones */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🔔", fontSize = 18.sp)
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF93C5FD), CircleShape)
                        .clickable(onClick = onProfile),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👤", fontSize = 18.sp)
                }
            }
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Banner de bienvenida
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E3A8A), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "¡Hola, ${userProfile?.firstName ?: "Usuario"}!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Tienes $totalListings ${if (totalListings == 1) "publicación" else "publicaciones"}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Tarjeta de publicaciones activas
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFF6FF)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📊", fontSize = 24.sp)
                    Text(
                        text = "Publicaciones Activas",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "$totalListings",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            }

            // Botón publicar anuncio
            Button(
                onClick = onCreateListing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                )
            ) {
                Text(
                    text = "+  Publicar nuevo producto",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Gestión de anuncios
            Text(
                text = "Gestión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onProfile),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFAFAFA)
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Ver mis publicaciones",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "$totalListings activos",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                    Text(
                        text = "→",
                        fontSize = 24.sp,
                        color = Color(0xFF1E3A8A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}