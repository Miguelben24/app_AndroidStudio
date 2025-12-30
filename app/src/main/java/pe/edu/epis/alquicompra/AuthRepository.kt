package pe.edu.epis.alquicompra

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Usuario actual
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // REGISTRO
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String
    ): Result<FirebaseUser> {
        return try {
            // 1. Crear usuario en Authentication
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Error al crear usuario")

            // 2. Guardar datos adicionales en Firestore
            val userData = hashMapOf(
                "uid" to user.uid,
                "email" to email,
                "fullName" to fullName,
                "phone" to phone,
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(user.uid)
                .set(userData)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // LOGIN
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Error al iniciar sesión")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // LOGOUT
    fun logout() {
        auth.signOut()
    }

    // VERIFICAR SI ESTÁ LOGUEADO
    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}