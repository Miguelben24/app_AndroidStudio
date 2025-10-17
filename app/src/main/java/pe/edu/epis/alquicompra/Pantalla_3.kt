package com.example.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

///graecias chuhcas
@Preview
@Composable
fun Pantalla3Onboarding2() {
    var selectedMode by remember { mutableStateOf<String?>(null) }
    var expandedLocation by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("Puno, Puno") }

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
            IconButton(onClick = {}) {
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

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Sección Modalidad
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

                // Opción Alquilar
                ModeSelectionCard(
                    title = "Alquilar objetos",
                    description = "Renta por días, semanas o meses",
                    icon = "🕐",
                    isSelected = selectedMode == "rent",
                    borderColor = Color(0xFF3B82F6),
                    backgroundColor = Color(0xFFDEF7FF),
                    iconBackgroundColor = Color(0xFFBFDBFE)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Opción Comprar
                ModeSelectionCard(
                    title = "Comprar segunda mano",
                    description = "Productos usados a buen precio",
                    icon = "🛍",
                    isSelected = selectedMode == "buy",
                    borderColor = Color(0xFF10B981),
                    backgroundColor = Color(0xFFECFDF5),
                    iconBackgroundColor = Color(0xFFBFEFD5)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Opción Ambos
                ModeSelectionCard(
                    title = "Me interesan ambos",
                    description = "Alquilar y comprar según necesite",
                    icon = "❤️",
                    isSelected = selectedMode == "both",
                    borderColor = Color(0xFFA855F7),
                    backgroundColor = Color(0xFFF3E8FF),
                    iconBackgroundColor = Color(0xFFE9D5FF)
                )
            }

            // Sección Ubicación
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

                // Ubicación automática (seleccionada)
                LocationCard(
                    title = "Usar mi ubicación actual",
                    description = "Detectar automáticamente",
                    icon = "📍",
                    isSelected = true,
                    backgroundColor = Color(0xFFDEF7FF),
                    borderColor = Color(0xFF3B82F6)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Ubicación manual
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

                        // Dropdown
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

        // Botón continuar
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD1D5DB),
                disabledContainerColor = Color(0xFFD1D5DB)
            ),
            enabled = false
        ) {
            Text(
                text = "Continuar",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF9CA3AF)
            )
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
    iconBackgroundColor: Color
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