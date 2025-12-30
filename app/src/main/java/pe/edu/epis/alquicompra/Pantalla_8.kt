package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
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
fun Pantalla8SearchMap(
    onBackClick: () -> Unit,
    onListViewClick: () -> Unit,
    onFilterClick: () -> Unit,
    onProductClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedPin by remember { mutableIntStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFD1FAE5),
                            Color(0xFFBFDBFE),
                            Color(0xFFC7D2FE)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Mapa",
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF9CA3AF).copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Mapa de Puno",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280).copy(alpha = 0.75f)
                )
            }

            MapPin(
                modifier = Modifier.offset(x = 150.dp, y = 180.dp),
                isSelected = selectedPin == 1,
                onClick = { selectedPin = 1 }
            )
            MapPin(
                modifier = Modifier.offset(x = 220.dp, y = 300.dp),
                isSelected = selectedPin == 2,
                onClick = { selectedPin = 2 }
            )
            MapPin(
                modifier = Modifier.offset(x = 130.dp, y = 420.dp),
                isSelected = selectedPin == 3,
                onClick = { selectedPin = 3 }
            )
            MapPin(
                modifier = Modifier.offset(x = 90.dp, y = 270.dp),
                isSelected = selectedPin == 4,
                onClick = { selectedPin = 4 }
            )
            MapPin(
                modifier = Modifier.offset(x = 260.dp, y = 390.dp),
                isSelected = selectedPin == 5,
                onClick = { selectedPin = 5 }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
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
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onListViewClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFF3F4F6),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.List,
                            contentDescription = "Vista lista",
                            tint = Color(0xFF6B7280)
                        )
                    }

                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFDBEAFE),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = "Vista mapa",
                            tint = Color(0xFF3B82F6)
                        )
                    }
                }
            }
        }

        if (selectedPin > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    when (selectedPin) {
                                        1 -> listOf(Color(0xFFDBEAFE), Color(0xFFBFDBFE))
                                        2 -> listOf(Color(0xFFD1FAE5), Color(0xFFA7F3D0))
                                        3 -> listOf(Color(0xFFF3E8FF), Color(0xFFE9D5FF))
                                        4 -> listOf(Color(0xFFFECDD3), Color(0xFFFDA4AF))
                                        else -> listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                                    }
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (selectedPin) {
                                1 -> "💻"
                                2 -> "🪑"
                                3 -> "📷"
                                4 -> "🎸"
                                else -> "📱"
                            },
                            fontSize = 32.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = when (selectedPin) {
                                1 -> "Laptop HP Pavilion"
                                2 -> "Silla de oficina ergonómica"
                                3 -> "Cámara Canon EOS R6"
                                4 -> "Guitarra acústica Yamaha"
                                else -> "Smartphone Samsung"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = when (selectedPin) {
                                1 -> "S/ 25/día • Alquiler"
                                2 -> "S/ 150 • Venta"
                                3 -> "S/ 40/día • Alquiler"
                                4 -> "S/ 280 • Venta"
                                else -> "S/ 30/día • Alquiler"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedPin == 1 || selectedPin == 3 || selectedPin == 5)
                                Color(0xFF3B82F6) else Color(0xFF10B981),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "📍 ${when (selectedPin) {
                                1 -> "0.5"
                                2 -> "1.2"
                                3 -> "2.1"
                                4 -> "0.8"
                                else -> "1.5"
                            }} km de ti",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        ),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = "Ver",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MapPin(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(if (isSelected) 36.dp else 30.dp)
            .background(
                color = if (isSelected) Color(0xFFEF4444) else Color(0xFF3B82F6),
                shape = CircleShape
            )
            .border(
                width = 3.dp,
                color = Color.White,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Pin vacío
    }
}