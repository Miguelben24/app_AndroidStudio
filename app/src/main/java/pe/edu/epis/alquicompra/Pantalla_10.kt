package pe.edu.epis.alquicompra

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun Pantalla10ProductDetail(
    onBackClick: () -> Unit,
    productId: String
) {
    val context = LocalContext.current
    val listingRepository = remember { ListingRepository(context) }
    val scope = rememberCoroutineScope()

    var listing by remember { mutableStateOf<Listing?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar producto
    LaunchedEffect(productId) {
        scope.launch {
            listingRepository.getAllListings().onSuccess { listings ->
                listing = listings.find { it.id == productId }
                isLoading = false
            }.onFailure {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (listing == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "❌", fontSize = 64.sp)
                Text(
                    text = "Producto no encontrado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text("Volver")
                }
            }
        } else {
            val product = listing!!

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header con imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Botón atrás
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .background(Color.White, CircleShape)
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color(0xFF4B5563)
                        )
                    }

                    // Botón compartir
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                            .background(Color.White, CircleShape)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = Color(0xFF4B5563)
                        )
                    }

                    // Badge de tipo
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .background(
                                if (product.type == "alquiler")
                                    Color(0xFF3B82F6)
                                else
                                    Color(0xFF10B981),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = product.type.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Contenido scrolleable
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Título y precio
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = product.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = { /* Toggle favorito */ },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = Color(0xFF9CA3AF)
                                )
                            }
                        }

                        Text(
                            text = "S/ ${product.price}${if (product.type == "alquiler") "/día" else ""}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (product.type == "alquiler")
                                Color(0xFF3B82F6)
                            else
                                Color(0xFF10B981),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Categoría y ubicación
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📂 ${product.category}",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )

                            Text(
                                text = "📍 ${product.location}",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    // Descripción
                    Column {
                        Text(
                            text = "Descripción",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = product.description,
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            lineHeight = 20.sp
                        )
                    }

                    // Especificaciones
                    if (product.specifications.isNotBlank()) {
                        Column {
                            Text(
                                text = "Especificaciones",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF9FAFB)
                                )
                            ) {
                                Text(
                                    text = product.specifications,
                                    fontSize = 14.sp,
                                    color = Color(0xFF4B5563),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    // Propietario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFF3F4F6))
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Publicado por",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFF3B82F6), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = product.userName.firstOrNull()?.uppercase() ?: "U",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.userName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Miembro de AlquiCompra",
                                    fontSize = 14.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    }
                }

                // Botón de contacto fijo
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val message = "Hola! Me interesa tu publicación: ${product.title}"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse("https://wa.me/${product.userPhone}?text=${Uri.encode(message)}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366)
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "💬", fontSize = 20.sp)
                            Text(
                                text = "Contactar por WhatsApp",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}