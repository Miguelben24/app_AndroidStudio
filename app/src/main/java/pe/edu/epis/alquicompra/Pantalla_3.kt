package pe.edu.epis.alquicompra


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun Pantalla3Login() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    // 🎨 Nueva paleta
    val primary = Color(0xFF1D5D9B)
    val primaryLight = Color(0xFF49A5D6)
    val accent = Color(0xFFFFC727)
    val bg = Color(0xFFF3F6F9)
    val textDark = Color(0xFF1F2937)
    val textLight = Color(0xFF6B7280)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
    ) {

        // ------------------------------
        // 🔵 BARRA SUPERIOR
        // ------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = textLight
                )
            }

            Text(
                text = "Iniciar sesión",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Box(modifier = Modifier.width(40.dp))
        }

        // ---------------------------------
        // LOGO (AQUÍ COLOCARÁS TU IMAGEN)
        // ---------------------------------
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
            )

            // 👉 AQUI VA TU LOGO (reemplazar el Box por Image)
            // Ejemplo:
            // Image(
            //    painterResource(id = R.drawable.mi_logo),
            //    contentDescription = "Logo",
            //    modifier = Modifier.size(120.dp)
            // )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ------------------------------
        // CAMPOS
        // ------------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {

            // 🔹 Campo correo
            Text(
                text = "Correo electrónico",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = textDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("tu@email.com") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = primaryLight,
                    focusedBorderColor = primary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Campo contraseña
            Text(
                text = "Contraseña",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = textDark,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = primaryLight,
                    focusedBorderColor = primary,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ------------------------------
            // RECORDARME / OLVIDÉ CONTRASEÑA
            // ------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = primary,
                            uncheckedColor = textLight
                        )
                    )
                    Text(text = "Recordarme", fontSize = 14.sp, color = textLight)
                }

                TextButton(onClick = {}) {
                    Text("¿Olvidaste tu contraseña?", color = primary)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ------------------------------
            // BOTÓN PRINCIPAL
            // ------------------------------
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary
                )
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ------------------------------
            // REGISTRARSE
            // ------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("¿No tienes cuenta?", color = textLight)
                TextButton(onClick = {}) {
                    Text("Regístrate", color = primary)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
// Miguel Angel Benito Chambi