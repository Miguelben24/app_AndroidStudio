package pe.edu.epis.alquicompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlquiCompraTheme {
                AlquiCompraApp()
            }
        }
    }
}

// Tema centralizado
@Composable
fun AlquiCompraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF3B82F6),
            secondary = Color(0xFF60A5FA),
            tertiary = Color(0xFF1E40AF),
            background = Color.White,
            surface = Color(0xFFFAFAFA),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF111827),
            onSurface = Color(0xFF374151)
        ),
        content = content
    )
}

// Rutas de navegación
object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val ONBOARDING2 = "onboarding2"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SEARCH = "search"
    const val SEARCH_MAP = "search_map"
    const val FILTERS = "filters"
    const val FILTERS_CONFIRMATION = "filters_confirmation"
    const val ANIMATIONS_DEMO = "animations_demo"
}

// App principal con navegación
@Composable
fun AlquiCompraApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(tween(300))
        }
    ) {
        // SPLASH SCREEN
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigate = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ONBOARDING 1
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onStartClick = { navController.navigate(Routes.ONBOARDING2) },
                onLoginClick = { navController.navigate(Routes.LOGIN) }
            )
        }

        // ONBOARDING 2 - Personaliza tu experiencia
        composable(Routes.ONBOARDING2) {
            Pantalla5Onboarding2(
                onBackClick = { navController.popBackStack() },
                onContinueClick = {
                    // AQUÍ ESTÁ EL CAMBIO: Ahora va a REGISTER en vez de quedarse sin acción
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // LOGIN
        composable(Routes.LOGIN) {
            Pantalla3Login(
                onBackClick = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onSkipLogin = {
                    // Continuar sin cuenta - va directo a HOME
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // REGISTER
        composable(Routes.REGISTER) {
            Pantalla4Register(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onSkipRegister = {
                    // Explorar sin cuenta - va directo a HOME
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(Routes.HOME) {
            Pantalla6Home(
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onAnimationsClick = { navController.navigate(Routes.ANIMATIONS_DEMO) }
            )
        }

        // SEARCH (Lista)
        composable(Routes.SEARCH) {
            Pantalla7Search(
                onBackClick = { navController.popBackStack() },
                onMapViewClick = { navController.navigate(Routes.SEARCH_MAP) },
                onFilterClick = { navController.navigate(Routes.FILTERS) }
            )
        }

        // SEARCH MAP (Vista de mapa)
        composable(Routes.SEARCH_MAP) {
            Pantalla8SearchMap(
                onBackClick = { navController.popBackStack() },
                onListViewClick = { navController.popBackStack() },
                onFilterClick = { navController.navigate(Routes.FILTERS) }
            )
        }

        // FILTERS (Pantalla de filtros)
        composable(Routes.FILTERS) {
            Pantalla9_5Filtros(
                onCloseClick = { navController.popBackStack() },
                onApplyFilters = {
                    navController.navigate(Routes.FILTERS_CONFIRMATION) {
                        popUpTo(Routes.FILTERS) { inclusive = true }
                    }
                }
            )
        }

        // FILTERS CONFIRMATION
        composable(Routes.FILTERS_CONFIRMATION) {
            Pantalla9_5_2Confirmacion(
                onOpenFilters = {
                    navController.popBackStack()
                    navController.navigate(Routes.FILTERS)
                },
                onContinue = {
                    navController.navigate(Routes.SEARCH) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        // ANIMATIONS DEMO
        composable(Routes.ANIMATIONS_DEMO) {
            AnimationsDemoScreen(navController)
        }
    }
}

// ==================== SPLASH SCREEN ====================
@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "loader")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(3000)
        onNavigate()
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1E40AF),
            Color(0xFF3B82F6),
            Color(0xFF60A5FA)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(800)),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 48.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .size(96.dp)
                            .padding(bottom = 16.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = "🛡️", fontSize = 48.sp)
                        }
                    }

                    Text(
                        text = "AlquiCompra",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Puno",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Text(
                    text = "Alquila y compra cerca de ti",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(rotation),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 4.dp,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Text(
                        text = "Cargando...",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Versión 1.0.0",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextButton(onClick = { }) {
                        Text(
                            text = "Términos y privacidad",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.75f),
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    }
}

// ==================== ONBOARDING SCREEN ====================
@Composable
fun OnboardingScreen(
    onStartClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var showIcons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showIcons = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFDBEAFE),
                            Color(0xFFBFDBFE)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val scale by animateFloatAsState(
                    targetValue = if (showIcons) 1f else 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )

                Surface(
                    modifier = Modifier.size((128 * scale).dp),
                    shape = CircleShape,
                    color = Color(0xFF3B82F6)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "💼", fontSize = 64.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AnimatedIconCircle(emoji = "👥", color = Color(0xFFFBBF24), show = showIcons, delay = 0)
                    AnimatedIconCircle(emoji = "🏠", color = Color(0xFF34D399), show = showIcons, delay = 100)
                    AnimatedIconCircle(emoji = "📱", color = Color(0xFFA78BFA), show = showIcons, delay = 200)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Bienvenido a AlquiCompra Puno",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Alquila por días o compra objetos de segunda mano. Conecta con tu comunidad local de forma fácil y segura.",
                    fontSize = 16.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    )
                ) {
                    Text(
                        text = "Comenzar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        fontSize = 14.sp,
                        color = Color(0xFF3B82F6)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedIconCircle(emoji: String, color: Color, show: Boolean, delay: Int) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(show) {
        if (show) {
            delay(delay.toLong())
            visible = true
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Surface(
        modifier = Modifier.size((32 * scale).dp),
        shape = CircleShape,
        color = color
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = emoji, fontSize = 16.sp)
        }
    }
}

// ==================== PANTALLA DE DEMOSTRACIONES ====================
@Composable
fun AnimationsDemoScreen(navController: NavHostController) {
    var selectedDemo by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Text("← Volver", fontSize = 16.sp, color = Color(0xFF3B82F6))
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Demo Animaciones",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.weight(1f))
        }

        Divider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { selectedDemo = 0 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDemo == 0) Color(0xFF3B82F6) else Color(0xFFE5E7EB)
                )
            ) {
                Text("Demo 1", color = if (selectedDemo == 0) Color.White else Color.Black)
            }

            Button(
                onClick = { selectedDemo = 1 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDemo == 1) Color(0xFF3B82F6) else Color(0xFFE5E7EB)
                )
            ) {
                Text("Demo 2", color = if (selectedDemo == 1) Color.White else Color.Black)
            }

            Button(
                onClick = { selectedDemo = 2 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedDemo == 2) Color(0xFF3B82F6) else Color(0xFFE5E7EB)
                )
            ) {
                Text("Demo 3", color = if (selectedDemo == 2) Color.White else Color.Black)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedDemo) {
                0 -> AnimacionDemo1()
                1 -> AnimacionDemo2()
                2 -> AnimacionDemo3()
            }
        }
    }
}

