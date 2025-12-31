package pe.edu.epis.alquicompra

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Pantalla3Login(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onSkipLogin: () -> Unit
) {
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // 🔍 DEBUG: Verificar estado inicial
    LaunchedEffect(Unit) {
        val currentUser = authRepository.currentUser
        val client = authRepository.googleSignInClient

        Log.d("LOGIN_SCREEN", "=== PANTALLA DE LOGIN ===")
        Log.d("LOGIN_SCREEN", "Usuario actual: ${currentUser?.email ?: "ninguno"}")
        Log.d("LOGIN_SCREEN", "GoogleSignInClient existe: ${client != null}")

        if (client == null) {
            Log.e("LOGIN_SCREEN", "⚠️ GoogleSignInClient es NULL!")
        }
    }

    // 🔥 LAUNCHER PARA GOOGLE SIGN IN
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LOGIN_SCREEN", "=== RESULTADO DE GOOGLE SIGN IN ===")
        Log.d("LOGIN_SCREEN", "Result code: ${result.resultCode}")
        Log.d("LOGIN_SCREEN", "RESULT_OK = ${Activity.RESULT_OK}")
        Log.d("LOGIN_SCREEN", "RESULT_CANCELED = ${Activity.RESULT_CANCELED}")

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                Log.d("LOGIN_SCREEN", "✅ Usuario seleccionó cuenta")

                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                scope.launch {
                    try {
                        val account = task.getResult(ApiException::class.java)

                        Log.d("LOGIN_SCREEN", "Cuenta obtenida:")
                        Log.d("LOGIN_SCREEN", "- Email: ${account?.email}")
                        Log.d("LOGIN_SCREEN", "- Nombre: ${account?.displayName}")
                        Log.d("LOGIN_SCREEN", "- ID Token existe: ${account?.idToken != null}")

                        if (account != null) {
                            isLoading = true
                            errorMessage = ""

                            Toast.makeText(
                                context,
                                "Autenticando con Google...",
                                Toast.LENGTH_SHORT
                            ).show()

                            val loginResult = authRepository.signInWithGoogle(account)
                            isLoading = false

                            loginResult.onSuccess { user ->
                                Log.d("LOGIN_SCREEN", "✅ LOGIN EXITOSO!")
                                Log.d("LOGIN_SCREEN", "Usuario: ${user.email}")

                                Toast.makeText(
                                    context,
                                    "¡Bienvenido ${user.displayName}!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onLoginSuccess()

                            }.onFailure { error ->
                                Log.e("LOGIN_SCREEN", "❌ Error en login", error)
                                errorMessage = error.message ?: "Error desconocido"
                                Toast.makeText(
                                    context,
                                    "Error: ${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Log.e("LOGIN_SCREEN", "❌ Account es null")
                            errorMessage = "No se pudo obtener la cuenta de Google"
                        }

                    } catch (e: ApiException) {
                        Log.e("LOGIN_SCREEN", "❌ ApiException: ${e.statusCode}", e)
                        Log.e("LOGIN_SCREEN", "Mensaje: ${e.message}")

                        errorMessage = when (e.statusCode) {
                            12501 -> "Login cancelado"
                            12500 -> "Error de configuración de Google Sign In"
                            else -> "Error: ${e.message}"
                        }

                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            Activity.RESULT_CANCELED -> {
                Log.d("LOGIN_SCREEN", "ℹ️ Usuario canceló el login")
                Toast.makeText(context, "Login cancelado", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Log.e("LOGIN_SCREEN", "❌ Result code desconocido: ${result.resultCode}")
                Toast.makeText(
                    context,
                    "Error inesperado: ${result.resultCode}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
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
                    text = "Iniciar sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Box(modifier = Modifier.width(40.dp))
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFF3F4F6))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Botón de Google
            Button(
                onClick = {
                    if (!isLoading) {
                        Log.d("LOGIN_SCREEN", "🔵 Usuario tocó botón de Google")

                        val client = authRepository.googleSignInClient

                        if (client == null) {
                            Log.e("LOGIN_SCREEN", "❌ GoogleSignInClient es NULL!")
                            errorMessage = "Error: Google Sign In no configurado"
                            Toast.makeText(
                                context,
                                "Error de configuración. Verifica google-services.json",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }

                        // Cerrar sesión anterior para forzar selector de cuenta
                        client.signOut().addOnCompleteListener {
                            Log.d("LOGIN_SCREEN", "Sesión anterior cerrada")

                            val signInIntent = client.signInIntent
                            Log.d("LOGIN_SCREEN", "Intent creado, lanzando...")

                            try {
                                googleSignInLauncher.launch(signInIntent)
                                Log.d("LOGIN_SCREEN", "Launcher ejecutado")
                            } catch (e: Exception) {
                                Log.e("LOGIN_SCREEN", "❌ Error al lanzar", e)
                                Toast.makeText(
                                    context,
                                    "Error: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                enabled = !isLoading
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔵", fontSize = 24.sp)
                    Text(
                        text = "Continuar con Google",
                        fontSize = 16.sp,
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Button(
                onClick = { /* TODO: Facebook */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1877F2)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = false
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "f", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Continuar con Facebook",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                Text(
                    text = "O con email",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
            }

            // Formulario tradicional
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                placeholder = { Text("tu@email.com") },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    focusedBorderColor = Color(0xFF3B82F6)
                ),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFD1D5DB),
                    focusedBorderColor = Color(0xFF3B82F6)
                ),
                enabled = !isLoading
            )

            if (errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEE2E2)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = Color(0xFFDC2626),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        errorMessage = ""
                        scope.launch {
                            val result = authRepository.login(email, password)
                            isLoading = false

                            result.onSuccess {
                                onLoginSuccess()
                            }.onFailure { error ->
                                errorMessage = "Error: ${error.message}"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    fontSize = 14.sp,
                    color = Color(0xFF3B82F6)
                )
            }

            TextButton(
                onClick = onSkipLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Explorar sin cuenta",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}