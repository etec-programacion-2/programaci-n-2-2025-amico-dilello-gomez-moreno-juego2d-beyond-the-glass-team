package org.example.desktop

// --- CAMBIO: Imports simplificados ---
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.audio.Music
import org.example.core.AssetManager // Nuestro manifiesto del core

/**
 * (Patrón de Diseño: Inyección de Dependencias)
 *
 * (ACTUALIZADO: Estrategia de Carga Simplificada)
 *
 * Esta clase ahora carga assets directamente usando Gdx.files.internal
 * y los almacena en un Map, lo que es mucho más robusto para
 * configuraciones de proyecto no estándar.
 */
class GdxAssetLoader {
    
    // --- CAMBIO: Usamos mapas simples en lugar de LibGdxAssetManager ---
    private val textureCache = mutableMapOf<String, Texture>()
    private val soundCache = mutableMapOf<String, Sound>()
    private val musicCache = mutableMapOf<String, Music>()

    /**
     * (Criterio de Aceptación 3: Cargar recursos al inicio)
     *
     * Inicia la carga de todos los assets definidos en el manifiesto.
     * Esta es una carga directa, síncrona (bloqueante).
     */
    fun loadAssets() {
        println("[GdxAssetLoader]: Iniciando carga de assets (Método Directo)...")
        
        // --- Carga de Texturas ---
        AssetManager.getAllTexturePaths().forEach { path ->
            try {
                // --- CAMBIO: Carga directa ---
                // Gdx.files.internal() busca en la classpath (ej: desktop/src/main/resources/)
                // Si tu carpeta 'assets' está ahí, 'assets/jugador/...' funcionaría.
                // Si tu carpeta 'jugador' está en la raíz, 'jugador/...' (como está ahora) funcionará.
                println("[GdxAssetLoader]: Cargando textura: $path")
                val texture = Texture(Gdx.files.internal(path))
                textureCache[path] = texture
                println("[GdxAssetLoader]: ...Éxito.")
            } catch (e: Exception) {
                // Si esto se imprime, el archivo NO está en la classpath.
                println("[GdxAssetLoader]: ¡ERROR AL CARGAR TEXTURA! ¿Está el archivo '$path' en 'desktop/src/main/resources'?")
                println(e.message)
            }
        }

        // --- Carga de Sonidos (Simulada por ahora) ---
        AssetManager.getAllSoundPaths().forEach { path ->
            try {
                // val sound = Gdx.audio.newSound(Gdx.files.internal(path))
                // soundCache[path] = sound
            } catch (e: Exception) {
                println("[GdxAssetLoader]: ¡ERROR AL CARGAR SONIDO! '$path'")
            }
        }
        
        // --- Carga de Música (Simulada por ahora) ---
        AssetManager.getAllMusicPaths().forEach { path ->
            try {
                // val music = Gdx.audio.newMusic(Gdx.files.internal(path))
                // musicCache[path] = music
            } catch (e: Exception) {
                println("[GdxAssetLoader]: ¡ERROR AL CARGAR MÚSICA! '$path'")
            }
        }
        
        println("[GdxAssetLoader]: Carga completada.")
    }

    /**
     * (Criterio de Aceptación 2: Provee métodos de acceso)
     *
     * Obtiene un asset ya cargado desde el caché (el Map).
     */
    fun getTexture(key: String): Texture? {
        val texture = textureCache[key]
        if (texture == null) {
            println("[GdxAssetLoader]: AVISO: Se pidió textura '$key' pero no está en caché (¿falló al cargar?).")
        }
        return texture
    }
    
    fun getSound(key: String): Sound? {
        return soundCache[key]
    }

    /**
     * Libera todos los recursos cargados de la memoria.
     */
    fun dispose() {
        println("[GdxAssetLoader]: Liberando todos los assets.")
        // --- CAMBIO: Disponer desde los mapas ---
        textureCache.values.forEach { it.dispose() }
        soundCache.values.forEach { it.dispose() }
        musicCache.values.forEach { it.dispose() }
    }
}