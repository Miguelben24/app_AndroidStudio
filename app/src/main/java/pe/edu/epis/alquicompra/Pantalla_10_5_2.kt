package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Pantalla_10_5_2() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Icono de filtro
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "Filtro",
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF2563EB)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Título
            Text(
                text = "Filtros Aplicados",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtítulo
            Text(
                text = "Los filtros se han aplicado a tu búsqueda",
                fontSize = 16.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Abrir Filtros
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(240.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Abrir Filtros",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}