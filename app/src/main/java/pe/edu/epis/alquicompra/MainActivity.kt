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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val ONBOARDING2 = "onboarding2"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SEARCH = "search"
    const val CATEGORY = "category/{categoryName}"
    const val PRODUCT_DETAIL = "product_detail/{productId}"
    const val PROFILE = "profile"
    const val HOST_DASHBOARD = "host_dashboard"
    const val CREATE_LISTING = "create_listing"
    const val FAVORITES = "favorites"
}

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
        composable(Routes.SPLASH) { //para que gire la bolita
            SplashScreen(
                onNavigate = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onStartClick = { navController.navigate(Routes.ONBOARDING2) },
                onLoginClick = { navController.navigate(Routes.LOGIN) }
            )
        }

        composable(Routes.ONBOARDING2) {
            Pantalla5Onboarding2(
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(Routes.REGISTER) }
            )
        }

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
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            Pantalla4Register(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onSkipRegister = {
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
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onCategoryClick = { category ->
                    navController.navigate("category/$category")
                },
                onFavoritesClick = { navController.navigate(Routes.FAVORITES) }
            )
        }

        // SEARCH - Sin categoría
        composable(Routes.SEARCH) {
            Pantalla7Search(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                categoryFilter = null
            )
        }

        // CATEGORY - Con categoría específica
        composable(
            route = Routes.CATEGORY,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            Pantalla7Search(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                },
                categoryFilter = categoryName
            )
        }

        // PRODUCT DETAIL
        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            Pantalla10ProductDetail(
                onBackClick = { navController.popBackStack() },
                productId = productId
            )
        }

        // PROFILE
        composable(Routes.PROFILE) {
            Pantalla17Perfil(
                onBackClick = { navController.popBackStack() },
                onHostDashboard = { navController.navigate(Routes.HOST_DASHBOARD) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }

        // HOST DASHBOARD
        composable(Routes.HOST_DASHBOARD) {
            Pantalla18Dashboard(
                onBackClick = { navController.popBackStack() },
                onCreateListing = { navController.navigate(Routes.CREATE_LISTING) },
                onManageListings = { navController.navigate(Routes.PROFILE) },
                onProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        // CREATE LISTING
        composable(Routes.CREATE_LISTING) {
            CreateListingFlow(
                onBackClick = { navController.popBackStack() },
                onPublishComplete = {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(Routes.HOST_DASHBOARD) { inclusive = false }
                    }
                }
            )
        }
        composable(Routes.FAVORITES) {
            PantallaFavoritos(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product_detail/$productId")
                }
            )
        }
    }
}

// ==================== SPLASH SCREEN ====================
@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "loader") //rueda de carga
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