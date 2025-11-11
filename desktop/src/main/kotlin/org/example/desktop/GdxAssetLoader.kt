package org.example.desktop

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import org.example.core.AssetManager

/**
 * (ACTUALIZADO: Carga la animación de ATAQUE AÉREO)
 */
class GdxAssetLoader {

    // Caché para almacenar las texturas cargadas
    private val textureCache = mutableMapOf<String, Texture>()

    /**
     * Carga todas las texturas definidas en el AssetManager
     * usando el método directo (Gdx.files.internal).
     */
    fun loadAssets() {
        println("[GdxAssetLoader]: Iniciando carga de assets (Método Directo)...")
        
        // --- CAMBIO: Añadido Player.ATTACK_AIR ---
        val texturePaths = listOf(
            AssetManager.Player.IDLE,
            AssetManager.Player.MOVE,
            AssetManager.Player.JUMP,
            AssetManager.Player.ATTACK_GROUND,
            AssetManager.Player.ATTACK_AIR, // <-- NUEVO
            AssetManager.Enemy.BASIC_MOVE,
            AssetManager.World.PLATFORM_A,
            AssetManager.World.PLATFORM_B,
            AssetManager.World.BACKGROUND,
            AssetManager.Items.ENERGY_FRAGMENT
        )
        
        texturePaths.forEach { path ->
            println("[GdxAssetLoader]: Cargando textura: $path")
            try {
                // Cargar la textura
                val texture = Texture(Gdx.files.internal(path))
                // Almacenar en caché
                textureCache[path] = texture
                println("[GdxAssetLoader]: ...Éxito.")
            } catch (e: Exception) {
                // Capturar el error si el archivo no se encuentra
                println("[GdxAssetLoader]: ¡ERROR AL CARGAR TEXTURA! ¿Está el archivo '$path' en 'desktop/src/main/resources'?")
                e.printStackTrace()
            }
        }
        println("[GdxAssetLoader]: Carga completada.")
    }

    /**
     * Obtiene una textura desde la caché.
     * Devuelve null si no se pudo cargar.
     */
    fun getTexture(key: String): Texture? {
        val texture = textureCache[key]
        if (texture == null) {
            println("[GdxAssetLoader]: AVISO: Se pidió textura '$key' pero no está en caché (¿falló al cargar?).")
        }
        return texture
    }

    /**
     * Libera todas las texturas cargadas de la memoria.
     */
    fun dispose() {
        println("[GdxAssetLoader]: Liberando todos los assets.")
        textureCache.values.forEach { it.dispose() }
        textureCache.clear()
    }
}