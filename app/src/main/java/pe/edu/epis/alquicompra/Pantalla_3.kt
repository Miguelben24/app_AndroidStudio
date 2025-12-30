package pe.edu.epis.alquicompra

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Pantalla3Login(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onSkipLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val authRepository = remember { AuthRepository() }

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
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .background(Color(0xFFFAFAFA))
                    .padding(bottom = 16.dp),
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
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Correo electrónico",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                showError = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("tu@email.com") },
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFD1D5DB),
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                errorBorderColor = Color(0xFFEF4444)
                            ),
                            isError = showError,
                            enabled = !isLoading
                        )
                    }
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Contraseña",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                showError = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Tu contraseña") },
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFD1D5DB),
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                errorBorderColor = Color(0xFFEF4444)
                            ),
                            isError = showError,
                            enabled = !isLoading
                        )
                    }
                    AnimatedVisibility(
                        visible = showError,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = errorMessage,
                            fontSize = 14.sp,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3B82F6),
                                    uncheckedColor = Color(0xFFD1D5DB)
                                ),
                                enabled = !isLoading
                            )
                            Text(
                                text = "Recordarme",
                                fontSize = 14.sp,
                                color = Color(0xFF4B5563)
                            )
                        }

                        TextButton(
                            onClick = { },
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                fontSize = 14.sp,
                                color = Color(0xFF3B82F6)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                isLoading = true
                                scope.launch {
                                    val result = authRepository.login(email, password)

                                    isLoading = false

                                    result.onSuccess {
                                        onLoginSuccess()
                                    }.onFailure { error ->
                                        showError = true
                                        errorMessage = when {
                                            error.message?.contains("password") == true ->
                                                "Contraseña incorrecta"
                                            error.message?.contains("user") == true ->
                                                "Usuario no encontrado"
                                            else ->
                                                "Error al iniciar sesión. Intenta de nuevo."
                                        }
                                    }
                                }
                            } else {
                                showError = true
                                errorMessage = "Por favor, completa todos los campos"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6),
                            disabledContainerColor = Color(0xFF93C5FD)
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
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = onSkipLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF3B82F6)
                        ),
                        enabled = !isLoading
                    ) {
                        Text(
                            text = "Continuar sin cuenta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes cuenta? ",
                            fontSize = 14.sp,
                            color = Color(0xFF4B5563)
                        )
                        TextButton(
                            onClick = onRegisterClick,
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Regístrate aquí",
                                fontSize = 14.sp,
                                color = Color(0xFF3B82F6)
                            )
                        }
                    }
                }
            }
        }
    }
}