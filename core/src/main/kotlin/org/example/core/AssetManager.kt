package org.example.core

/**
 * (Patrón de Diseño: Singleton)
 *
 * (ACTUALIZADO: Corrección de ruta RAÍZ 'assets/')
 * Todos los assets deben tener el prefijo de la carpeta raíz "assets/".
 */
object AssetManager {

    // --- Raíz de los Assets ---
    // (Asumiendo que has puesto la carpeta 'assets' en 'desktop/src/main/resources/')
    private const val ROOT = "assets/"

    // --- Extensiones (para consistencia) ---
    private const val PNG = ".png"
    private const val MP3 = ".mp3"

    /**
     * Assets del Jugador
     */
    object Player {
        const val IDLE = "${ROOT}jugador/Leoric animacion AFK.png"
        const val MOVE = "${ROOT}jugador/Leoric animacion caminar.png"
        const val JUMP = "${ROOT}jugador/Leoric animacion salto y caida.png"
        const val ATTACK = "${ROOT}jugador/Leoric ataque.png" 
    }

    /**
     * Assets de Enemigos
     */
    object Enemy {
        const val BASIC_MOVE = "${ROOT}Esqueleto enemigo/Esqueleto caminando.png"
    }

    /**
     * Assets del Mundo (Nivel)
     */
    object World {
        const val PLATFORM_A = "${ROOT}plataformas/plataforma1_pasto_tierra.png"
        const val PLATFORM_B = "${ROOT}plataformas/plataforma1_dimension_espejo.png" 
        const val BACKGROUND = "${ROOT}plataformas/FondoMontaña.png"
    }

    /**
     * Assets de Items y Coleccionables
     */
    object Items {
        const val ENERGY_FRAGMENT = "${ROOT}coleccionables/Coleccionable-estatico.png"
    }

    /**
     * Assets de Sonido
     */
    object Sound {
        const val JUMP = "${ROOT}sound/jump$MP3"
        const val ATTACK = "${ROOT}sound/attack$MP3"
        const val COLLECT = "${ROOT}sound/collect$MP3"
        const val HIT = "${ROOT}sound/hit$MP3"
        const val MUSIC_LEVEL1 = "${ROOT}music/level1$MP3"
    }

    // --- Método de ayuda para obtener todos los assets ---
    fun getAllTexturePaths(): List<String> {
        return listOf(
            Player.IDLE, Player.MOVE, Player.JUMP, Player.ATTACK,
            Enemy.BASIC_MOVE,
            World.PLATFORM_A, World.PLATFORM_B, World.BACKGROUND,
            Items.ENERGY_FRAGMENT
        )
    }
    
    fun getAllSoundPaths(): List<String> {
        return listOf(
            Sound.JUMP, Sound.ATTACK, Sound.COLLECT, Sound.HIT
        )
    }
    
    fun getAllMusicPaths(): List<String> {
        return listOf(
            Sound.MUSIC_LEVEL1
        )
    }
}