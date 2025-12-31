package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pantalla6Home(
    onSearchClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onFavoritesClick: () -> Unit = {}  // 🔥 NUEVA FUNCIÓN
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var firstName by remember { mutableStateOf("Usuario") }

    val auth = FirebaseAuth.getInstance()
    val context = androidx.compose.ui.platform.LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val result = authRepository.getUserProfile()
            result.onSuccess { profile ->
                firstName = profile.firstName.ifEmpty {
                    profile.fullName.split(" ").firstOrNull() ?: "Usuario"
                }
            }.onFailure {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    firstName = currentUser.displayName?.split(" ")?.firstOrNull() ?: "Usuario"
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Hola, $firstName! 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "📍 Puno, Perú",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFF3F4F6), CircleShape)
                ) {
                    Text(text = "🔔", fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Qué necesitas hoy?") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF9CA3AF)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedContainerColor = Color.White
                ),
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    .also { interactionSource ->
                        androidx.compose.runtime.LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect { interaction ->
                                if (interaction is androidx.compose.foundation.interaction.PressInteraction.Release) {
                                    onSearchClick()
                                }
                            }
                        }
                    }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Acceso rápido
            Column {
                Text(
                    text = "Acceso rápido",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        onClick = { onSearchClick() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "🔄", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Alquilar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Encuentra lo que necesitas",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }

                    Card(
                        onClick = { onSearchClick() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF10B981)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "🛒", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Comprar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Segunda mano",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }

            // Categorías populares
            Column {
                Text(
                    text = "Categorías populares",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard("📱", "Electrónicos", Modifier.weight(1f)) {
                        onCategoryClick("Electrónicos")
                    }
                    CategoryCard("🏠", "Viviendas", Modifier.weight(1f)) {
                        onCategoryClick("Viviendas")
                    }
                    CategoryCard("👕", "Ropa", Modifier.weight(1f)) {
                        onCategoryClick("Ropa")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CategoryCard("⚽", "Deportes", Modifier.weight(1f)) {
                        onCategoryClick("Deportes")
                    }
                    CategoryCard("📚", "Libros", Modifier.weight(1f)) {
                        onCategoryClick("Libros")
                    }
                    CategoryCard("🔧", "Herramientas", Modifier.weight(1f)) {
                        onCategoryClick("Herramientas")
                    }
                }
            }
        }

        // Bottom Navigation
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Inicio"
                    )
                },
                label = { Text("Inicio", fontSize = 10.sp) },
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3B82F6),
                    selectedTextColor = Color(0xFF3B82F6),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF),
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
                        contentDescription = "Buscar"
                    )
                },
                label = { Text("Buscar", fontSize = 10.sp) },
                selected = selectedTab == 1,
                onClick = {
                    selectedTab = 1
                    onSearchClick()
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3B82F6),
                    selectedTextColor = Color(0xFF3B82F6),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF),
                    indicatorColor = Color.Transparent
                )
            )

            // 🔥 FAVORITOS
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 2) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favoritos"
                    )
                },
                label = { Text("Favoritos", fontSize = 10.sp) },
                selected = selectedTab == 2,
                onClick = {
                    selectedTab = 2
                    onFavoritesClick()
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFEF4444),
                    selectedTextColor = Color(0xFFEF4444),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF),
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Perfil"
                    )
                },
                label = { Text("Perfil", fontSize = 10.sp) },
                selected = selectedTab == 3,
                onClick = {
                    selectedTab = 3
                    onProfileClick()
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3B82F6),
                    selectedTextColor = Color(0xFF3B82F6),
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun CategoryCard(emoji: String, label: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF111827)
            )
        }
    }
}