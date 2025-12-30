package pe.edu.epis.alquicompra

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Pantalla5Onboarding2(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var expandedLocation by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("Puno, Puno") }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(400)
            )
        ) {
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
                    text = "Personaliza tu experiencia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Box(modifier = Modifier.width(40.dp))
            }
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(600)) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(600)
            )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
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

                    ModeSelectionCard(
                        title = "Alquilar objetos",
                        description = "Renta por días, semanas o meses",
                        icon = "🕐",
                        isSelected = selectedMode == "rent",
                        borderColor = Color(0xFF3B82F6),
                        backgroundColor = Color(0xFFDEF7FF),
                        iconBackgroundColor = Color(0xFFBFDBFE),
                        onClick = { selectedMode = "rent" }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ModeSelectionCard(
                        title = "Comprar segunda mano",
                        description = "Productos usados a buen precio",
                        icon = "🛍",
                        isSelected = selectedMode == "buy",
                        borderColor = Color(0xFF10B981),
                        backgroundColor = Color(0xFFECFDF5),
                        iconBackgroundColor = Color(0xFFBFEFD5),
                        onClick = { selectedMode = "buy" }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ModeSelectionCard(
                        title = "Me interesan ambos",
                        description = "Alquilar y comprar según necesite",
                        icon = "❤️",
                        isSelected = selectedMode == "both",
                        borderColor = Color(0xFFA855F7),
                        backgroundColor = Color(0xFFF3E8FF),
                        iconBackgroundColor = Color(0xFFE9D5FF),
                        onClick = { selectedMode = "both" }
                    )
                }

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

                    LocationCard(
                        title = "Usar mi ubicación actual",
                        description = "Detectar automáticamente",
                        icon = "📍",
                        isSelected = true,
                        backgroundColor = Color(0xFFDEF7FF),
                        borderColor = Color(0xFF3B82F6)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(2.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFF3F4F6), RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("🔍", fontSize = 20.sp)
                                }
                                Text(
                                    text = "Buscar manualmente",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF111827)
                                )
                            }

                            Box {
                                OutlinedTextField(
                                    value = selectedLocation,
                                    onValueChange = {},
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    readOnly = true,
                                    enabled = false,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color(0xFFD1D5DB),
                                        disabledBorderColor = Color(0xFFD1D5DB),
                                        disabledTextColor = Color(0xFF111827)
                                    )
                                )

                                DropdownMenu(
                                    expanded = expandedLocation,
                                    onDismissRequest = { expandedLocation = false },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    listOf(
                                        "Puno, Puno",
                                        "Juliaca, Puno",
                                        "Ilave, Puno",
                                        "Yunguyo, Puno",
                                        "Ayaviri, Puno"
                                    ).forEach { location ->
                                        DropdownMenuItem(
                                            text = { Text(location) },
                                            onClick = {
                                                selectedLocation = location
                                                expandedLocation = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Button(
                onClick = {
                    onContinueClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedMode != null) Color(0xFF3B82F6) else Color(0xFFD1D5DB)
                ),
                enabled = selectedMode != null
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedMode != null) Color.White else Color(0xFF9CA3AF)
                )
            }
        }
    }
}

@Composable
fun ModeSelectionCard(
    title: String,
    description: String,
    icon: String,
    isSelected: Boolean,
    borderColor: Color,
    backgroundColor: Color,
    iconBackgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                2.dp,
                if (isSelected) borderColor else Color(0xFFE5E7EB),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
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
                        if (isSelected) iconBackgroundColor else Color(0xFFF3F4F6),
                        RoundedCornerShape(50)
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
fun LocationCard(
    title: String,
    description: String,
    icon: String,
    isSelected: Boolean,
    backgroundColor: Color,
    borderColor: Color
) {
    Card(
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
                        if (isSelected) Color(0xFFBFDBFE) else Color(0xFFF3F4F6),
                        RoundedCornerShape(50)
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