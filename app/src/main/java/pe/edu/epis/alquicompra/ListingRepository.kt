package pe.edu.epis.alquicompra

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

class ListingRepository(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val IMGBB_API_KEY = "ca0d005aad5ea965ae5684544eeae85d"

    suspend fun createListing(
        imageUri: Uri,
        category: String,
        title: String,
        description: String,
        price: Double,
        type: String,
        location: String,
        specifications: String,
        userPhone: String
    ): Result<String> {
        return try {
            Log.d("LISTING_REPO", "=== CREANDO LISTING ===")

            val currentUser = auth.currentUser
            if (currentUser == null) {
                Log.e("LISTING_REPO", "❌ Usuario no autenticado")
                throw Exception("Usuario no autenticado")
            }

            Log.d("LISTING_REPO", "✅ Usuario: ${currentUser.email}")

            val userDoc = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()

            val userName = userDoc.getString("firstName") ?: currentUser.displayName ?: "Usuario"
            Log.d("LISTING_REPO", "✅ Nombre: $userName")

            Log.d("LISTING_REPO", "2️⃣ Subiendo imagen...")
            val imageUrl = uploadImageToImgBB(imageUri)
            Log.d("LISTING_REPO", "✅ URL: $imageUrl")

            Log.d("LISTING_REPO", "3️⃣ Creando documento...")
            val listingId = firestore.collection("listings").document().id
            Log.d("LISTING_REPO", "✅ ID: $listingId")

            val listing = Listing(
                id = listingId,
                userId = currentUser.uid,
                userName = userName,
                userPhone = userPhone,
                imageUrl = imageUrl,
                category = category,
                title = title,
                description = description,
                price = price,
                type = type,
                location = location,
                specifications = specifications,
                createdAt = System.currentTimeMillis(),
                isActive = true
            )

            Log.d("LISTING_REPO", "4️⃣ Guardando en Firestore...")
            firestore.collection("listings")
                .document(listingId)
                .set(listing)
                .await()

            Log.d("LISTING_REPO", "✅ Guardado")

            Log.d("LISTING_REPO", "5️⃣ Verificando...")
            val verifyDoc = firestore.collection("listings")
                .document(listingId)
                .get()
                .await()

            if (verifyDoc.exists()) {
                Log.d("LISTING_REPO", "✅ VERIFICADO: Documento existe")
            } else {
                throw Exception("El documento no se guardó")
            }

            Log.d("LISTING_REPO", "6️⃣ Actualizando contador...")
            firestore.collection("users")
                .document(currentUser.uid)
                .update("activeListings", com.google.firebase.firestore.FieldValue.increment(1))
                .await()

            Log.d("LISTING_REPO", "✅ ¡PROCESO COMPLETO!")
            Result.success(listingId)

        } catch (e: Exception) {
            Log.e("LISTING_REPO", "❌ ERROR", e)
            Result.failure(Exception("Error: ${e.message}"))
        }
    }

    private suspend fun uploadImageToImgBB(imageUri: Uri): String = withContext(Dispatchers.IO) {
        try {
            val inputStream: InputStream = context.contentResolver.openInputStream(imageUri)
                ?: throw Exception("No se pudo abrir la imagen")

            var bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            bitmap = correctImageOrientation(imageUri, bitmap)
            bitmap = resizeBitmapIfNeeded(bitmap, 1920, 1920)

            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val imageBytes = outputStream.toByteArray()

            val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val requestBody = FormBody.Builder()
                .add("image", base64Image)
                .build()

            val request = Request.Builder()
                .url("https://api.imgbb.com/1/upload?key=$IMGBB_API_KEY")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
                ?: throw Exception("Respuesta vacía")

            if (!response.isSuccessful) {
                throw Exception("Error HTTP ${response.code}")
            }

            val jsonObject = JSONObject(responseBody)
            if (!jsonObject.getBoolean("success")) {
                throw Exception("ImgBB rechazó la imagen")
            }

            val imageUrl = jsonObject.getJSONObject("data").getString("url")
            imageUrl

        } catch (e: Exception) {
            throw Exception("Error al subir imagen: ${e.message}")
        }
    }

    private fun correctImageOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exif = ExifInterface(inputStream)
            inputStream.close()

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }

            if (!matrix.isIdentity) {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            bitmap
        }
    }

    private fun resizeBitmapIfNeeded(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val ratio = minOf(
            maxWidth.toFloat() / width,
            maxHeight.toFloat() / height
        )

        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    // 🔥 SIN ÍNDICE. . busca por userId y luego filtra en memoria
    suspend fun getUserListings(userId: String): Result<List<Listing>> {
        return try {
            Log.d("LISTING_REPO", "=== OBTENIENDO LISTINGS (SIN ÍNDICE) ===")
            Log.d("LISTING_REPO", "User ID: $userId")

            // Solo filtramos por userId (no requiere índice)
            val snapshot = firestore.collection("listings")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            Log.d("LISTING_REPO", "Documentos encontrados: ${snapshot.size()}")

            // Filtramos en memoria
            val listings = snapshot.documents
                .mapNotNull { doc ->
                    try {
                        doc.toObject(Listing::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                .filter { it.isActive } // Filtro en memoria
                .sortedByDescending { it.createdAt } // Ordenar en memoria

            Log.d("LISTING_REPO", "✅ ${listings.size} listings activos")

            listings.forEach { listing ->
                Log.d("LISTING_REPO", "  - ${listing.title} (${listing.category})")
            }

            Result.success(listings)

        } catch (e: Exception) {
            Log.e("LISTING_REPO", "❌ Error", e)
            Result.failure(e)
        }
    }

    // SIN ÍNDICE - Por categoría
    suspend fun getListingsByCategory(category: String): Result<List<Listing>> {
        return try {
            Log.d("LISTING_REPO", "=== BUSCANDO POR CATEGORÍA (SIN ÍNDICE) ===")
            Log.d("LISTING_REPO", "Categoría: '$category'")

            // Solo filtramos por categoría
            val snapshot = firestore.collection("listings")
                .whereEqualTo("category", category)
                .get()
                .await()

            Log.d("LISTING_REPO", "Documentos: ${snapshot.size()}")

            // Filtrar y ordenar en memoria
            val listings = snapshot.documents
                .mapNotNull { doc -> doc.toObject(Listing::class.java) }
                .filter { it.isActive }
                .sortedByDescending { it.createdAt }

            Log.d("LISTING_REPO", "✅ ${listings.size} activos")
            Result.success(listings)

        } catch (e: Exception) {
            Log.e("LISTING_REPO", "❌ Error", e)
            Result.failure(e)
        }
    }

    // SIN ÍNDICE - Todos
    suspend fun getAllListings(): Result<List<Listing>> {
        return try {
            Log.d("LISTING_REPO", "=== OBTENIENDO TODOS (SIN ÍNDICE) ===")

            // Sin filtros complejos
            val snapshot = firestore.collection("listings")
                .get()
                .await()

            Log.d("LISTING_REPO", "Total documentos: ${snapshot.size()}")

            // Filtrar en memoria
            val listings = snapshot.documents
                .mapNotNull { doc -> doc.toObject(Listing::class.java) }
                .filter { it.isActive }
                .sortedByDescending { it.createdAt }

            Log.d("LISTING_REPO", "✅ ${listings.size} activos")
            Result.success(listings)

        } catch (e: Exception) {
            Log.e("LISTING_REPO", "❌ Error", e)
            Result.failure(e)
        }
    }

    suspend fun deleteListing(listingId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("No autenticado")

            firestore.collection("listings")
                .document(listingId)
                .update("isActive", false)
                .await()

            firestore.collection("users")
                .document(currentUser.uid)
                .update("activeListings", com.google.firebase.firestore.FieldValue.increment(-1))
                .await()

            Log.d("LISTING_REPO", "✅ Eliminado: $listingId")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("LISTING_REPO", "❌ Error al eliminar", e)
            Result.failure(e)
        }
    }
}