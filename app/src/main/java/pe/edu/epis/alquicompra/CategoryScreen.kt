package pe.edu.epis.alquicompra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CategoryScreen(
    category: String,
    onBackClick: () -> Unit,
    onProductClick: (Listing) -> Unit
) {
    val context = LocalContext.current
    val listingRepository = remember { ListingRepository(context) }
    val scope = rememberCoroutineScope()

    var listings by remember { mutableStateOf<List<Listing>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar productos de la categoría
    LaunchedEffect(category) {
        scope.launch {
            listingRepository.getListingsByCategory(category).onSuccess { result ->
                listings = result
                isLoading = false
            }.onFailure {
                isLoading = false
            }
        }
    }

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
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF4B5563)
                )
            }

            Column {
                Text(
                    text = category,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "${listings.size} ${if (listings.size == 1) "producto" else "productos"}",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Divider(color = Color(0xFFF3F4F6))

        // Contenido
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else if (listings.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "📦", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No hay productos en esta categoría",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listings.forEach { listing ->
                    SearchProductCard(
                        listing = listing,
                        onClick = { onProductClick(listing) }
                    )
                }
            }
        }
    }
}