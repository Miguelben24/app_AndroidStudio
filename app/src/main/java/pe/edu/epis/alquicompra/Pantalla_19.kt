package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Pantalla19CrearAnuncio(
    onBackClick: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("") }

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
                text = "Crear anuncio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
            Box(modifier = Modifier.size(40.dp))
        }

        // Barra de progreso
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color(0xFFF3F4F6))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(4.dp)
                    .background(Color(0xFFF97316))
            )
        }

        Text(
            text = "Paso 1 de 4",
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "¿Qué quieres anunciar?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Selecciona la categoría que mejor describa tu producto o servicio",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                lineHeight = 20.sp
            )

            // Grid de categorías
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        emoji = "⛺",
                        text = "Camping",
                        color = Color(0xFF1E3A8A),
                        isSelected = selectedCategory == "Camping",
                        onClick = {
                            selectedCategory = "Camping"
                            onCategorySelected("Camping")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        emoji = "🏠",
                        text = "Vivienda",
                        color = Color(0xFF60A5FA),
                        isSelected = selectedCategory == "Vivienda",
                        onClick = {
                            selectedCategory = "Vivienda"
                            onCategorySelected("Vivienda")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        emoji = "🔧",
                        text = "Herramientas",
                        color = Color(0xFF2563EB),
                        isSelected = selectedCategory == "Herramientas",
                        onClick = {
                            selectedCategory = "Herramientas"
                            onCategorySelected("Herramientas")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        emoji = "🪑",
                        text = "Muebles",
                        color = Color(0xFF93C5FD),
                        isSelected = selectedCategory == "Muebles",
                        onClick = {
                            selectedCategory = "Muebles"
                            onCategorySelected("Muebles")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        emoji = "📱",
                        text = "Tecnología",
                        color = Color(0xFF1E40AF),
                        isSelected = selectedCategory == "Tecnología",
                        onClick = {
                            selectedCategory = "Tecnología"
                            onCategorySelected("Tecnología")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        emoji = "🚗",
                        text = "Vehículos",
                        color = Color(0xFF3B82F6),
                        isSelected = selectedCategory == "Vehículos",
                        onClick = {
                            selectedCategory = "Vehículos"
                            onCategorySelected("Vehículos")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard(
                        emoji = "📚",
                        text = "Libros",
                        color = Color(0xFF60A5FA),
                        isSelected = selectedCategory == "Libros",
                        onClick = {
                            selectedCategory = "Libros"
                            onCategorySelected("Libros")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CategoryCard(
                        emoji = "🎵",
                        text = "Música",
                        color = Color(0xFF93C5FD),
                        isSelected = selectedCategory == "Música",
                        onClick = {
                            selectedCategory = "Música"
                            onCategorySelected("Música")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CategoryCard(
    emoji: String,
    text: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .background(
                if (isSelected) color.copy(alpha = 0.9f) else color,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color.White.copy(alpha = if (isSelected) 0.4f else 0.2f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )
            }
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            if (isSelected) {
                Text(
                    text = "✓",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}