package pe.edu.epis.alquicompra

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppUtils {

    /**
     * Abre WhatsApp con un número y mensaje predefinido
     * @param context Contexto de la aplicación
     * @param phoneNumber Número de teléfono (puede incluir código de país)
     * @param message Mensaje predefinido (opcional)
     */
    fun openWhatsApp(context: Context, phoneNumber: String, message: String = "") {
        try {
            // Limpiar el número de teléfono (quitar espacios, guiones, etc.)
            val cleanPhone = phoneNumber.replace(Regex("[^0-9+]"), "")

            // Construir el mensaje
            val encodedMessage = Uri.encode(message)

            // Intentar abrir WhatsApp
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$cleanPhone?text=$encodedMessage")
                setPackage("com.whatsapp")
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            // Si falla, intentar abrir el navegador
            try {
                val cleanPhone = phoneNumber.replace(Regex("[^0-9+]"), "")
                val encodedMessage = Uri.encode(message)
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/$cleanPhone?text=$encodedMessage"))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "No se pudo abrir WhatsApp. Verifica que esté instalado.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Verifica si WhatsApp está instalado
     */
    fun isWhatsAppInstalled(context: Context): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.whatsapp", 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}