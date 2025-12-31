package pe.edu.epis.alquicompra

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val firstName: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val createdAt: Long = 0,
    val activeListings: Int = 0
)

class AuthRepository(context: Context? = null) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 🔍 SOLUCIÓN: Configuración correcta de Google Sign In
    val googleSignInClient: GoogleSignInClient? = context?.let {
        try {
            // 📝 IMPORTANTE: Este Web Client ID debe venir de tu google-services.json
            // Busca el archivo google-services.json en app/
            // Copia el "client_id" que tiene "client_type": 3

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("746201808496-a364e45giuv34vck77ipujmu4hrt8ro4.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build()

            val client = GoogleSignIn.getClient(it, gso)

            Log.d("AUTH_REPO", "✅ GoogleSignInClient creado correctamente")
            client

        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Error al crear GoogleSignInClient", e)
            null
        }
    }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // REGISTRO TRADICIONAL
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Result<FirebaseUser> {
        return try {
            Log.d("AUTH_REPO", "Iniciando registro para: $email")

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Error al crear usuario")

            val firstName = fullName.split(" ").firstOrNull() ?: fullName

            val userData = UserProfile(
                uid = user.uid,
                email = email,
                fullName = fullName,
                firstName = firstName,
                phone = phone,
                photoUrl = "",
                createdAt = System.currentTimeMillis(),
                activeListings = 0
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userData)
                .await()

            Log.d("AUTH_REPO", "✅ Usuario registrado: ${user.uid}")
            Result.success(user)

        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Error en registro", e)
            Result.failure(e)
        }
    }

    // LOGIN TRADICIONAL
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            Log.d("AUTH_REPO", "Iniciando login para: $email")

            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Error al iniciar sesión")

            Log.d("AUTH_REPO", "✅ Login exitoso: ${user.uid}")
            Result.success(user)

        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Error en login", e)
            Result.failure(e)
        }
    }

    // 🔥 GOOGLE SIGN IN - CORREGIDO
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser> {
        return try {
            Log.d("AUTH_REPO", "=== GOOGLE SIGN IN ===")
            Log.d("AUTH_REPO", "Account: ${account.email}")
            Log.d("AUTH_REPO", "ID Token: ${account.idToken?.take(20)}...")

            // Verificar que tengamos el token
            val idToken = account.idToken
            if (idToken == null) {
                Log.e("AUTH_REPO", "❌ ID Token es null!")
                throw Exception("No se pudo obtener el token de Google")
            }

            // Crear credencial
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            Log.d("AUTH_REPO", "Credencial creada")

            // Autenticar con Firebase
            val result = auth.signInWithCredential(credential).await()
            val user = result.user ?: throw Exception("Usuario es null después de login")

            Log.d("AUTH_REPO", "✅ Autenticado en Firebase: ${user.uid}")

            // Verificar si es nuevo usuario
            val userDoc = firestore.collection("users").document(user.uid).get().await()

            if (!userDoc.exists()) {
                Log.d("AUTH_REPO", "Usuario nuevo - Creando perfil")

                val firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Usuario"

                val userData = UserProfile(
                    uid = user.uid,
                    email = user.email ?: "",
                    fullName = user.displayName ?: "",
                    firstName = firstName,
                    phone = "",
                    photoUrl = user.photoUrl?.toString() ?: "",
                    createdAt = System.currentTimeMillis(),
                    activeListings = 0
                )

                firestore.collection("users")
                    .document(user.uid)
                    .set(userData)
                    .await()

                Log.d("AUTH_REPO", "✅ Perfil creado en Firestore")
            } else {
                Log.d("AUTH_REPO", "Usuario existente - Perfil ya existe")
            }

            Log.d("AUTH_REPO", "✅ GOOGLE SIGN IN COMPLETO")
            Result.success(user)

        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ ERROR EN GOOGLE SIGN IN", e)
            Log.e("AUTH_REPO", "Mensaje: ${e.message}")
            Log.e("AUTH_REPO", "Stack trace:", e)
            Result.failure(Exception("Error al iniciar sesión con Google: ${e.message}"))
        }
    }

    // OBTENER PERFIL DEL USUARIO
    suspend fun getUserProfile(): Result<UserProfile> {
        return try {
            val userId = currentUser?.uid ?: throw Exception("Usuario no autenticado")

            Log.d("AUTH_REPO", "Obteniendo perfil para: $userId")

            val doc = firestore.collection("users").document(userId).get().await()
            val profile = doc.toObject(UserProfile::class.java)
                ?: throw Exception("Perfil no encontrado")

            Log.d("AUTH_REPO", "✅ Perfil obtenido: ${profile.firstName}")
            Result.success(profile)

        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Error al obtener perfil", e)
            Result.failure(e)
        }
    }

    // LOGOUT
    fun logout() {
        try {
            Log.d("AUTH_REPO", "Cerrando sesión")
            auth.signOut()
            googleSignInClient?.signOut()
            Log.d("AUTH_REPO", "✅ Sesión cerrada")
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Error al cerrar sesión", e)
        }
    }

    // VERIFICAR SI ESTÁ LOGUEADO
    fun isLoggedIn(): Boolean {
        val loggedIn = currentUser != null
        Log.d("AUTH_REPO", "¿Está logueado? $loggedIn")
        return loggedIn
    }
}