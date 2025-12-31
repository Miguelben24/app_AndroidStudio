package pe.edu.epis.alquicompra

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun CreateListingFlow(
    onBackClick: () -> Unit,
    onPublishComplete: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedCategory by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Puno, Puno") }
    var specifications by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var listingType by remember { mutableStateOf("") }

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
            IconButton(onClick = {
                if (currentStep > 1) {
                    currentStep--
                } else {
                    onBackClick()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF4B5563)
                )
            }
            Text(
                text = "Publicar producto",
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
                    .fillMaxWidth(currentStep / 4f)
                    .height(4.dp)
                    .background(Color(0xFF3B82F6))
            )
        }

        Text(
            text = "Paso $currentStep de 4",
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            label = "step_animation"
        ) { step ->
            when (step) {
                1 -> Step1TakePhoto(
                    selectedImageUri = selectedImageUri,
                    onImageSelected = { uri -> selectedImageUri = uri },
                    onContinue = {
                        if (selectedImageUri != null) {
                            currentStep = 2
                        }
                    }
                )
                2 -> Step2SelectCategory(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { category -> selectedCategory = category },
                    onContinue = {
                        if (selectedCategory.isNotEmpty()) {
                            currentStep = 3
                        }
                    }
                )
                3 -> Step3ProductDetails(
                    title = title,
                    description = description,
                    price = price,
                    location = location,
                    specifications = specifications,
                    onTitleChange = { title = it },
                    onDescriptionChange = { description = it },
                    onPriceChange = { price = it },
                    onLocationChange = { location = it },
                    onSpecificationsChange = { specifications = it },
                    onContinue = {
                        if (title.isNotBlank() && description.isNotBlank() && price.isNotBlank()) {
                            currentStep = 4
                        }
                    }
                )
                4 -> Step4ContactAndType(
                    phoneNumber = phoneNumber,
                    listingType = listingType,
                    onPhoneChange = { phoneNumber = it },
                    onTypeSelected = { listingType = it },
                    imageUri = selectedImageUri,
                    category = selectedCategory,
                    title = title,
                    description = description,
                    price = price,
                    location = location,
                    specifications = specifications,
                    onPublishComplete = onPublishComplete
                )
            }
        }
    }
}

// PASO 1: TOMAR FOTO
@Composable
fun Step1TakePhoto(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    onContinue: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Añade una foto de tu producto",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Una buena foto ayuda a vender más rápido",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cambiar foto")
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        .border(2.dp, Color(0xFFD1D5DB), RoundedCornerShape(12.dp))
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Tomar foto",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                        Text(
                            text = "Toca para agregar una foto",
                            fontSize = 16.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedImageUri != null,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6)
            )
        ) {
            Text(
                text = "Continuar",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// PASO 2: SELECCIONAR CATEGORÍA
@Composable
fun Step2SelectCategory(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¿En qué categoría está tu producto?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Esto ayuda a las personas a encontrarlo más fácil",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val categories = listOf(
                "Electrónicos" to "📱",
                "Viviendas" to "🏠",
                "Ropa" to "👕",
                "Deportes" to "⚽",
                "Libros" to "📚",
                "Herramientas" to "🔧"
            )

            categories.chunked(2).forEach { rowCategories ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowCategories.forEach { (category, emoji) ->
                        CategoryButton(
                            emoji = emoji,
                            text = category,
                            isSelected = selectedCategory == category,
                            onClick = { onCategorySelected(category) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowCategories.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedCategory.isNotEmpty(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6)
            )
        ) {
            Text(
                text = "Continuar",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CategoryButton(
    emoji: String,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3B82F6) else Color.White
        ),
        border = if (!isSelected)
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF111827),
                textAlign = TextAlign.Center
            )
        }
    }
}

