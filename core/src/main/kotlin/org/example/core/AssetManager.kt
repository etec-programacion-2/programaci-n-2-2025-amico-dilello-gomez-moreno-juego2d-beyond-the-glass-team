package org.example.core

/**
 * (ACTUALIZADO: Añadida la animación de ATAQUE AÉREO)
 */
object AssetManager {

    private const val ROOT = "assets/"

    /**
     * Assets del Jugador
     */
    object Player {
        const val IDLE = "${ROOT}jugador/Leoric animacion AFK.png"
        const val MOVE = "${ROOT}jugador/Leoric animacion caminar.png"
        const val JUMP = "${ROOT}jugador/Leoric animacion salto y caida.png"
        
        // --- CAMBIO: Separamos el ataque en suelo y aire ---
        const val ATTACK_GROUND = "${ROOT}jugador/Leoric ataque.png"
        const val ATTACK_AIR = "${ROOT}jugador/Leoric ataque en aire.png"
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
        const val JUMP = "${ROOT}sound/jump.mp3"
        const val ATTACK = "${ROOT}sound/attack.mp3"
        const val COLLECT = "${ROOT}sound/collect.mp3"
        const val HIT = "${ROOT}sound/hit.mp3"
        const val MUSIC_LEVEL1 = "${ROOT}music/level1.mp3"
    }

    // --- Método de ayuda para obtener todos los assets ---
    fun getAllTexturePaths(): List<String> {
        return listOf(
            Player.IDLE, Player.MOVE, Player.JUMP, 
            Player.ATTACK_GROUND, Player.ATTACK_AIR, // <-- CAMBIO
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