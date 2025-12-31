package pe.edu.epis.alquicompra

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FavoritesRepository(context: Context? = null) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val listingRepository = ListingRepository(context!!)

    // Agregar a favoritos
    suspend fun addFavorite(listingId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("Usuario no autenticado")

            Log.d("FAVORITES_REPO", "Agregando a favoritos: $listingId")

            // Crear ID único: userId_listingId
            val favoriteId = "${currentUser.uid}_$listingId"

            val favorite = Favorite(
                id = favoriteId,
                userId = currentUser.uid,
                listingId = listingId,
                createdAt = System.currentTimeMillis()
            )

            firestore.collection("favorites")
                .document(favoriteId)
                .set(favorite)
                .await()

            Log.d("FAVORITES_REPO", "✅ Agregado a favoritos")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("FAVORITES_REPO", "❌ Error al agregar", e)
            Result.failure(e)
        }
    }

    // Quitar de favoritos
    suspend fun removeFavorite(listingId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("Usuario no autenticado")

            Log.d("FAVORITES_REPO", "Quitando de favoritos: $listingId")

            val favoriteId = "${currentUser.uid}_$listingId"

            firestore.collection("favorites")
                .document(favoriteId)
                .delete()
                .await()

            Log.d("FAVORITES_REPO", "✅ Quitado de favoritos")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e("FAVORITES_REPO", "❌ Error al quitar", e)
            Result.failure(e)
        }
    }

    // Verificar si está en favoritos
    suspend fun isFavorite(listingId: String): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser ?: return Result.success(false)

            val favoriteId = "${currentUser.uid}_$listingId"

            val doc = firestore.collection("favorites")
                .document(favoriteId)
                .get()
                .await()

            Result.success(doc.exists())

        } catch (e: Exception) {
            Log.e("FAVORITES_REPO", "❌ Error al verificar", e)
            Result.success(false)
        }
    }

    // Obtener todos los favoritos del usuario
    suspend fun getUserFavorites(): Result<List<Listing>> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("Usuario no autenticado")

            Log.d("FAVORITES_REPO", "=== OBTENIENDO FAVORITOS ===")

            // Obtener IDs de favoritos
            val favoritesSnapshot = firestore.collection("favorites")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .await()

            Log.d("FAVORITES_REPO", "Favoritos encontrados: ${favoritesSnapshot.size()}")

            val listingIds = favoritesSnapshot.documents.mapNotNull { doc ->
                doc.getString("listingId")
            }

            if (listingIds.isEmpty()) {
                Log.d("FAVORITES_REPO", "No hay favoritos")
                return Result.success(emptyList())
            }

            // Obtener los listings completos
            val listings = mutableListOf<Listing>()

            listingIds.forEach { listingId ->
                try {
                    val listingDoc = firestore.collection("listings")
                        .document(listingId)
                        .get()
                        .await()

                    val listing = listingDoc.toObject(Listing::class.java)
                    if (listing != null && listing.isActive) {
                        listings.add(listing)
                        Log.d("FAVORITES_REPO", "  - ${listing.title}")
                    }
                } catch (e: Exception) {
                    Log.e("FAVORITES_REPO", "Error al cargar listing $listingId", e)
                }
            }

            Log.d("FAVORITES_REPO", "✅ ${listings.size} listings cargados")
            Result.success(listings.sortedByDescending { it.createdAt })

        } catch (e: Exception) {
            Log.e("FAVORITES_REPO", "❌ Error", e)
            Result.failure(e)
        }
    }

    // Obtener IDs de favoritos (más rápido para UI)
    suspend fun getFavoriteIds(): Result<Set<String>> {
        return try {
            val currentUser = auth.currentUser ?: return Result.success(emptySet())

            val snapshot = firestore.collection("favorites")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .await()

            val ids = snapshot.documents.mapNotNull { doc ->
                doc.getString("listingId")
            }.toSet()

            Result.success(ids)

        } catch (e: Exception) {
            Log.e("FAVORITES_REPO", "❌ Error", e)
            Result.success(emptySet())
        }
    }
}