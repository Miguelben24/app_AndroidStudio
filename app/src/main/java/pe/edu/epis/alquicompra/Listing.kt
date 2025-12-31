
package pe.edu.epis.alquicompra

data class Listing(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "", // "Electrónicos" "Viviendas" "Ropa" "Deportes" "Libros" "Herramientas"
    val price: Double = 0.0,
    val type: String = "", // "venta" o "alquiler"
    val location: String = "",
    val specifications: String = "",
    val imageUrl: String = "",
    val createdAt: Long = 0,
    val isActive: Boolean = true
)