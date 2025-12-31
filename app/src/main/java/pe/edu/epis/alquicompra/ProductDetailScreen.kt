package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun ProductDetailScreen(
    listing: Listing,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Imagen del producto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = listing.imageUrl,
                contentDescription = listing.title,
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
                onClick = { /* Compartir */ },
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
                        if (listing.type == "venta") Color(0xFF10B981) else Color(0xFF3B82F6),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (listing.type == "venta") "VENTA" else "ALQUILER",
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Título y precio
            Column {
                Text(
                    text = listing.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "S/ ${listing.price}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (listing.type == "venta") Color(0xFF10B981) else Color(0xFF3B82F6)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ${listing.location}",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = listing.category,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Divider(color = Color(0xFFF3F4F6))

            // Descripción
            Column {
                Text(
                    text = "Descripción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = listing.description,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 20.sp
                )
            }

            // Especificaciones
            if (listing.specifications.isNotBlank()) {
                Column {
                    Text(
                        text = "Especificaciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = listing.specifications,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        lineHeight = 20.sp
                    )
                }
            }

            Divider(color = Color(0xFFF3F4F6))

            // Info del vendedor
            Column {
                Text(
                    text = "Vendedor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF3B82F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = listing.userName.firstOrNull()?.toString()?.uppercase() ?: "U",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Column {
                        Text(
                            text = listing.userName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "Vendedor verificado",
                            fontSize = 12.sp,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }
        }

        // Botón de contactar por WhatsApp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Button(
                onClick = {
                    val message = "Hola! Me interesa tu producto: ${listing.title} (S/ ${listing.price})"
                    WhatsAppUtils.openWhatsApp(context, listing.userPhone, message)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF25D366) // Color de WhatsApp
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "El vendedor responderá directamente en WhatsApp",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}