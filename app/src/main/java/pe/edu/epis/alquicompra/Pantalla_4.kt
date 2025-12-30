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
fun Pantalla4Register(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onSkipRegister: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val authRepository = remember { AuthRepository() }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val isNameValid = fullName.length >= 3
    val isEmailValid = email.contains("@") && email.contains(".")
    val isPhoneValid = phone.length >= 9
    val isPasswordValid = password.length >= 8
    val isFormValid = isNameValid && isEmailValid && isPhoneValid && isPasswordValid && termsAccepted

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
                    text = "Crear cuenta",
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
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
            FormField(
                label = "Nombre completo",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Tu nombre completo",
                isError = showErrors && !isNameValid,
                errorMessage = "El nombre debe tener al menos 3 caracteres",
                enabled = !isLoading
            )

            FormField(
                label = "Correo electrónico",
                value = email,
                onValueChange = { email = it },
                placeholder = "tu@email.com",
                isError = showErrors && !isEmailValid,
                errorMessage = "Ingresa un correo válido",
                enabled = !isLoading
            )

            FormField(
                label = "Teléfono",
                value = phone,
                onValueChange = { phone = it },
                placeholder = "+51 999 999 999",
                isError = showErrors && !isPhoneValid,
                errorMessage = "El teléfono debe tener al menos 9 dígitos",
                enabled = !isLoading
            )

            FormField(
                label = "Contraseña",
                value = password,
                onValueChange = { password = it },
                placeholder = "Mínimo 8 caracteres",
                isPassword = true,
                isError = showErrors && !isPasswordValid,
                errorMessage = "La contraseña debe tener al menos 8 caracteres",
                enabled = !isLoading
            )

            if (password.isNotEmpty()) {
                PasswordStrengthIndicator(password)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    modifier = Modifier.padding(top = 2.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF3B82F6),
                        uncheckedColor = if (showErrors && !termsAccepted)
                            Color(0xFFEF4444) else Color(0xFFD1D5DB)
                    ),
                    enabled = !isLoading
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Acepto los términos y condiciones y la política de privacidad",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 20.sp
                    )

                    AnimatedVisibility(
                        visible = showErrors && !termsAccepted,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = "Debes aceptar los términos",
                            fontSize = 12.sp,
                            color = Color(0xFFEF4444),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (isFormValid) {
                        isLoading = true
                        scope.launch {
                            val result = authRepository.register(
                                email = email,
                                password = password,
                                fullName = fullName,
                                phone = phone
                            )

                            isLoading = false

                            result.onSuccess {
                                onRegisterSuccess()
                            }.onFailure { error ->
                                showErrors = true
                                println("Error: ${error.message}")
                            }
                        }
                    } else {
                        showErrors = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6),
                    disabledContainerColor = Color(0xFFD1D5DB)
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
                        text = "Crear cuenta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            OutlinedButton(
                onClick = onSkipRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF6B7280)
                ),
                enabled = !isLoading
            ) {
                Text(
                    text = "Explorar sin cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            visualTransformation = if (isPassword) PasswordVisualTransformation()
            else androidx.compose.ui.text.input.VisualTransformation.None,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Color(0xFFEF4444) else Color(0xFFD1D5DB),
                focusedBorderColor = if (isError) Color(0xFFEF4444) else Color(0xFF3B82F6),
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                errorBorderColor = Color(0xFFEF4444)
            ),
            isError = isError,
            enabled = enabled
        )

        AnimatedVisibility(
            visible = isError,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color(0xFFEF4444),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun PasswordStrengthIndicator(password: String) {
    val strength = remember(password) {
        when {
            password.length < 8 -> 0
            password.length < 12 && password.any { it.isDigit() } -> 1
            password.length >= 12 && password.any { it.isDigit() } &&
                    password.any { it.isUpperCase() } -> 2
            else -> 0
        }
    }

    val color = when (strength) {
        0 -> Color(0xFFEF4444)
        1 -> Color(0xFFFBBF24)
        2 -> Color(0xFF10B981)
        else -> Color(0xFFD1D5DB)
    }

    val label = when (strength) {
        0 -> "Débil"
        1 -> "Media"
        2 -> "Fuerte"
        else -> ""
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            if (index <= strength) color else Color(0xFFE5E7EB),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        Text(
            text = "Fortaleza: $label",
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}