@Composable
fun AnimacionDemo1() {
    var showView by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("AnimatedVisibility", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { showView = !showView }) {
            Text(if (showView) "Ocultar Vista" else "Mostrar Vista")
        }

        AnimatedVisibility(
            visible = showView,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF3B82F6), shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("¡Vista Animada! 🎉", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun AnimacionDemo2() {
    var isSelected by remember { mutableStateOf(false) }

    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF3B82F6) else Color(0xFFEF4444),
        animationSpec = tween(500)
    )

    val animatedSize by animateDpAsState(
        targetValue = if (isSelected) 200.dp else 80.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Color y Tamaño Animado", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { isSelected = !isSelected }) {
            Text(if (isSelected) "Desseleccionar" else "Seleccionar")
        }

        Box(
            modifier = Modifier
                .size(animatedSize)
                .background(animatedColor, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                if (isSelected) "Grande 📦" else "Pequeño",
                color = Color.White,
                fontSize = if (isSelected) 24.sp else 14.sp
            )
        }
    }
}

@Composable
fun AnimacionDemo3() {
    var isSelected by remember { mutableStateOf(false) }

    val animatedOffset by animateOffsetAsState(
        targetValue = if (isSelected) Offset(100f, 100f) else Offset(0f, 0f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val animatedFloat by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Offset y Float Animado", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Button(onClick = { isSelected = !isSelected }) {
            Text("Animar Posición")
        }

        Box(
            modifier = Modifier
                .offset(animatedOffset.x.dp, animatedOffset.y.dp)
                .size(100.dp)
                .background(Color(0xFF10B981), shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text("Moviendo 🚀", color = Color.White)
        }

        Text(

            "Float animado: ${String.format("%.2f", animatedFloat)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        LinearProgressIndicator(
            progress = animatedFloat,
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = Color(0xFF3B82F6)
        )
    }
}