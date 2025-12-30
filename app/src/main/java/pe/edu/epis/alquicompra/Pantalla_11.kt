package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Pantalla11ProductInfo(
    onBackClick: () -> Unit,
    onReserveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF4B5563)
                )
            }
            Text(
                text = "Información del producto",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            Box(modifier = Modifier.width(40.dp))
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFF3F4F6))
        )

        // Contenido scrolleable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen/Galería
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color(0xFFE8EAF6), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📸 Galería de imágenes",
                        fontSize = 14.sp,
                        color = Color(0xFF9575CD)
                    )
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF5E35B1), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "1/8",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Badge de tipo
            Box(
                modifier = Modifier
                    .background(Color(0xFF00BFA5), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Alquiler",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Título
            Text(
                text = "Carpa familiar 3 personas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            // Precio
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "S/ 35",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5E35B1)
                )
                Text(
                    text = "/día",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF5E35B1)
                )
            }

            TextButton(
                onClick = { },
                modifier = Modifier.padding(0.dp)
            ) {
                Text(
                    text = "Ver tarifas completas",
                    fontSize = 14.sp,
                    color = Color(0xFF5E35B1)
                )
            }

            // Detalles
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📍", fontSize = 16.sp)
                    Text(
                        text = "Jr. La Cultura 123, Puno (1.2 km)",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "👥", fontSize = 16.sp)
                    Text(
                        text = "Hasta 3 personas",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🏕️", fontSize = 16.sp)
                    Text(
                        text = "Camping",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Carpa familiar ideal para acampar cerca del lago Titicaca. Incluye sleeping bags y colchones inflables. Perfecta para aventuras familiares y escapadas de fin de semana. Resistente al agua y fácil de armar.",
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Reglas
            Text(
                text = "Reglas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "• Cuidado responsable del equipo",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
                Text(
                    text = "• Devolución en las mismas condiciones",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
                Text(
                    text = "• Entrega: 9:00 AM",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
                Text(
                    text = "• Devolución: 6:00 PM",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Footer con precio y botón
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Desde S/ 70",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "2 días",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                Button(
                    onClick = onReserveClick,
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5E35B1)
                    )
                ) {
                    Text(
                        text = "Seleccionar fechas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}