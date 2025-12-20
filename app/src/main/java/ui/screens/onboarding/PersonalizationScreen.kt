package pe.edu.epis.alquicompra.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var selectedLocation by remember { mutableStateOf("Puno, Puno") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Personaliza tu experiencia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Atrás",
                        tint = Color(0xFF4B5563)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // ¿Qué te interesa más?
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿Qué te interesa más?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Esto nos ayuda a mostrarte contenido relevante",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Alquilar
                ModeCard(
                    title = "Alquilar objetos",
                    description = "Renta por días, semanas o meses",
                    icon = "🕐",
                    isSelected = selectedMode == "rent",
                    onClick = { selectedMode = "rent" },
                    backgroundColor = Color(0xFFDEF7FF),
                    borderColor = Color(0xFF3B82F6)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Comprar
                ModeCard(
                    title = "Comprar segunda mano",
                    description = "Productos usados a buen precio",
                    icon = "🛍️",
                    isSelected = selectedMode == "buy",
                    onClick = { selectedMode = "buy" },
                    backgroundColor = Color(0xFFECFDF5),
                    borderColor = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ambos
                ModeCard(
                    title = "Me interesan ambos",
                    description = "Alquilar y comprar según necesite",
                    icon = "❤️",
                    isSelected = selectedMode == "both",
                    onClick = { selectedMode = "both" },
                    backgroundColor = Color(0xFFF3E8FF),
                    borderColor = Color(0xFFA855F7)
                )
            }

            // ¿Dónde te encuentras?
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "¿Dónde te encuentras?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Para mostrarte productos cerca de ti",
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Ubicación automática
                LocationCard(
                    title = "Usar mi ubicación actual",
                    description = "Detectar automáticamente",
                    icon = "📍",
                    isSelected = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Botón Continuar
        Button(
            onClick = onNavigateToHome,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6)
            ),
            enabled = selectedMode != null
        ) {
            Text(
                text = "Continuar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    description: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    backgroundColor: Color,
    borderColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isSelected) borderColor else Color(0xFFE5E7EB),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) backgroundColor else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (isSelected) borderColor.copy(alpha = 0.2f) else Color(0xFFF3F4F6),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun LocationCard(
    title: String,
    description: String,
    icon: String,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isSelected) Color(0xFF3B82F6) else Color(0xFFE5E7EB),
                RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFDEF7FF) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        if (isSelected) Color(0xFFBFDBFE) else Color(0xFFF3F4F6),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}