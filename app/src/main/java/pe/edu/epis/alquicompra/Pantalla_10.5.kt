package pe.edu.epis.alquicompra

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

@Preview
@Composable
fun PantallaFiltros() {
    var precioMin by remember { mutableFloatStateOf(145f) }
    var precioMax by remember { mutableFloatStateOf(1000f) }
    var fechaDesde by remember { mutableStateOf("19/09/2025") }
    var fechaHasta by remember { mutableStateOf("11/10/2025") }
    var distancia by remember { mutableFloatStateOf(5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Filtros",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xFFE0E0E0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // FILTRO DE PRECIO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Precio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Inputs Mínimo y Máximo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Mínimo
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mínimo",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = precioMin.toInt().toString(),
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF5865F2),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedContainerColor = Color(0xFFF8F8F8)
                                ),
                                readOnly = true
                            )
                        }

                        // Máximo
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Máximo",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = "S/${precioMax.toInt()}",
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF5865F2),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedContainerColor = Color(0xFFF8F8F8)
                                ),
                                readOnly = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Slider
                    Slider(
                        value = precioMin,
                        onValueChange = { precioMin = it },
                        valueRange = 0f..2000f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF5865F2),
                            activeTrackColor = Color(0xFF5865F2),
                            inactiveTrackColor = Color(0xFFE0E0E0)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "S/${precioMin.toInt()}",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = "S/${precioMax.toInt()}",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Introduce un rango coherente.",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }

            // FILTRO DE FECHA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Fecha disponible",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Desde
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Desde",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = fechaDesde,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    Text("📅", fontSize = 18.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF5865F2),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedContainerColor = Color(0xFFF8F8F8)
                                ),
                                readOnly = true
                            )
                        }

                        // Hasta
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hasta",
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            OutlinedTextField(
                                value = fechaHasta,
                                onValueChange = {},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    Text("📅", fontSize = 18.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color(0xFFE0E0E0),
                                    focusedBorderColor = Color(0xFF5865F2),
                                    unfocusedContainerColor = Color(0xFFF8F8F8),
                                    focusedContainerColor = Color(0xFFF8F8F8)
                                ),
                                readOnly = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Mostramos sólo fechas con disponibilidad confirmada.",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }

            // FILTRO DE DISTANCIA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Distancia",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Dentro de ${distancia.toInt()} km",
                        fontSize = 16.sp,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Slider(
                        value = distancia,
                        onValueChange = { distancia = it },
                        valueRange = 1f..50f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF5865F2),
                            activeTrackColor = Color(0xFF5865F2),
                            inactiveTrackColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
        }

        // BOTONES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón Limpiar
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF5865F2)
                )
            ) {
                Text(
                    text = "Limpiar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Botón Aplicar
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5865F2)
                )
            ) {
                Text(
                    text = "Aplicar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}