// PASO 3: DETALLES DEL PRODUCTO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step3ProductDetails(
    title: String,
    description: String,
    price: String,
    location: String,
    specifications: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSpecificationsChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cuéntanos sobre tu producto",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "Mientras más detalles, mejor",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Título *") },
                placeholder = { Text("Ej: Laptop HP Pavilion") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Descripción *") },
                placeholder = { Text("Describe tu producto en detalle...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                maxLines = 5
            )

            OutlinedTextField(
                value = price,
                onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) onPriceChange(it) },
                label = { Text("Precio (S/) *") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = { Text("S/", fontWeight = FontWeight.Bold) },
                singleLine = true
            )

            OutlinedTextField(
                value = location,
                onValueChange = onLocationChange,
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = specifications,
                onValueChange = onSpecificationsChange,
                label = { Text("Especificaciones (opcional)") },
                placeholder = { Text("Ej: Intel i5, 8GB RAM, 256GB SSD") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = title.isNotBlank() && description.isNotBlank() && price.isNotBlank(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3B82F6)
            )
        ) {
            Text("Continuar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// PASO 4: CONTACTO Y TIPO
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4ContactAndType(
    phoneNumber: String,
    listingType: String,
    onPhoneChange: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    imageUri: Uri?,
    category: String,
    title: String,
    description: String,
    price: String,
    location: String,
    specifications: String,
    onPublishComplete: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { ListingRepository(context) }
    val scope = rememberCoroutineScope()
    var isPublishing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¡Último paso!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Text(
                text = "¿Cómo quieres que te contacten?",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 15) onPhoneChange(it) },
                label = { Text("Número de WhatsApp *") },
                placeholder = { Text("+51 999 999 999") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Text("📱", fontSize = 20.sp)
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tipo de publicación",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TypeButton(
                    text = "Venta",
                    icon = "💰",
                    isSelected = listingType == "venta",
                    onClick = { onTypeSelected("venta") },
                    modifier = Modifier.weight(1f)
                )

                TypeButton(
                    text = "Alquiler",
                    icon = "🔄",
                    isSelected = listingType == "alquiler",
                    onClick = { onTypeSelected("alquiler") },
                    modifier = Modifier.weight(1f)
                )
            }

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

        Button(
            onClick = {
                if (imageUri != null && phoneNumber.isNotBlank() && listingType.isNotBlank()) {
                    Log.d("PUBLISH_DEBUG", "=== INICIANDO PUBLICACIÓN ===")
                    Log.d("PUBLISH_DEBUG", "Category: $category")
                    Log.d("PUBLISH_DEBUG", "Title: $title")
                    Log.d("PUBLISH_DEBUG", "Type: $listingType")

                    isPublishing = true
                    errorMessage = ""

                    Toast.makeText(context, "📤 Subiendo...", Toast.LENGTH_SHORT).show()

                    scope.launch {
                        try {
                            val result = repository.createListing(
                                imageUri = imageUri,
                                category = category,
                                title = title,
                                description = description,
                                price = price.toDoubleOrNull() ?: 0.0,
                                type = listingType,
                                location = location,
                                specifications = specifications,
                                userPhone = phoneNumber
                            )

                            isPublishing = false

                            result.onSuccess { listingId ->
                                Log.d("PUBLISH_DEBUG", "✅ ÉXITO! ID: $listingId")
                                Toast.makeText(context, "✅ ¡Publicado!", Toast.LENGTH_LONG).show()
                                onPublishComplete()
                            }.onFailure { error ->
                                Log.e("PUBLISH_DEBUG", "❌ ERROR", error)
                                errorMessage = "Error: ${error.message}"
                                Toast.makeText(context, "❌ ${error.message}", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            isPublishing = false
                            Log.e("PUBLISH_DEBUG", "❌ EXCEPCIÓN", e)
                            errorMessage = "Error: ${e.message}"
                            Toast.makeText(context, "❌ ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "⚠️ Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = phoneNumber.isNotBlank() && listingType.isNotBlank() && !isPublishing,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            )
        ) {
            if (isPublishing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🚀", fontSize = 20.sp)
                    Text("Publicar ahora", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TypeButton(
    text: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF10B981) else Color.White
        ),
        border = if (!isSelected)
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF111827)
            )
        }
    }
}